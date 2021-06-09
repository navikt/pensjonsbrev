import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, PartialObserver} from "rxjs";
import {Brev} from "./dto/brev";

@Injectable({
  providedIn: 'root'
})
export class BrevService {

  constructor(private httpClient: HttpClient) {

  }

  public getLetter(params: any, selectedLetter: any): Observable<Blob>{
    return this.httpClient.post(`http://localhost:8080/api/letter/${selectedLetter}`, params, {
      responseType: 'blob'
    })
  }

  public getLetterTypes(){
    return this.httpClient.get<Array<string>>(`http://localhost:8080/api/letterTypes`)
  }

  getLetterFields(letterType: string) {
    return this.httpClient.get<Array<string>>(`http://localhost:8080/api/letterFields/${letterType}`)
  }
}

