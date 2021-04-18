import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Expert} from '../../models/expert.model';

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

  requestExpert(expert: Expert): Observable<Expert> {
    return this.http.post(API_URL, expert, httpOptions);
  }

  getAllExperts(): Observable<Expert[]> {
    return this.http.get<Expert[]>(API_URL);
  }

  getByUserId(userId: bigint): Observable<Expert> {
    return this.http.get(API_URL + '/' + userId);
  }

  checkByUserId(userId: bigint): Observable<any> {
    return this.http.get(API_URL + '/' + userId + '/check');
  }

  edit(expert: Expert): Observable<Expert> {
    return this.http.put(API_URL, expert, httpOptions);
  }

}
