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
    return this.http.post(API_URL, {
      qualification,
      onlineHelp,
      userId
    }, httpOptions);
  }

  getAllExperts() {
    return this.http.get(API_URL);
  }

}
