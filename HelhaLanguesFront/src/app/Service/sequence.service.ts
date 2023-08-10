import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sequence } from '../model/sequence';
import { Qcmquestion } from '../model/qcmquestion';
import { Reponse } from '../model/reponse';

@Injectable({
  providedIn: 'root'
})
export class SequenceService {

  constructor(private http : HttpClient) { }

  GetAllSequences(): Observable<Sequence[]> {
    const url = 'http://localhost:8080/api/sequences/AllSequences'; // Mettez l'URL correcte de l'API ici
    const headers = new HttpHeaders({ 'content-type': 'application/json' });
    return this.http.get<Sequence[]>(url, { headers: headers });
  }

  addSequences(email: string, file: File, sequenceJson: string): Observable<any> {
    const url = `http://localhost:8080/api/users/${email}/sequences`;
    const headers = new HttpHeaders(); // Pas besoin de spécifier content-type, HttpClient s'en occupe automatiquement

    // Créez un objet FormData pour envoyer le fichier et la chaîne JSON en tant que corps de la requête
    const formData = new FormData();
    formData.append('file', file);
    formData.append('seq', sequenceJson);

    return this.http.post<any>(url, formData, { headers: headers });
  }

  addSequencesWithoutFile(email: string, sequenceJson: string): Observable<any> {
    const url = `http://localhost:8080/api/users/${email}/sequencesWithoutFile`;
    const headers = new HttpHeaders(); // Pas besoin de spécifier content-type, HttpClient s'en occupe automatiquement

    // Créez un objet FormData pour envoyer le fichier et la chaîne JSON en tant que corps de la requête
    const formData = new FormData();
    formData.append('seq', sequenceJson);
    return this.http.post<any>(url, formData, { headers: headers });
  }

  addQuestion(sequenceId: number, qcmQuestion: string,responses : Reponse[]): Observable<Sequence> {
    const url = `http://localhost:8080/api/sequences/${sequenceId}/addQuestionWithResponses`;

    const qcmQuestionData: any = {
      questionNom: qcmQuestion,
      responses: responses.map(response => {
        return {
          response: response.response,
          responseCorrect: response.responseCorrect
        };
      })
    };

    return this.http.post<Sequence>(url, qcmQuestionData);
  }

  updateSequenceWithFile(id: number, file: File, sequenceJson: string): Observable<any> {
    const url = `http://localhost:8080/api/sequences/WithFile/${id}`;
    const headers = new HttpHeaders();

    // Créez un objet FormData pour envoyer le fichier et la chaîne JSON en tant que corps de la requête
    const formData = new FormData();
    formData.append('file', file);
    formData.append('seq', sequenceJson);
    console.log(formData);
    return this.http.put<any>(url, formData, { headers: headers });
  }

  updateSequenceWithoutFile(id: number, sequenceJson: string): Observable<any> {
    const url = `http://localhost:8080/api/sequences/${id}`;
    const headers = new HttpHeaders();

    // Créez un objet FormData pour envoyer le fichier et la chaîne JSON en tant que corps de la requête
    const formData = new FormData();
    formData.append('seq', sequenceJson);
    console.log(formData);
    return this.http.put<any>(url, formData, { headers: headers });
  }

  deleteSequence(id: number): Observable<any> {
    const url = `http://localhost:8080/api/sequences/${id}`;
    return this.http.delete<any>(url);
  }
  getSequence(id: number): Observable<Sequence> {
    const url = `${"http://localhost:8080/api/sequences/Sequence"}?id=${id}`;
    return this.http.get<Sequence>(url);
  }

  updateResponse(id: number, response: any): Observable<any> {
    const url = `http://localhost:8080/api/responses/${id}/`;
    const headers = new HttpHeaders();

    return this.http.put<any>(url, response, { headers: headers });
  }

  updateQuestion(id: number, question: any): Observable<any> {
    const url = `http://localhost:8080/api/qcmQuestions/${id}/`;
    const headers = new HttpHeaders();

    return this.http.put<any>(url, question, { headers: headers });
  }

  deleteResponse(id: number): Observable<any> {
    const url = `http://localhost:8080/api/responses/${id}/`;
    const headers = new HttpHeaders();

    return this.http.delete<any>(url);
  }

  deleteQuestion(id: number): Observable<any> {
    const url = `http://localhost:8080/api/qcmQuestions/${id}/`;
    const headers = new HttpHeaders();

    return this.http.delete<any>(url);
  }

  addNewResponse(id: number, response: any): Observable<any> {
    const url = `http://localhost:8080/api/qcmQuestions/addResponse/${id}/`;
    const headers = new HttpHeaders();

    return this.http.put<any>(url, response, { headers: headers });
  }
}
