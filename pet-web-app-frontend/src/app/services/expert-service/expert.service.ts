import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const API_URL = 'http://localhost:8080/rest/pet-expert';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ExpertService {

  constructor(private http: HttpClient) {
  }

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

  getByUserId(userId: bigint) {
    return this.http.get(API_URL + '/' + userId);
  }

  edit(userId: bigint, qualification: string, onlineHelp: boolean) {
    return this.http.put(API_URL, {
      qualification,
      onlineHelp,
      userId
    }, httpOptions);
  }


  findMessages(senderId, recipientId) {
    return this.http.get('http://localhost:8080/rest/message/' + senderId + '/' + recipientId);
  }

}
