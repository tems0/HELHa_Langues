import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SequenceService } from '../Service/sequence.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService, TreeNode } from 'primeng/api';
import { Sequence } from '../model/sequence';
import { Qcmquestion } from '../model/qcmquestion';
import { Reponse } from '../model/reponse';

@Component({
  selector: 'app-modify-question',
  templateUrl: './modify-question.component.html',
  styleUrls: ['./modify-question.component.css']
})
export class ModifyQuestionComponent implements OnInit{

  sequenceId:number=0;
  sequence!: Sequence;
  treeData: TreeNode[] = [];

  responseId:number=0;
  questionId:number=0;

  editableQuestion:boolean=false;
  editableResponse:boolean=false;
  addnewResponse:boolean=false;

  value: any  = false;

  @ViewChild('reponse', { static: false }) reponseInput!: ElementRef ;
  @ViewChild('reponse2', { static: false }) reponse2Input!: ElementRef ;//pour l'ajout de nouvelle reponses
  @ViewChild('question', { static: false }) questionInput!: ElementRef ;

  constructor(private sequenceService: SequenceService,private route: ActivatedRoute,
    private router: Router,private messageService: MessageService) {}

  ngOnInit(): void {
    const sequenceIdString = this.route.snapshot.paramMap.get('id');
    this.sequenceId = parseInt(sequenceIdString!, 10);
    this.sequenceService.getSequence(this.sequenceId).subscribe({
      next: (data) => {
        this.sequence = data;
        this.sequence.qcmQuestions.sort((a: Qcmquestion, b: Qcmquestion) => {
          const nameA = a.questionNom.toLowerCase(); // Convertir en minuscules pour un tri insensible à la casse
          const nameB = b.questionNom.toLowerCase();
          if (nameA < nameB) {
            return -1;
          }
          if (nameA > nameB) {
            return 1;
          }
          return 0;
        });
        this.buildTreeData();
        console.log(data);
      },
      error: (err) => {
        console.log(err);
        console.log("tg");
      }
    });
  }

  buildTreeData() {
    this.treeData = this.sequence.qcmQuestions.map((question) => ({
      label: question.questionNom,
      id:question.qcmQuestionId,
      responseCorrect: '',
      children: question.responses.map((response) => ({
        label: response.response,
        id:response.responseId,
        responseCorrect: response.responseCorrect ? 'Correct' : 'Incorrect'
      }))
    }));
  }
  check(event: any)
  {
    this.value = event.checked;
  }

  deleteQuestionResponse(i:number,isCorrect:string)
  {
    if(isCorrect=='')
    {
      this.sequenceService.deleteQuestion(i).subscribe({
        next: (data) => {
          console.log(data);
          window.location.reload();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
    else if(isCorrect!=''){
      this.sequenceService.deleteResponse(i).subscribe({
        next: (data) => {
          console.log(data);
          window.location.reload();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }

  updateQuestionResponse(i:number,questionId:number,label:string,isCorrect:string)
  {
    if(isCorrect=='')
    {
      this.editableQuestion = !this.editableQuestion;
      this.editableResponse =false;
      this.questionId=i;
    }
    else if(isCorrect!=''&& !this.editableResponse){
      this.editableResponse = !this.editableResponse;
      this.editableQuestion =false;
      this.responseId=i;
    }
  }

  retour()
  {
    this.editableQuestion=false;
    this.editableResponse=false;
    this.addnewResponse=false;
  }
  newResponse(i:number)
  {
    this.addnewResponse = !this.addnewResponse;
    this.questionId=i;
  }

  onSubmitQuestion(event:Event) {
    event.preventDefault();

    const questionId = this.questionId;
    const updatedQuestion = {
      responseId: questionId,
      questionNom: this.questionInput.nativeElement.value
    };

    this.sequenceService.updateQuestion(questionId, updatedQuestion).subscribe({
      next: (data) => {
        console.log(data);
        window.location.reload();
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  onSubmitReponse(event:Event) {
    event.preventDefault();

    const responseId = this.responseId;
    const updatedResponse = {
      responseId: responseId,
      response: this.reponseInput.nativeElement.value,
      responseCorrect: this.value
    };

    this.sequenceService.updateResponse(responseId, updatedResponse).subscribe({
      next: (data) => {
        console.log(data);
        window.location.reload();
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  onSubmitAddReponse(event:Event) {
    event.preventDefault();

    const questionId = this.questionId;
    const updatedResponse = {
      response: this.reponse2Input.nativeElement.value,
      responseCorrect: this.value
    };

    this.sequenceService.addNewResponse(questionId, updatedResponse).subscribe({
      next: (data) => {
        console.log(data);
        window.location.reload();
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
