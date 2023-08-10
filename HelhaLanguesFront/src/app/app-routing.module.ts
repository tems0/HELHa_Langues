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
import { QcmComponent } from './qcm/qcm.component';
import { NotAutorisedComponent } from './not-autorised/not-autorised.component';
import { StudentGuard } from './guard/student.guard';
import { TeacherGuard } from './guard/teacher.guard';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' }, // Redirection vers la page 'home' par défaut
  { path: 'home', component: HomeComponent },
  { path: 'login-token',component: LoginTokenComponent },
  { path: 'list-sequences', component: ListeSequencesComponent },
  { path: 'follow-sequences/:id', canActivate: [StudentGuard],component: FollowSequenceComponent },
  { path: 'add-sequence',canActivate: [TeacherGuard], component: AddSequenceComponent },
  { path: 'add-question/:id',canActivate: [TeacherGuard], component: AddQuestionComponent },
  { path: 'modify-question/:id', canActivate: [TeacherGuard],component: ModifyQuestionComponent },
  { path: 'qcm',canActivate: [StudentGuard], component: QcmComponent },
  { path: 'modify-sequence',canActivate: [TeacherGuard], component: ModifySequenceComponent },
  { path: 'not-autorised', component: NotAutorisedComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
