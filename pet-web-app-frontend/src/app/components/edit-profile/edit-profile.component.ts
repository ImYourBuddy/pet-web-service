import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  user: any;
  isSuccessful = false;
  errorMessage = '';

  constructor(private userService: UserService, private token: TokenStorageService, private router: Router) { }

  ngOnInit(): void {
    const id = this.token.getUser().id;
    this.userService.getUser(id)
      .subscribe(
        data => {
          this.user = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  onSubmit() {
    const {id, firstName, lastName} = this.user;
    this.userService.updateProfile(id, firstName, lastName).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
    this.router.navigate(['/profile']);
  }
}
