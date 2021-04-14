import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';


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

  getExpertRequest() {
    return this.http.get(API_URL + '/experts');
  }

  confirmExpert(userId: bigint, expertId: bigint) {
    return this.http.patch(API_URL + '/experts', {
      userId,
      expertId
    }, httpOptions);
  }

  rejectExpert(expertId: bigint) {
    return this.http.delete(API_URL + '/experts/' + expertId);
  }
  deleteExpert(userId: bigint) {
    return this.http.delete(API_URL + '/experts/delete/' + userId);
  }

  banUser(userId: bigint, description: string) {
    return this.http.post(API_URL + '/ban',{
      userId,
      description,
      banned: true
    });
  }

  unbanUser(userId: bigint) {
    return this.http.delete(API_URL + '/ban/' + userId, httpOptions);
  }

}
