import { Component, ElementRef, OnInit, Output, ViewChild } from '@angular/core';
import { ConnexionService } from '../Service/connexion.service';
import { User } from '../model/user';
import { FormBuilder, NgForm } from '@angular/forms';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {

  @ViewChild('email', { static: false }) emailInput!: ElementRef ;
  @ViewChild('emailLogin', { static: false }) emailLoginInput!: ElementRef ;
  @ViewChild('firstname', { static: false }) FirstNameInput!: ElementRef ;
  @ViewChild('lastname', { static: false }) LastNameInput!: ElementRef;
  @ViewChild('form', { static: false }) form!: NgForm;

  //organisation de la navbar
  isLogin: boolean=false;
  isStudent: boolean=false;

  teacherEmail: string = '';
  studentEmail: string = '';
  showRegisterPopup = false;//afficher les pop up de register
  showLoginPopup = false;//et de login
  value: any  = false;
  isTeacher : boolean =false;//pour la check box de l inscription
  inProgress : boolean=false;
  error:string="";

  user: User = {
    lastName: '',
    email: '',
    firstName: ''
  };
  constructor(private connexionService: ConnexionService) {}


  ngOnInit() {
    if(sessionStorage.getItem("email")!=null&&sessionStorage.getItem("email")!.includes("helha.be")){
      this.isLogin=true;
    }
    if(sessionStorage.getItem("role")!=null&&sessionStorage.getItem("role")!="Student"){
      this.isStudent=false;
    }
    else if(sessionStorage.getItem("role")!=null&&sessionStorage.getItem("role")!="Teacher"){
      this.isStudent=true;
    }
    console.log(this.isLogin);
  }

  check(event: any)
  {
    console.log(event);
    this.value = event.checked;
    this.isTeacher=this.value;
  }

  toggleRegisterPopup() {
    this.error="";
    this.showRegisterPopup = !this.showRegisterPopup;
    this.showLoginPopup = false;
  }

  Logout() {
    sessionStorage.removeItem("email");
    location.replace("http://localhost:4200");
  }

  toggleLoginPopup() {
    this.error="";
    this.showLoginPopup = !this.showLoginPopup;
    this.showRegisterPopup = false;
  }
  onSubmit(event:Event) {
    event.preventDefault();
    console.log(this.form);
    console.log('Le formulaire est valide.');
    const emailValue = this.emailInput?.nativeElement.value;
    const FirstNameInput = this.FirstNameInput?.nativeElement.value;
    const LastNameInput = this.LastNameInput?.nativeElement.value;
    this.user.firstName=FirstNameInput;
    this.user.lastName=LastNameInput;
    this.user.email=emailValue;
    console.log(this.user);
    if(this.isTeacher)
    {
      this.user.email+="@helha.be"
    }
    else{
      this.user.email+="@student.helha.be"
    }
    console.log(this.user);
    this.connexionService.register(this.user).subscribe({
      next: (data) => {
        console.log(data);
    },
    error: (err) => {
      console.log(err);
      this.error=err.message;
      console.log(this.error);
    }
    });
  }

  onSubmitLogin(event:Event) {
    event.preventDefault();
    this.inProgress=true;
    const emailValue = this.emailLoginInput?.nativeElement.value;
    this.connexionService.login(emailValue).subscribe({
      next: (data) => {
        this.inProgress=false;
        console.log(data);
        sessionStorage.setItem("emailToken",emailValue);
        location.replace("/login-token")
    },
    error: (err) => {
      console.log(err.error.message);
      this.inProgress=false;
      this.error=err.error.message;
    }
    });
    this.showRegisterPopup = false;


  }
}
