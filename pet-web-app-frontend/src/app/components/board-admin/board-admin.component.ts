import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {AdminService} from '../../services/admin-service/admin.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  users: any;
  testInit: string;

  isSuccessful = false;
  errorMessage = '';

  constructor(private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.getAllUsers();
    this.testInit = 'works';
  }

  getAllUsers() {
    this.adminService.getUsers()
      .subscribe(
        data => {
          this.users = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  addModer(id: bigint) {
    this.adminService.addModer(id).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

}
