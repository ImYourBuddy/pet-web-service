import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Expert} from '../../models/expert.model';
import {User} from '../../models/user.model';


const API_URL = 'http://localhost:8080/rest/moder';
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ModerService {
  constructor(private http: HttpClient) {
  }

  getExpertRequest(): Observable<any> {
    return this.http.get(API_URL + '/experts');
  }

  confirmExpert(userId: bigint, expertId: bigint): Observable<Expert> {
    return this.http.patch(API_URL + '/experts/' + userId, {
      expertId
    }, httpOptions);
  }

  rejectExpert(expertId: bigint): Observable<Expert> {
    return this.http.delete(API_URL + '/experts/' + expertId);
  }

  deleteExpert(userId: bigint): Observable<Expert> {
    return this.http.delete(API_URL + '/experts/' + userId + '/delete');
  }

  banUser(userId: bigint, description: string): Observable<User> {
    return this.http.post(API_URL + '/users/' + userId + '/ban', {
      description
    }, httpOptions);
  }

  unbanUser(userId: bigint): Observable<User> {
    return this.http.delete(API_URL + '/users/' + userId + '/ban');
  }

}
