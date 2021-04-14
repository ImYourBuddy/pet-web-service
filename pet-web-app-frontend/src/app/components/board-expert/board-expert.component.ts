import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-board-expert',
  templateUrl: './board-expert.component.html',
  styleUrls: ['./board-expert.component.css']
})
export class BoardExpertComponent implements OnInit {
  posts: any;
  userId: bigint;
  currentUser: any;
  pageOfItems: Array<any>;

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
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
          window.location.reload();
        });
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
    this.retrieveTutorials();
  }

  retrieveTutorials() {
    const author = this.token.getUser().id;
    this.postService.getByAuthor(author)
      .subscribe(
        data => {
          this.posts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  deletePost(id: bigint) {
    this.postService.deletePost(id).subscribe
    (
      data => {
        console.log(data);
        this.isSuccessful = true;
        window.location.reload();
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
        window.location.reload();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onChangePage(pageOfItems: Array<any>) {
    this.pageOfItems = pageOfItems;
  }

}
