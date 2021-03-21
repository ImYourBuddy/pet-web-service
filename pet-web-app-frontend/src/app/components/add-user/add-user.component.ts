import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth-service/auth.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  user: any = {
    username: null,
    password: null,
    firstName: null,
    lastName: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  hide = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { username, password, firstName, lastName } = this.user;

    this.authService.registerUser(username, password, firstName, lastName).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
    this.router.navigate(['/login']);
  }
}
