import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private http: HttpClient) {
  }

  findMessages(senderId: bigint, recipientId: bigint): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/rest/chat/messages/' + senderId + '/' + recipientId);
  }

  findChats(userId: bigint): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/rest/chat/' + userId);
  }

  haveNewMessages(userId: bigint): Observable<any> {
    return this.http.get('http://localhost:8080/rest/chat/messages/new/' + userId);
  }

  haveNewMessagesInChat(senderId: bigint, recipientId: bigint): Observable<any> {
    return this.http.get('http://localhost:8080/rest/chat/messages/new/' + senderId + '/' + recipientId);
  }

  markAsDelivered(senderId: bigint, recipientId: bigint) {
    return this.http.patch('http://localhost:8080/rest/chat/messages/delivered/' + senderId + '/' + recipientId, {});
  }
}
