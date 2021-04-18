import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Expert} from '../../models/expert.model';
import {User} from '../../models/user.model';

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

  getUsers(): Observable<Expert[]> {
    return this.http.get<Expert[]>(API_URL + '/users');
  }

  addModer(id: bigint): Observable<User> {
    return this.http.post(API_URL + '/users/' + id + '/moder', {},
      httpOptions);
  }

  removeModer(id: bigint): Observable<User> {
    return this.http.delete(API_URL + '/users/' + id + '/moder');
  }
}
