import { Component } from '@angular/core';
import {TokenStorageService} from './services/token-storage/token-storage.service';
import {ChatComponent} from './components/chat/chat.component';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import {NotifierService} from 'angular-notifier';
import {ChatService} from './services/chat-service/chat.service';

const API_URL = 'http://localhost:8080';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private tokenStorageService: TokenStorageService, notifier: NotifierService,
              private chatService: ChatService) {
    this.notifier = notifier;
  }
  private notifier: NotifierService;
  public stompClient;
  title = 'pet-web-app-frontend';
  private roles: any[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  showExpertBoard = false;
  haveNewMessages;
  username?: string;
  public newMessages: any[] = [];

  static reload() {
    window.location.reload();
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMINISTRATOR');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');
      this.showExpertBoard = this.roles.includes('ROLE_EXPERT');

      this.chatService.haveNewMessages(user.id)
        .subscribe(
          data => {
            this.haveNewMessages = data;
            if (this.haveNewMessages == true) {
              this.notifier.notify('success', 'You have new messages. Check your chats.');
            }
            console.log(data);
          },
          error => {
            console.log(error);
          });

      this.username = user.username;

      this.initializeWebSocketConnection(this.tokenStorageService.getUser().id);
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
  }

  initializeWebSocketConnection(userId) {
    const ws = new SockJS(API_URL + '/socket');
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/chat/' + userId, (message) => {
        if (message.body) {
          const parse = JSON.parse(message.body);
          that.notifier.notify('success', 'You have a new message from ' + parse.senderName);
        }
      });
    });
  }
}
