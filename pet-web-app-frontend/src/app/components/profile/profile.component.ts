import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;
  roles: string[] = [];

  constructor(private userService: UserService, private token: TokenStorageService) { }

  ngOnInit(): void {
    // tslint:disable-next-line:no-unused-expression
    const id = this.token.getUser().id;
    this.userService.getUser(id)
      .subscribe(
        data => {
          this.currentUser = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

}
