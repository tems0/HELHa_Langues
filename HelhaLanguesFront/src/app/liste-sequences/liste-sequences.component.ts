import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SequenceService } from '../Service/sequence.service';
import { MessageService, SelectItem } from 'primeng/api';
import { Sequence } from '../model/sequence';
import { Table } from 'primeng/table';
import { Dropdown } from 'primeng/dropdown';
import { UploadEvent } from '../model/upload-event';
import { Router } from '@angular/router';

@Component({
  selector: 'app-liste-sequences',
  templateUrl: './liste-sequences.component.html',
  styleUrls: ['./liste-sequences.component.css']
})
export class ListeSequencesComponent implements OnInit{

  sequences!: Sequence[];
  sequencesFilter!: Sequence[];
  sequence!: Sequence;
  statuses!: SelectItem[];
  distinctLanguages!:string[];
  clonedSequences: { [s: string]: Sequence } = {};
  isUpload:boolean=false;
  uploadedFiles: File | null = null;
  isStudent: boolean=false;

  @ViewChild('pdrop', { static: false }) pdropInput!: Dropdown ;
  @ViewChild('seqLangues', { static: false }) seqLanguesInput!: ElementRef ;
  @ViewChild('seqtimer', { static: false }) seqtimerInput!: ElementRef ;
  @ViewChild('seqscore', { static: false }) seqscoreInput!: ElementRef ;

  constructor(private sequenceService: SequenceService,private router: Router,private messageService: MessageService) {}

  ngOnInit(): void {
    if(sessionStorage.getItem("role")!=null&&sessionStorage.getItem("role")!="Student"){
      this.isStudent=false;
    }
    else if(sessionStorage.getItem("role")!=null&&sessionStorage.getItem("role")!="Teacher"){
      this.isStudent=true;
    }
    sessionStorage.removeItem('qcmStartTime');
    this.sequenceService.GetAllSequences().subscribe({
      next: (data) => {
        this.sequences =data;
        // Triez this.sequences par langues
        this.sequences.sort((a: Sequence, b: Sequence) => {
          if (a.languages < b.languages) {
            return -1;
          }
          if (a.languages > b.languages) {
            return 1;
          }
          return 0;
        });
        this.sequencesFilter =data;
        const allLanguages = this.sequences.map((sequence: Sequence) => sequence.languages);

        // Utilisez un Set pour obtenir une liste distincte de langues
        this.distinctLanguages = [...new Set(allLanguages)];
        this.distinctLanguages.push("Tout");
        console.log(this.distinctLanguages);
      },
      error: (err) => {
        console.log(err.error.message);
      }
    });
  }

  onUpload(event:any) {
    this.uploadedFiles = event.files[0];
    this.messageService.add({severity: 'info', summary: 'File Uploaded', detail: ''});
    this.isUpload=true;
  }

  onLanguageChange(event: any)
  {
    if (event.value === 'Tout') {
      // Si la langue sélectionnée est "tout", afficher toutes les séquences sans filtre
      this.sequences = this.sequencesFilter;
    } else {
      // Sinon, filtrer les séquences en fonction de la langue sélectionnée
      this.sequences = this.sequencesFilter.filter((sequence: Sequence) => sequence.languages === event.value);
    }
  }

  clear(table: Table) {
    table.clear();

  }

  onRowEditInit(sequence: Sequence) {
    this.clonedSequences[sequence.sequenceId as unknown as string] = { ...sequence };
  }

  followSeq(sequence: Sequence)
  {
    this.router.navigate(['/follow-sequences', sequence.sequenceId]);
  }

  addQuestion(sequence: Sequence)
  {
    this.router.navigate(['/add-question', sequence.sequenceId]);
  }

  modifyQuestion(sequence: Sequence)
  {
    this.router.navigate(['/modify-question', sequence.sequenceId]);
  }

  onRowEditSave(sequence: Sequence) {
    const seqLangues = this.seqLanguesInput?.nativeElement.value;
    const seqtimer = this.seqtimerInput?.nativeElement.value;
    const seqscore = this.seqscoreInput?.nativeElement.value;

    const updatedSequence = {
      sequenceId: sequence.sequenceId,
      languages: seqLangues,
      audioMP3: 'string',
      videoMP4: 'string',
      score: parseInt(seqscore, 10),
      timer: seqtimer,
      scoreGot: 0,
      completed:false
    };
    const sequenceJson = JSON.stringify(updatedSequence);//pour creer le json
    console.log(updatedSequence);
    let email=sessionStorage.getItem("email");
    // Appeler le service pour ajouter les séquences
    if(this.uploadedFiles==null)
    {
      this.sequenceService.updateSequenceWithFile(sequence.sequenceId,this.uploadedFiles!,sequenceJson).subscribe({
        next: (data) => {
          console.log(data);
          window.location.reload();
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Product is updated' });
      },
        error: (err) => {
          console.log(err);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error });
        }
      });
    }
    else
    {
      this.sequenceService.updateSequenceWithoutFile(sequence.sequenceId,sequenceJson).subscribe({
        next: (data) => {
          console.log(data);
          window.location.reload();
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Product is updated' });
      },
        error: (err) => {
          console.log(err);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error });
        }
      });
    }

  }

  onRowEditCancel(sequence: Sequence, index: number) {
      this.sequences[index] = this.clonedSequences[sequence.sequenceId as unknown as string];
      delete this.clonedSequences[sequence.sequenceId as unknown as string];
  }

  onRowEditDelete(sequence: Sequence, index: number) {
    this.sequenceService.deleteSequence(sequence.sequenceId).subscribe({
      next: (data) => {
        console.log(data);
        window.location.reload();
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Product is updated' });
    },
      error: (err) => {
        window.location.reload();
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error });
      }
    });
  }
}
