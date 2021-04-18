import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {UserService} from '../../services/user-service/user.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';
import {Post} from '../../models/post.model';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.css']
})
export class PostsListComponent implements OnInit {

  posts: Post[];
  pageOfItems: Post[];

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
  onChangePage(pageOfItems: Post[]) {
    this.pageOfItems = pageOfItems;
  }
}
