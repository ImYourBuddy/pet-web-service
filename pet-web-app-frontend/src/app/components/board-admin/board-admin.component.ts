import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user-service/user.service';
import {AdminService} from '../../services/admin-service/admin.service';
import {Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {User} from '../../models/user.model';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  users: User[];
  userId: bigint;
  currentUser: User;

  isSuccessful = false;
  errorMessage = '';
  hideUsers = true;

  constructor(private adminService: AdminService, private token: TokenStorageService,
              private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.getAllUsers();
    }
  }

  getAllUsers() {
    this.adminService.getUsers()
      .subscribe(
        data => {
          this.users = data;
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

  addModer(id: bigint) {
    this.adminService.addModer(id).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        window.location.reload();
      },
      error => {
        if (error.status == 401) {
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        this.errorMessage = error.error.message;
      }
    );
  }
  removeModer(id: bigint) {
    this.adminService.removeModer(id).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        window.location.reload();
      },
      error => {
        if (error.status == 401) {
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        this.errorMessage = error.error.message;
      }
    );
  }

}
