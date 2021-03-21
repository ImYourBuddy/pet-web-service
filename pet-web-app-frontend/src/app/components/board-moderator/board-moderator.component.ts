import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {PostService} from '../../services/post-service/post.service';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './board-moderator.component.html',
  styleUrls: ['./board-moderator.component.css']
})
export class BoardModeratorComponent implements OnInit {
  posts: any;

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService) {
  }

  ngOnInit(): void {
    this.postService.getAllModer()
      .subscribe(
        data => {
          this.posts = data;
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

}
