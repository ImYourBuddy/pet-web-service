import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post-service/post.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {Post} from '../../models/post.model';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth-service/auth.service';

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

  showError = false;
  errorMessage = '';

  constructor(private postService: PostService, private token: TokenStorageService, private userService: UserService,
              private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.userId = this.token.getUser().id;
      this.userService.getUser(this.userId)
        .subscribe(
          data => {
            this.currentUser = data;
            console.log(data);
          },
          error => {
            console.log(error);
            this.authService.logoutUser().subscribe();
            this.token.signOut();
            window.location.reload();
            this.router.navigate(['/login']);
          });
    }
  }

  onSubmit(): void {
    this.post.author = this.token.getUser().id;
    const file = this.selectedFile;
    this.postService.add(this.post, file).subscribe(
      data => {
        console.log(data);
        this.newPost = data;
        this.router.navigate(['/posts']);
      },
      err => {
        if (err.status == 401) {
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        if (err.status == 403) {
          this.showError = true;
          this.errorMessage = 'Please login as expert';
        } else {
        this.errorMessage = err.error.message;
          }
      }
    );
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

}
