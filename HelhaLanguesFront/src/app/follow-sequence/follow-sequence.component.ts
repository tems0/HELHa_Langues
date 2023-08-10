import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SequenceService } from '../Service/sequence.service';
import { Sequence } from '../model/sequence';
import { ActivatedRoute, Router } from '@angular/router';
import { FileContentService } from '../Service/file-content.service';
import { Qcmquestion } from '../model/qcmquestion';

@Component({
  selector: 'app-follow-sequence',
  templateUrl: './follow-sequence.component.html',
  styleUrls: ['./follow-sequence.component.css']
})
export class FollowSequenceComponent implements OnInit{

  audioUrl!: SafeResourceUrl;
  audioBlobUrl: any;
  videoBlobUrl: any;
  questions:Qcmquestion[]=[];
  time:number=0;
  sequence!:Sequence;
  constructor(private route: ActivatedRoute,private router: Router, private contentFile :FileContentService,
    private sequenceService: SequenceService, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    const sequenceIdString = this.route.snapshot.paramMap.get('id');
    const sequenceId = parseInt(sequenceIdString!, 10);
    this.getContentUrlFromBackend(sequenceId);
  }

  extractFileNameFromPath(filePath: string): string {
    const pathSegments = filePath.split('\\');
    const fileName = pathSegments[pathSegments.length - 1];
    return fileName;
  }

  getContentUrlFromBackend(seq:number) {
    this.sequenceService.getSequence(seq).subscribe({
      next: (data) => {
        this.sequence=data;
        this.questions=data.qcmQuestions
        this.time=parseInt(data.timer, 10);;
        console.log(this.questions);
        if(data.audioMP3!="string")
        {
          console.log(this.extractFileNameFromPath(data.audioMP3));
          this.contentFile.getMP3File(this.extractFileNameFromPath(data.audioMP3)).subscribe({
            next: (data2) => {
              this.audioBlobUrl = URL.createObjectURL(data2.body!);
              this.audioUrl=this.audioBlobUrl;
              console.log(this.audioBlobUrl);
            },
            error: (err) => {
              console.log(err);
              console.log("erreur");
            }
          });;
        }
        else{
          console.log(this.extractFileNameFromPath(data.videoMP4));
          this.contentFile.getMP4File(this.extractFileNameFromPath(data.videoMP4)).subscribe({
            next: (data2) => {
              this.videoBlobUrl = URL.createObjectURL(data2.body!);
              console.log(this.videoBlobUrl);
            },
            error: (err) => {
              console.log(err);
            }
          });;
        }
      },
      error: (err) => {
        console.log(err);
      }
    });

  }


  goToQcm()
  {
    this.router.navigate(['/qcm'], {
      queryParams: {
        questions: JSON.stringify(this.questions),
        time: this.time.toString(),
        sequence: JSON.stringify(this.sequence)
      }
    });
  }
}
