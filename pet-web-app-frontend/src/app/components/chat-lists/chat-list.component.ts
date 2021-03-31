import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent {
  chats;
  userId;
  user;

  constructor(private chatService: ChatService, private token: TokenStorageService, private userService: UserService) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    this.userId = this.token.getUser().id;
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
}
