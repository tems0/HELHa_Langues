import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Qcmquestion } from '../model/qcmquestion';
import { Sequence } from '../model/sequence';
import { SequenceService } from '../Service/sequence.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-qcm',
  templateUrl: './qcm.component.html',
  styleUrls: ['./qcm.component.css']
})
export class QcmComponent implements OnInit {
  questions: Qcmquestion[] = [];
  remainingTime: number = 0; // Timer in seconds
  timerInterval: any;
  sequence!:Sequence;
  scoreObtenu:number=0;
  score:number=0;
  GotScore:boolean=false;

  constructor(private route: ActivatedRoute,private messageService: MessageService,private sequenceService: SequenceService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['questions']) {
        this.questions = JSON.parse(params['questions']);
        this.remainingTime = JSON.parse(params['time']);
        this.sequence = JSON.parse(params['sequence']);
      }
    });
    this.score=this.sequence.score;
    this.remainingTime*=60// pour avoir en minute
    const storedStartTime = sessionStorage.getItem('qcmStartTime');
    if (storedStartTime) {
      const currentTime = Math.floor(Date.now() / 1000);
      const elapsedTime = currentTime - parseInt(storedStartTime, 10);
      this.remainingTime -= elapsedTime;
      if (this.remainingTime <= 0) {
        this.finishQuiz();
      } else {
        this.startTimer();
      }
    } else {
      this.startTimer();
    }


  }

  startTimer() {
    const startTime = Math.floor(Date.now() / 1000);
    sessionStorage.setItem('qcmStartTime', startTime.toString());

    this.timerInterval = setInterval(() => {
      if (this.remainingTime > 0) {
        this.remainingTime--;
      } else {
        this.finishQuiz();
      }
    }, 1000);
  }

  getFormattedTime(): string {
    const minutes = Math.floor(this.remainingTime / 60);
    const seconds = this.remainingTime % 60;
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  }

  ValidQcm()
  {
    let point=0;
    for (let i = 0; i < this.questions.length; i++) {
      let cpt=0;
      for (let j = 0; j < this.questions[i].responses.length; j++) {
        const isChecked = (document.getElementById('checkbox'+i+''+j) as HTMLBodyElement);
        const inputElement = (isChecked.querySelector('input[type="checkbox"]') as HTMLInputElement).checked;
        if(inputElement)
        {

          if(!this.questions[i].responses[j].responseCorrect==inputElement)
          {
            console.log("mauvais");
            cpt--;
          }
          else{
            cpt++;
            console.log("bon");
          }
        }
      }
      if(cpt==this.questions[i].responses.filter(response => response.responseCorrect === true).length)
      point++;
    }
    if(point <0)
      point=0;

    this.sequence.scoreGot=point;
    const sequenceJson = JSON.stringify(this.sequence);
    this.scoreObtenu=point;
    console.log(point);
    this.sequenceService.addSequencesWithoutFile(sessionStorage.getItem("email")!,sequenceJson).subscribe({
      next: (data) => {
        console.log(data);
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Product is updated' });
        this.GotScore=true;
      },
      error: (err) => {
        console.log(err);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid Price' });
      }
    });
  }

  Retour()
  {
    location.replace("/list-sequences");
  }

  finishQuiz() {
    this.ValidQcm();
    clearInterval(this.timerInterval);
    sessionStorage.removeItem('qcmStartTime');
  }
}
