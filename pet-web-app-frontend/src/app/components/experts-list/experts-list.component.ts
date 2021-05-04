import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {Expert} from '../../models/expert.model';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-experts-list',
  templateUrl: './experts-list.component.html',
  styleUrls: ['./experts-list.component.css']
})
export class ExpertsListComponent {
  experts: Expert[];
  userId: bigint;
  constructor(private expertService: ExpertService, private token: TokenStorageService, private userService: UserService,
              private router: Router, private authService: AuthService) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.userId = this.token.getUser().id;
      this.getExperts();
    }
  }

  getExperts() {
    this.expertService.getAllExperts()
      .subscribe(
        data => {
          this.experts = data;
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
  }
}
