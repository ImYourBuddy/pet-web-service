import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {ExpertService} from '../../services/expert-service/expert.service';
import {User} from '../../models/user.model';
import {Pet} from '../../models/pet.model';
import {Expert} from '../../models/expert.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: User;
  expertInfo: Expert;
  roles: string[] = [];
  hideBecomeExpert = false;
  pets: Pet[];

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router, private expertService: ExpertService) {
  }

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
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
    this.expertService.checkByUserId(id)
      .subscribe(
        data => {
          if (data == true) {
            this.hideBecomeExpert = true;
          }
          console.log(data);
        },
        error => {
          console.log(error);
        });
    if (this.roles.includes('ROLE_EXPERT')) {
      this.expertService.getByUserId(id)
        .subscribe(
          data => {
            this.expertInfo = data;
            console.log(data);
          },
          error => {
            console.log(error);
          });
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
    const userId = this.token.getUser().id;
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
