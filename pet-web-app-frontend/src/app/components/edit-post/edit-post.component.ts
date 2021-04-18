import { Component, OnInit } from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Post} from '../../models/post.model';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {

  currentPost: Post;
  isSuccessful = false;
  errorMessage = '';
  selectedFile: File;
  editedPost: Post;

  constructor(private postService: PostService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.getPost(this.route.snapshot.paramMap.get('id'));
  }

  onSubmit(): void {
    const {title, description, text, author} = this.currentPost;
    const file = this.selectedFile;

    this.postService.editPost(this.currentPost, file).subscribe(
      data => {
        console.log(data);
        this.editedPost = data;
        this.isSuccessful = true;
        this.router.navigate(['posts/' + this.editedPost.id]);
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

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

}
