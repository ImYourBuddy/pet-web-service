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
  isSuccessful = false;
  errorMessage = '';

  constructor(private token: TokenStorageService, private expertService: ExpertService, private router: Router) {
  }

  ngOnInit() {
    this.getExpert();
  }

  onSubmit() {
    this.expertService.edit(this.currentExpert).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.router.navigate(['profile']);
      },
      err => {
        this.errorMessage = err.error.message;
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
          console.log(error);
        });
  }
}
