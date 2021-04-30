import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';
import {UserService} from '../../services/user-service/user.service';
import {User} from '../../models/user.model';
import {Post} from '../../models/post.model';
import {AuthService} from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-board-expert',
  templateUrl: './board-expert.component.html',
  styleUrls: ['./board-expert.component.css']
})
export class BoardExpertComponent implements OnInit {
  posts: Post[];
  pageOfItems: Post[];

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService, private userService: UserService,
              private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.retrieveTutorials();
    }
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
          if (error.status == 401) {
            this.authService.logoutUser().subscribe();
            this.token.signOut();
            window.location.reload();
            this.router.navigate(['/login']);
          }
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

  restorePost(id: bigint) {
    this.postService.restore(id).subscribe
    (
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

  onChangePage(pageOfItems: Post[]) {
    this.pageOfItems = pageOfItems;
  }

}
