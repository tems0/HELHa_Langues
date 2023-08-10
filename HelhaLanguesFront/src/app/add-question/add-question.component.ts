import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MessageService } from 'primeng/api';
import { SequenceService } from '../Service/sequence.service';
import { ActivatedRoute } from '@angular/router';
import { Reponse } from '../model/reponse';

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit{

  answers: string[] = ['',''];
  sequenceId:number=0;
  responses: Reponse[]=[];

  @ViewChild('questionName', { static: false }) questionNameInput!: ElementRef ;
  @ViewChild('responseName', { static: false }) responseNameInput!: ElementRef ;

  constructor(private sequenceService: SequenceService,private route: ActivatedRoute,
    private messageService: MessageService) {}

  ngOnInit(): void {
    const sequenceIdString = this.route.snapshot.paramMap.get('id');
    this.sequenceId = parseInt(sequenceIdString!, 10);
  }

  addAnswer(): void {
    this.answers.push('');
    console.log(this.responseNameInput.nativeElement.value);
  }



  addQuestion(event:Event)
  {
    event.preventDefault();

    const questionValue = (document.getElementById('question') as HTMLInputElement).value;

    for (let i = 0; i < this.answers.length; i++) {
      const answerValue = (document.getElementById('answer' + i) as HTMLInputElement).value;
      const isChecked = (document.getElementById('checkbox'+i) as HTMLBodyElement);
      const inputElement = (isChecked.querySelector('input[type="checkbox"]') as HTMLInputElement).checked;

      const newResponse: Reponse = {
        responseId: i,
        response: answerValue,
        responseCorrect: inputElement
      };
      this.responses.push(newResponse);
    }
    const hasCorrectResponse = this.responses.some(response => response.responseCorrect === true);

    if (hasCorrectResponse) {
      this.sequenceService.addQuestion(this.sequenceId,questionValue,this.responses).subscribe({
        next: (data) => {
          console.log(data);
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'question ajouté' });
          location.replace("/list-sequences");
        },
        error: (err) => {
          console.log(err);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'erreur' });
        }
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Veillez mettre au moins une bonne reponse' });
    }


  }
}
