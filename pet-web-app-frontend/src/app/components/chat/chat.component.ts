import {Component} from '@angular/core';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../services/user/user.service';

import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';

const API_URL = 'http://localhost:8080';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  // userId;
  // recipientId;
  // input;
  // messages = [];
  //
  // constructor(private chatService: ChatService, private route: ActivatedRoute, private token: TokenStorageService) {
  // }
  //
  // ngOnInit(): void {
  //   this.recipientId = this.route.snapshot.paramMap.get('id');
  //   this.userId = this.token.getUser().id;
  // }
  //
  // sendMessage() {
  //   if (this.input) {
  //     this.chatService.sendMessage(this.input);
  //     this.messages = this.chatService.msg;
  //     this.input = '';
  //   }
  // }

  title = 'Pet-service';
  input;
  to;
  userId;
  sender: any;
  recipient: any;
  messages: any;
  public newMessages: any[] = [];
  public sentMessages: any[] = [];
  public stompClient;
  constructor(private chatService: ChatService, private token: TokenStorageService,
              private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {
    this.userId = this.token.getUser().id;
    this.to = this.route.snapshot.paramMap.get('id');
    this.initializeWebSocketConnection();
    this.findMessages();
    this.userService.getUser(this.userId)
      .subscribe(
        data => {
          this.sender = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
    this.userService.getUser(this.to)
      .subscribe(
        data => {
          this.recipient = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(API_URL + '/socket');
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/chat/' + that.userId, (message) => {
        if (message.body) {
          let parse = JSON.parse(message.body);
          that.newMessages.push(parse);
        }
      });
    });
  }

  sendMessage() {
    if (this.input) {
      let newMessage = {
        sender: this.userId,
        text: this.input,
        timestamp: new Date()};
      this.stompClient.send('/rest/send/message/' + this.to, {}, JSON.stringify(newMessage));
      // this.sentMessages.push(newMessage);
      this.newMessages.push(newMessage);
      this.input = '';
    }
  }

  findMessages() {
    this.chatService.findMessages(this.userId, this.to)
      .subscribe(
        data => {
          this.messages = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  setMessage(message) {

  }


}
