import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const API_URL = 'http://localhost:8080/rest/admin';
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) {
  }

  getUsers() {
    return this.http.get(API_URL + '/all');
  }

  addModer(id: bigint) {
    return this.http.post(API_URL + '/add-moder', {
      id
    }, httpOptions);
  }

  getExpertRequest() {
    return this.http.get(API_URL + '/experts');
  }

  confirmExpert(userId: bigint, expertId: bigint) {
    return this.http.patch(API_URL + '/confirm-expert', {
      userId,
      expertId
    }, httpOptions);
  }
}
