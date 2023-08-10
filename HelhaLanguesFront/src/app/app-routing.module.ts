import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginTokenComponent } from './login-token/login-token.component';
import { ListeSequencesComponent } from './liste-sequences/liste-sequences.component';
import { AddSequenceComponent } from './add-sequence/add-sequence.component';
import { ModifySequenceComponent } from './modify-sequence/modify-sequence.component';
import { HomeComponent } from './home/home.component';
import { FollowSequenceComponent } from './follow-sequence/follow-sequence.component';
import { AddQuestionComponent } from './add-question/add-question.component';
import { ModifyQuestionComponent } from './modify-question/modify-question.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' }, // Redirection vers la page 'home' par défaut
  { path: 'home', component: HomeComponent },
  { path: 'login-token', component: LoginTokenComponent },
  { path: 'list-sequences', component: ListeSequencesComponent },
  { path: 'follow-sequences/:id', component: FollowSequenceComponent },
  { path: 'add-sequence', component: AddSequenceComponent },
  { path: 'add-question/:id', component: AddQuestionComponent },
  { path: 'modify-question/:id', component: ModifyQuestionComponent },
  { path: 'modify-sequence', component: ModifySequenceComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
