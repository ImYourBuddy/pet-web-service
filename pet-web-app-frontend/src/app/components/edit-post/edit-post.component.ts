import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {

  currentPost = null;
  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.getPost(this.route.snapshot.paramMap.get('id'));
  }

  onSubmit(): void {
    const {title, description, text} = this.currentPost;

    this.postService.editPost(this.currentPost.id, title, description, text).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.router.navigate(['expert']);
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
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

}
