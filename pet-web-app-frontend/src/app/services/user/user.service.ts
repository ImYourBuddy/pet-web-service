import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API_URL = 'http://localhost:8080/rest/user';
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  updateProfile(id: bigint, firstName: string, lastName: string) {
    return this.http.put(API_URL + '/' + id, {
      firstName,
      lastName
    });
  }

  getUser(id: bigint) {
    return this.http.get(API_URL + '/' + id);
  }

  getPets(id: bigint) {
    return this.http.get(API_URL + '/' + id + '/pets');
  }

  addPet(owner: bigint, species: string, name: string, breed: string, gender: string, birthdate) {
    return this.http.post(API_URL + '/add-pet', {
      species,
      name,
      breed,
      gender,
      birthdate,
      owner
    }, httpOptions);
  }

  getUsers() {
    return this.http.get(API_URL + '/summaries');
  }
}
