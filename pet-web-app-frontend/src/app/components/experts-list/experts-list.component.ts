import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-experts-list',
  templateUrl: './experts-list.component.html',
  styleUrls: ['./experts-list.component.css']
})
export class ExpertsListComponent {
  experts: any;
  userId: bigint;
  currentUser: any;
  constructor(private expertService: ExpertService, private token: TokenStorageService, private userService: UserService, private router: Router) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    this.userId = this.token.getUser().id;
    this.userService.getUser(this.userId)
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
    this.getExperts();
  }

  getExperts() {
    this.expertService.getAllExperts()
      .subscribe(
        data => {
          this.experts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }
}
