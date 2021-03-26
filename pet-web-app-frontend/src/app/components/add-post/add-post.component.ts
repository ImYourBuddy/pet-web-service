import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {

  post: any = {
    title: null,
    description: null,
    text: null,
    author: null
  };

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.post.author = this.token.getUser().id;
    const {title, description, text, author} = this.post;

    this.postService.add(title, description, text, author).subscribe(
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
