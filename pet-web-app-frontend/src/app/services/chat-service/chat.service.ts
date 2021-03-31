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
  public stompClient;
  public msg: any;
  newMessage: any =  {
    sender: null
  };
  // initializeWebSocketConnection(userId: bigint, recipient: bigint) {
  //   const ws = new SockJS(API_URL + '/socket');
  //   this.stompClient = Stomp.over(ws);
  //   const that = this;
  //   // tslint:disable-next-line:only-arrow-functions
  //   this.stompClient.connect({}, function(frame) {
  //     that.stompClient.subscribe('/chat/' + userId, (message) => {
  //       if (message.body) {
  //         console.log(message.body);
  //         console.log(recipient);
  //         that.findMessages(recipient, userId)
  //           .subscribe(
  //             data => {
  //               that.msg = data;
  //               console.log(data);
  //             },
  //             error => {
  //               console.log(error);
  //             });
  //       }
  //     });
  //   });
  // }
  //
  // sendMessage(sender, text, to) {
  //   this.stompClient.send('/rest/send/message/' + to, {}, JSON.stringify({
  //     sender,
  //     text,
  //   timestamp: new Date()}));
  // }

  findMessages(senderId, recipientId) {
    return this.http.get('http://localhost:8080/rest/message/' + senderId + '/' + recipientId);
  }

  findChats(userId) {
    return this.http.get('http://localhost:8080/rest/chat/' + userId);
  }
  // public stompClient;
  // selectedUser;
  // public msg = [];
  //
  // constructor() {
  //   // this.connectToChat();
  // }

  // connectToChat() {
  //   // console.log('Initialize WebSocket Connection');
  //   // const socket = new SockJS(API_URL + '/chat');
  //   // this.stompClient = Stomp.over(socket);
  //   // this.stompClient.connect({}, this.onConnected(userId));
  //   const ws = new SockJS(API_URL + '/chat');
  //   this.stompClient = Stomp.over(ws);
  //   const that = this;
  //   // tslint:disable-next-line:only-arrow-functions
  //   this.stompClient.connect({}, function(frame) {
  //     that.stompClient.subscribe('/topic', (message) => {
  //       if (message.body) {
  //         that.msg.push(message.body);
  //       }
  //     });
  //   });
  // }

  // sendMessage(message) {
  //   this.stompClient.send('/rest/send/message' , {}, message);
  // }

  // onConnected(userId: bigint) {
  //   console.log('connected');
  //   this.stompClient.subscribe('/topic/messages/' + userId,
  //     function(response) {
  //       console.log('success');
  //       const data = JSON.parse(response.body);
  //       this.messages.push(data.text);
  //     });
  // }

  // sendMessage(sender: bigint, text: string) {
  //   this.stompClient.send('/rest/chat' + this.selectedUser, {}, JSON.stringify({
  //     sender,
  //     text,
  //     date: new Date()
  //   }));
  // }

  // selectUser(selectedUser: bigint) {
  //   this.selectedUser = selectedUser;
  // }
}
