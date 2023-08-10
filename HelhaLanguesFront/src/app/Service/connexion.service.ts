import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ConnexionService {

  constructor(private http : HttpClient) { }

  register(user:User):  Observable<any>
  {
    const headers = { 'content-type': 'application/json'}
    return this.http.post
    ('http://localhost:8080/api/users/addUser/', user,{'headers':headers})
  }

  login(email:string):  Observable<any>
  {
    const url = `http://localhost:8080/api/users/login?email=${encodeURIComponent(email)}`;
    const headers = new HttpHeaders({ 'content-type': 'application/json' });
    return this.http.get(url, { headers: headers });
  }

  Getloged(token: string, email: string): Observable<any> {
    const url = `http://localhost:8080/api/users/login/User?token=${encodeURIComponent(token)}&email=${encodeURIComponent(email)}`;
    const headers = new HttpHeaders({ 'content-type': 'application/json' });
    return this.http.get(url, { headers: headers });
  }
}
