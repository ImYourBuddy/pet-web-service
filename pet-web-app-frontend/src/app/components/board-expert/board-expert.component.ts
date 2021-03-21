import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-board-expert',
  templateUrl: './board-expert.component.html',
  styleUrls: ['./board-expert.component.css']
})
export class BoardExpertComponent implements OnInit {
  posts: any;

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService) { }

  ngOnInit(): void {
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

}
