import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {NotifierService} from 'angular-notifier';
import {Post} from '../../models/post.model';

@Component({
  selector: 'app-read-post',
  templateUrl: './read-post.component.html',
  styleUrls: ['./read-post.component.css']
})
export class ReadPostComponent implements OnInit {

  currentPost: Post;
  userId: bigint;
  userMark = null;
  postImage: any;
  isFailed = false;
  errorMessage = '';

  constructor(private postService: PostService, private route: ActivatedRoute, private token: TokenStorageService,
              private notifier: NotifierService) {
  }

  ngOnInit(): void {
    const roles = this.token.getUser().roles;
    if (roles != null &&
      (roles.includes('ROLE_ADMINISTRATOR') || roles.includes('ROLE_MODERATOR') || roles.includes('ROLE_EXPERT'))) {
      this.getDeletedPost(this.route.snapshot.paramMap.get('id'));
    } else {
      this.getPost(this.route.snapshot.paramMap.get('id'));
    }
    this.getPostImage(this.route.snapshot.paramMap.get('id'));
    this.userId = this.token.getUser().id;
    this.checkMark(this.route.snapshot.paramMap.get('id'), this.userId);
  }

  getPost(id) {
    this.postService.getById(id)
      .subscribe(
        data => {
          this.currentPost = data;
          console.log(data);
        },
        error => {
          this.errorMessage = error.error.message;
          this.isFailed = true;
          console.log(error.error);
        });
  }

  getDeletedPost(id) {
    this.postService.getDeletedPostById(id)
      .subscribe(
        data => {
          this.currentPost = data;
          console.log(data);
        },
        error => {
          this.errorMessage = error.error.message;
          this.isFailed = true;
          console.log(error.error);
        });
  }

  getPostImage(id) {
    this.postService.getPostImage(id)
      .subscribe(
        data => {
          this.postImage = data;
          console.log(this.postImage.image);
        },
        error => {
          this.errorMessage = error.error.message;
          this.isFailed = true;
          console.log(error.error);
        });
  }

  checkMark(postId, userId: bigint) {
    this.postService.checkMark(postId, userId)
      .subscribe(
        data => {
          this.userMark = data;
          console.log('check mark');
        },
        error => {
          console.log('error');
        });
  }

  ratePost(postId: bigint, userId: bigint, liked: boolean) {
    this.postService.ratePost(postId, userId, liked).subscribe(
      data => {
        window.location.reload();
      },
      error => {
        console.log('error');
      });
  }

}
