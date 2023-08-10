import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileContentService {

  constructor(private http : HttpClient) { }


  getMP4File(fileName: string): Observable<HttpResponse<Blob>> {
    const url = `http://localhost:8080/api/mp4/getMP4/${fileName}`;
    const headers = new HttpHeaders({
      'Content-Type': 'video/mp4',
    });

    return this.http.get(url, {
      headers: headers,
      responseType: 'blob',
      observe: 'response',
    });
  }
  getMP3File(fileName: string): Observable<HttpResponse<Blob>> {
    const url = `http://localhost:8080/api/mp3/getMP3/${fileName}`;
    const headers = new HttpHeaders({
      'Content-Type': 'audio/mpeg',
    });

    return this.http.get(url, {
      headers: headers,
      responseType: 'blob',
      observe: 'response',
    });
  }
}
