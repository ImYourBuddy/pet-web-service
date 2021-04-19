import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../models/user.model';
import {Pet} from '../../models/pet.model';

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

  updateProfile(user: User): Observable<User> {
    return this.http.put(API_URL + '/' + user.id, user);
  }

  getUser(id: bigint): Observable<User> {
    return this.http.get(API_URL + '/' + id);
  }

  getPets(id: bigint): Observable<Pet[]> {
    return this.http.get<Pet[]>(API_URL + '/' + id + '/pets');
  }

  addPet(pet: Pet): Observable<Pet> {
    return this.http.post(API_URL + '/' + pet.owner + '/pets', pet, httpOptions);
  }

  deletePet(owner: bigint, pet: bigint): Observable<Pet> {
    return this.http.delete(API_URL + '/' + owner + '/pets/' + pet);
  }

  editPet(pet: Pet): Observable<Pet> {
    return this.http.put(API_URL + '/' + pet.owner + '/pets/' + pet.id, pet, httpOptions);

  }

  getPet(owner: bigint, petId: bigint): Observable<Pet> {
    return this.http.get(API_URL + '/' + owner + '/pets/' + petId);
  }
}
