import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';
import {Router} from '@angular/router';
import {ExpertService} from '../../services/expert-service/expert.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;
  expertInfo: any;
  roles: string[] = [];
  hideBecomeExpert: boolean;
  pets: any;

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router, private expertService: ExpertService) { }

  ngOnInit(): void {
    // tslint:disable-next-line:no-unused-expression
    const id = this.token.getUser().id;
    this.roles = this.token.getUser().roles;
    this.userService.getUser(id)
      .subscribe(
        data => {
          this.currentUser = data;
          console.log(data);
        },
        error => {
          console.log(error);
          this.token.signOut();
          window.location.reload();
        });
    this.expertService.getByUserId(id)
      .subscribe(
        data => {
          this.expertInfo = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
    if (this.roles.includes('ROLE_OWNER')) {
      this.userService.getPets(id)
        .subscribe(
          data => {
            this.pets = data;
            console.log(data);
          },
          error => {
            console.log(error);
          });
    }
  }

  deletePet(petId: bigint) {
    let userId = this.token.getUser().id;
    this.userService.deletePet(userId, petId)
      .subscribe(
        data => {
          console.log(data);
          window.location.reload();
        },
        error => {
          console.log(error);
        });
  }


}
