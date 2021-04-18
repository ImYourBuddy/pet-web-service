import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {Post} from '../../models/post.model';
import {User} from '../../models/user.model';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {

  post: Post = {
    title: '',
    description: '',
    text: '',
    author: null
  };
  currentUser: User;
  userId: bigint;
  newPost: Post;
  selectedFile: File;

  isSuccessful = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService, private userService: UserService, private router: Router) {
  }

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
  }

  onSubmit(): void {
    this.post.author = this.token.getUser().id;
    const file = this.selectedFile;
    this.postService.add(this.post, file).subscribe(
      data => {
        console.log(data);
        this.newPost = data;
        this.isSuccessful = true;
        this.router.navigate(['/posts']);
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

}
