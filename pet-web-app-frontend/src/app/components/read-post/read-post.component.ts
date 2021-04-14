import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {NotifierService} from 'angular-notifier';

@Component({
  selector: 'app-read-post',
  templateUrl: './read-post.component.html',
  styleUrls: ['./read-post.component.css']
})
export class ReadPostComponent implements OnInit {

  currentPost = null;
  userId;
  userMark = null;

  constructor(private postService: PostService, private route: ActivatedRoute, private token: TokenStorageService,
              private notifier: NotifierService) {
  }

  ngOnInit(): void {
    this.getPost(this.route.snapshot.paramMap.get('id'));
    this.userId = this.token.getUser().id;
    this.checkMark(this.route.snapshot.paramMap.get('id'), this.userId);
  }

  getPost(id) {
    this.postService.get(id)
      .subscribe(
        data => {
          this.currentPost = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  checkMark(postId, userId) {
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
