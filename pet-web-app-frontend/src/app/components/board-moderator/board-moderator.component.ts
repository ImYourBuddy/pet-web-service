import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {PostService} from '../../services/post-service/post.service';
import {AdminService} from '../../services/admin-service/admin.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ModerService} from '../../services/moder-service/moder.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './board-moderator.component.html',
  styleUrls: ['./board-moderator.component.css']
})
export class BoardModeratorComponent implements OnInit {
  posts: any;
  experts: any;
  users: any;
  userId: bigint;
  currentUser: any;

  banDescription: any;
  isSuccessful = false;
  errorMessage = '';
  hidePosts = true;
  hideExpertRequest = true;
  hideUsers = true;
  hideBanInput = true;

  constructor(private postService: PostService, private adminService: AdminService,
              private moderService: ModerService, private token: TokenStorageService,
              private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
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
          this.router.navigate(['/login']);
        });
    this.postService.getAllForModer()
      .subscribe(
        data => {
          this.posts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
    this.moderService.getExpertRequest()
      .subscribe(
        data => {
          this.experts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
    this.getAllUsers();
  }

  removeFromPublicAccess(id: bigint) {
    this.postService.deletePost(id).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  restorePost(id: bigint) {
    this.postService.restore(id).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  deletePost(id: bigint) {
    this.postService.deletePostByModer(id).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  confirmExpert(userId: bigint, expertId: bigint) {
    this.moderService.confirmExpert(userId, expertId).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  rejectExpert(expertId: bigint) {
    this.moderService.rejectExpert(expertId).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  deleteExpert(userId: bigint) {
    this.moderService.deleteExpert(userId).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
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

  banUser(userId) {
    this.moderService.banUser(userId, this.banDescription).subscribe(data => {
      this.reloadPage();
    });
  }

  unbanUser(userId) {
    this.moderService.unbanUser(userId).subscribe(data => {
      this.reloadPage();
    });

  }

  reloadPage(): void {
    window.location.reload();
  }
}
