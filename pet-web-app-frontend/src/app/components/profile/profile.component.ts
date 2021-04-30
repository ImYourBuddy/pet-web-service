import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {ExpertService} from '../../services/expert-service/expert.service';
import {User} from '../../models/user.model';
import {Pet} from '../../models/pet.model';
import {Expert} from '../../models/expert.model';
import {AuthService} from '../../services/auth-service/auth.service';

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

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router,
              private expertService: ExpertService, private authService: AuthService) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:no-unused-expression
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      const id = this.token.getUser().id;
      this.roles = this.token.getUser().roles;
      this.userService.getUser(id)
        .subscribe(
          data => {
            this.currentUser = data;
            console.log(data);
          },
          error => {
            if (error.status == 401) {
              this.authService.logoutUser().subscribe();
              this.token.signOut();
              window.location.reload();
              this.router.navigate(['/login']);
            }
            console.log(error);
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
          if (error.status == 401) {
            this.token.signOut();
            window.location.reload();
            this.router.navigate(['/login']);
          }
          console.log(error);
        });
  }


}
