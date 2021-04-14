import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';

const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private http: HttpClient) {
  }

  findMessages(senderId, recipientId) {
    return this.http.get('http://localhost:8080/rest/message/' + senderId + '/' + recipientId);
  }

  findChats(userId) {
    return this.http.get('http://localhost:8080/rest/chat/' + userId);
  }

  haveNewMessages(userId) {
    return this.http.get('http://localhost:8080/rest/message/new/' + userId);
  }

  haveNewMessagesInChat(userId, recipientId) {
    return this.http.get('http://localhost:8080/rest/message/new/' + userId + '/' + recipientId);
  }

  markAsDelivered(userId, recipientId) {
    return this.http.patch('http://localhost:8080/rest/message/delivered/' + userId + '/' + recipientId, {});
  }
}
