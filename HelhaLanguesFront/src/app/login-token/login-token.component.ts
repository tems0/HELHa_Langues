import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ConnexionService } from '../Service/connexion.service';

@Component({
  selector: 'app-login-token',
  templateUrl: './login-token.component.html',
  styleUrls: ['./login-token.component.css']
})
export class LoginTokenComponent implements OnInit{

  @ViewChild('token', { static: false }) tokenInput!: ElementRef ;
  ngOnInit(): void {
    console.log(sessionStorage.getItem("salut"));
  }
  email!:string;
  inProgress : boolean=false;
  error:string="";

  constructor(private connexionService: ConnexionService) {}

  onSubmitLogin(event:Event) {
    this.error="";
    this.inProgress=true;
    event.preventDefault();
    const tokenValue = this.tokenInput?.nativeElement.value;
    if(sessionStorage.getItem("emailToken")!=null)
      this.email= sessionStorage.getItem("emailToken")!;
    this.connexionService.Getloged(tokenValue,this.email).subscribe({
      next: (data) => {
        this.inProgress=false;
        console.log(data);
        if(this.email.includes("@student.helha.be")){
          sessionStorage.setItem("role","Student")
        }
        else{
          sessionStorage.setItem("role","Teacher")
        }
        sessionStorage.setItem("email",this.email)
        if(data.message=="connecté")
          location.replace("/home")
    },
    error: (err) => {
      console.log(err);
    }
    });
  }
}
