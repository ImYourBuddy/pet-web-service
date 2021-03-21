import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

const API_URL = 'http://localhost:8080/rest/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  updateProfile(id: bigint, firstName: string, lastName: string) {
    return this.http.patch(API_URL + '/edit/' + id, {
      firstName,
      lastName
    } );
  }

  getUser(id: bigint) {
    return this.http.get(API_URL + '/' + id);
  }
}
