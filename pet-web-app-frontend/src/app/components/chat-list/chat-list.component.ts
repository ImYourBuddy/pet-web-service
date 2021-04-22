import {Component} from '@angular/core';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
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
  error: boolean;

  constructor(private chatService: ChatService, private token: TokenStorageService, private userService: UserService, private router: Router) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.userId = this.token.getUser().id;
      this.getChats();
    }
  }

  getChats() {
    this.chatService.findChats(this.userId)
      .subscribe(
        data => {
          this.chats = data;
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
  }
}
