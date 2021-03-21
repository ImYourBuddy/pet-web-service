import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const API_URL = 'http://localhost:8080/rest/pet-expert';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ExpertService {

  constructor(private http: HttpClient) { }

  requestExpert(qualification: string, onlineHelp: boolean, userId: bigint) {
    return this.http.post(API_URL + '/add', {
      qualification,
      onlineHelp,
      userId
    }, httpOptions);
  }

}
