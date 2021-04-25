import {Component, ElementRef, ViewChild} from '@angular/core';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user-service/user.service';

import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import {AppComponent} from '../../app.component';
import {NotifierService} from 'angular-notifier';
import {User} from '../../models/user.model';


const API_URL = 'http://localhost:8080';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  private notifier: NotifierService;

  constructor(private chatService: ChatService, private token: TokenStorageService,
              private route: ActivatedRoute, private userService: UserService, notifier: NotifierService,
              private app: AppComponent, private router: Router) {
    this.notifier = notifier;
  }

  @ViewChild('scrollMe') private myScrollContainer: ElementRef;

  input: '';
  to;
  userId: bigint;
  recipient: User;
  messages: any[];
  inChat: boolean;
  public newMessages: any[] = [];
  public sentMessages: any[] = [];
  private stompClient;
  sender: User;
  showErrorMessage = false;
  errorMessage: '';

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.inChat = true;
      console.log('In chat: ' + this.inChat);
      this.app.stompClient.disconnect();
      this.userId = this.token.getUser().id;
      this.to = this.route.snapshot.paramMap.get('id');
      this.initializeWebSocketConnection(this.userId);
      this.findMessages();
      this.userService.getUser(this.userId)
        .subscribe(
          data => {
            this.sender = data;
            console.log(data);
          },
          error => {
            if (error.status == 401) {
              this.token.signOut();
              window.location.reload();
              this.router.navigate(['/login']);
            }
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
      this.haveNewMessages();
    }
  }

  ngAfterViewInit() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) {
    }
  }

  ngOnDestroy(): void {
    console.log('Try to disconnect...');
    this.stompClient.disconnect();
    this.app.initializeWebSocketConnection(this.userId);
  }

  // ngAfterViewChecked() {
  //   this.container = document.getElementById('chat');
  //   this.container.scrollTop = this.container.scrollHeight;
  // }

  initializeWebSocketConnection(userId) {
    const ws = new SockJS(API_URL + '/socket');
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/chat/' + userId, (message) => {
        if (message.body) {
          const parse = JSON.parse(message.body);
          if (that.to == parse.sender) {
            that.newMessages.push(parse);
            that.chatService.markAsDelivered(that.userId, that.to)
              .subscribe();
            setTimeout(() => {
              that.scrollToBottom();
            }, 0);
          } else {
            that.notifier.notify('success', 'You have a new message from ' + parse.senderName);
          }
        }
      });
    });
  }

  sendMessage() {
    if (this.input) {
      const newMessage = {
        sender: this.userId,
        text: this.input,
        timestamp: new Date()
      };
      // AppComponent.stompClient.send('/rest/send/message/' + this.to, {}, JSON.stringify(newMessage));
      this.stompClient.send('/rest/send/message/' + this.to, {}, JSON.stringify(newMessage));
      // this.sentMessages.push(newMessage);
      this.newMessages.push(newMessage);
      this.input = '';
    }
    setTimeout(() => {
      this.scrollToBottom();
    }, 0);
  }

  findMessages() {
    this.chatService.findMessages(this.userId, this.to)
      .subscribe(
        data => {
          this.messages = data;
          console.log(data);
        },
        error => {
          this.showErrorMessage = true;
          this.errorMessage = error.error;
        });
  }

  public setMessage(message) {
    this.newMessages.push(message);
  }


  haveNewMessages() {
    this.chatService.haveNewMessagesInChat(this.userId, this.to)
      .subscribe(
        data => {
          if (data == true) {
            this.chatService.markAsDelivered(this.userId, this.to)
              .subscribe();
          }
          console.log(data);
        },
        error => {
          this.showErrorMessage = true;
          this.errorMessage = error.error;
        });
  }

}
