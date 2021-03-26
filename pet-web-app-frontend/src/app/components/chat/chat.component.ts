import {Component} from '@angular/core';
import {ChatService} from '../../services/chat-service/chat.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';

declare var SockJS;
declare var Stomp;


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  //   currentUser: any = {
  //     id: null
  //   };
  //   activeContact: any = {
  //     id: 2
  // };
  //   stompClient: any;
  //   messages: any[];
  //
  //   constructor(private chatService: ChatService, private token: TokenStorageService, private userService: UserService) {
  //   }
  //
  //   // tslint:disable-next-line:use-lifecycle-interface
  //   ngOnInit(): void {
  //     this.currentUser = this.token.getUser();
  //     this.connect();
  //   }
  //
  //   connect() {
  //     console.log('Initialize WebSocket Connection');
  //     const ws = new SockJS('http://localhost:8080/ws');
  //     this.stompClient = Stomp.over(ws);
  //     this.stompClient.connect({}, this.onConnected, this.onError);
  //   }
  //
  //   onConnected() {
  //     console.log('connected');
  //     console.log(this.currentUser);
  //     this.stompClient.subscribe(
  //       '/user/' + this.currentUser.id + '/queue/messages',
  //       this.onMessageReceived
  //     );
  //   }
  //
  //   onError(err) {
  //     console.log(err);
  //   }
  //
  //   onMessageReceived(msg) {
  //     const notification = JSON.parse(msg.body);
  //     const active = JSON.parse(sessionStorage.getItem('recoil-persist'))
  //       .chatActiveContact;
  //
  //     if (active.id === notification.senderId) {
  //       const message = this.chatService.findChatMessage(notification.id);
  //       const newMessages = JSON.parse(sessionStorage.getItem('recoil-persist'))
  //           .chatMessages;
  //       newMessages.push(message);
  //       this.messages = newMessages;
  //     }
  //     // loadContacts();
  //   }
  //
  //   sendMessage(msg) {
  //     if (msg.trim() !== '') {
  //       const message = {
  //         sender: this.currentUser.id,
  //         recipient: 2,
  //         timestamp: new Date(),
  //         text: msg,
  //       };
  //       this.stompClient.send('/app/chat', {}, JSON.stringify(message));
  //
  //       const newMessages = [...this.messages];
  //       newMessages.push(message);
  //       this.messages = newMessages;
  //     }
  //   }

  // loadContacts() {
  //   const users = this.userService.getUsers();
  //   for (var val of users) {
  //
  //   }
  //   users.flatMap((contact) =>
  //       countNewMessages(contact.id, this.currentUser.id).then((count) => {
  //         contact.newMessages = count;
  //         return contact;
  //       })
  //     )
  //   );
  //
  //   promise.then((promises) =>
  //     Promise.all(promises).then((users) => {
  //       setContacts(users);
  //       if (activeContact === undefined && users.length > 0) {
  //         setActiveContact(users[0]);
  //       }
  //     })
  //   );
  // }

}
