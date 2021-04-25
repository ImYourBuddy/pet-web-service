import {Component, Inject, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth-service/auth.service';
import {Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {AppComponent} from '../../app.component';
import {User} from '../../models/user.model';


@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {
  user: User = {
    username: '',
    password: ''
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  userId: bigint;
  hide = true;
  userNamePattern = '^[A-Za-z][A-Za-z0-9_-]+[A-Za-z0-9]$';


  constructor(private authService: AuthService, private router: Router, private tokenStorage: TokenStorageService) {
  }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
      this.router.navigate(['/posts']);
    }
  }

  onSubmit(): void {
    const {username, password} = this.user;

    this.authService.loginUser(this.user).subscribe(
      data => {
        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.userId = this.tokenStorage.getUser().id;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }

}
