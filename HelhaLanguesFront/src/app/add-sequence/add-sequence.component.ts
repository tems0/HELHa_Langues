import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SequenceService } from '../Service/sequence.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-add-sequence',
  templateUrl: './add-sequence.component.html',
  styleUrls: ['./add-sequence.component.css']
})
export class AddSequenceComponent implements OnInit{

  error:string="";
  isUpload:boolean=false;
  uploadedFiles: File | null = null;
  uploadedFileName!: string ;

  @ViewChild('seqLangues', { static: false }) seqLanguesInput!: ElementRef ;
  @ViewChild('seqtimer', { static: false }) seqtimerInput!: ElementRef ;
  @ViewChild('seqscore', { static: false }) seqscoreInput!: ElementRef ;

  ngOnInit(): void {

  }
  constructor(private sequenceService: SequenceService,private messageService: MessageService) {}

  onSubmitLogin(event:Event) {
    this.error="";
  }

  onUpload(event:any) {
    this.uploadedFiles = event.files[0];
    this.uploadedFileName = event.files[0].name;
    console.log(this.uploadedFileName);
    this.messageService.add({severity: 'info', summary: 'File Uploaded', detail: ''});
    this.isUpload=true;
  }

  addSequence(event:Event)
  {
    event.preventDefault();
    this.error="";
    const seqLangues = this.seqLanguesInput?.nativeElement.value;
    const seqtimer = this.seqtimerInput?.nativeElement.value;
    const seqscore = this.seqscoreInput?.nativeElement.value;

    const updatedSequence = {
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
    if(this.uploadedFiles==null)
    {
      this.error="Veullez importer un fichier (MP3 ou MP4)";
    }
    else{
      // Appeler le service pour ajouter la séquence
      this.sequenceService.addSequences(email!,this.uploadedFiles!,sequenceJson).subscribe({
        next: (data) => {
          console.log(data);
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Product is updated' });
          location.replace("/list-sequences");
        },
        error: (err) => {
          console.log(err);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid Price' });
        }
      });
    }

  }
}
