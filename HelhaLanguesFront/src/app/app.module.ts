import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ListeSequencesComponent } from './liste-sequences/liste-sequences.component';
import { AddSequenceComponent } from './add-sequence/add-sequence.component';
import { ModifySequenceComponent } from './modify-sequence/modify-sequence.component';
import { FollowSequenceComponent } from './follow-sequence/follow-sequence.component';

import { HomeComponent } from './home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
//prime ng
import { ButtonModule } from 'primeng/button';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CheckboxModule } from 'primeng/checkbox';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { TriStateCheckboxModule } from 'primeng/tristatecheckbox';
import { LoginTokenComponent } from './login-token/login-token.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TableModule } from 'primeng/table';
import { MessagesModule} from "primeng/messages";
import { MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { TreeTableModule } from 'primeng/treetable';
import { AddQuestionComponent } from './add-question/add-question.component';
import { ModifyQuestionComponent } from './modify-question/modify-question.component';
import { QcmComponent } from './qcm/qcm.component';
import { NotAutorisedComponent } from './not-autorised/not-autorised.component';
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginTokenComponent,
    ListeSequencesComponent,
    AddSequenceComponent,
    ModifySequenceComponent,
    HomeComponent,
    FollowSequenceComponent,
    AddQuestionComponent,
    ModifyQuestionComponent,
    QcmComponent,
    NotAutorisedComponent
  ],
  imports: [
    BrowserModule,
    ButtonModule,
    HttpClientModule,
    InputTextModule,
    BrowserAnimationsModule,
    DropdownModule,
    AppRoutingModule,
    CheckboxModule,
    FileUploadModule,
    AvatarModule,
    TriStateCheckboxModule,
    AvatarGroupModule,
    TableModule,
    ToastModule,
    TreeTableModule,
    ProgressSpinnerModule,
    MessagesModule,
    NgbModule
  ],
  providers: [
    MessageService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
