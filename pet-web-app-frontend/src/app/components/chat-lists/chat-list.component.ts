import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent {
  chats;
  userId;
  user;
  showNotification: any;
  error: boolean;

  constructor(private chatService: ChatService, private token: TokenStorageService, private userService: UserService, private router: Router) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    this.userId = this.token.getUser().id;
    this.userService.getUser(this.userId)
      .subscribe(
        data => {
          this.user = data;
          console.log(data);
        },
        error => {
          console.log(error);
          this.token.signOut();
          window.location.reload();
        });
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
    this.getChats();
  }

  getChats() {
    this.chatService.findChats(this.userId)
      .subscribe(
        data => {
          this.chats = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }


  haveNewMessages(recipient): boolean {
    this.chatService.haveNewMessagesInChat(this.userId, recipient)
      .subscribe(
        data => {
          this.showNotification = data;
        },
        error => {
          console.log(error);
        });
    return this.showNotification;
  }
}
