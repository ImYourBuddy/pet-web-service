import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ExpertService} from '../../services/expert-service/expert.service';
import {Router} from '@angular/router';
import {Expert} from '../../models/expert.model';


@Component({
  selector: 'app-edit-expert',
  templateUrl: './edit-expert.component.html',
  styleUrls: ['./edit-expert.component.css']
})
export class EditExpertComponent implements OnInit {

  currentExpert: Expert;
  showErrorMessage = false;
  errorMessage = '';

  constructor(private token: TokenStorageService, private expertService: ExpertService, private router: Router) {
  }

  ngOnInit() {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    } else {
      this.getExpert();
    }
  }

  onSubmit() {
    this.expertService.edit(this.currentExpert).subscribe(
      data => {
        console.log(data);
        this.router.navigate(['profile']);
      },
      error => {
        if (error.status == 401) {
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        this.errorMessage = error.error.message;
      }
    );
  }


  getExpert() {
    const userId = this.token.getUser().id;
    this.expertService.getByUserId(userId)
      .subscribe(
        data => {
          this.currentExpert = data;
          console.log(data);
        },
        error => {
          if (error.status == 401) {
            this.token.signOut();
            window.location.reload();
            this.router.navigate(['/login']);
          }
          this.showErrorMessage = true;
          this.errorMessage = error.error;
        });
  }
}
