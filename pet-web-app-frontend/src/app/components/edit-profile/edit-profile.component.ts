import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user-service/user.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  user: User;
  isSuccessful = false;
  errorMessage = '';

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router,
              private authService: AuthService) { }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      const id = this.token.getUser().id;
      this.getUser(id);
    }
  }

  onSubmit() {
    this.userService.updateProfile(this.user).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.router.navigate(['/profile']);
      },
      error => {
        if (error.status == 401) {
          this.authService.logoutUser().subscribe();
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        this.errorMessage = error.error.message;
      }
    );
  }

  getUser(id: bigint) {
    this.userService.getUser(id)
      .subscribe(
        data => {
          this.user = data;
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
