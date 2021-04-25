import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../models/user.model';

const AUTH_API = 'http://localhost:8080/rest/auth';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};



@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  loginUser(user: User): Observable<any> {
    return this.http.post(AUTH_API + '/signin', user, httpOptions);
  }

  registerUser(user: User): Observable<any> {
    return this.http.post(AUTH_API + '/signup', user, httpOptions);
  }

  logoutUser() {
    return this.http.post(AUTH_API + '/logout', {});
  }
}
