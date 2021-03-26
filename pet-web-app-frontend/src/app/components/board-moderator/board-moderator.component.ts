import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {PostService} from '../../services/post-service/post.service';
import {AdminService} from '../../services/admin-service/admin.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ModerService} from '../../services/moder-service/moder.service';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './board-moderator.component.html',
  styleUrls: ['./board-moderator.component.css']
})
export class BoardModeratorComponent implements OnInit {
  posts: any;
  experts: any;

  isSuccessful = false;
  errorMessage = '';
  hidePosts = true;
  hideExpertRequest = true;

  constructor(private postService: PostService, private adminService: AdminService,
              private moderService: ModerService, private token: TokenStorageService) {
  }

  ngOnInit(): void {
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
  }

  removeFromPublicAccess(id: bigint) {
    this.postService.deletePost(id).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
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
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

}
