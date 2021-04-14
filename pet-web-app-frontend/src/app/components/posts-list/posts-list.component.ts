import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {UserService} from '../../services/user/user.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.css']
})
export class PostsListComponent implements OnInit {

  posts: any;
  userId: bigint;
  currentUser: any;
  pageOfItems: Array<any>;

  constructor(private postService: PostService, private userService: UserService, private token: TokenStorageService, private router: Router) { }

  ngOnInit() {
    this.retrieveTutorials();
  }

  retrieveTutorials() {
    this.postService.getAll()
      .subscribe(
        data => {
          this.posts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }
  onChangePage(pageOfItems: Array<any>) {
    this.pageOfItems = pageOfItems;
  }
}
