import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {ExpertService} from '../../services/expert-service/expert.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-edit-expert',
  templateUrl: './edit-expert.component.html',
  styleUrls: ['./edit-expert.component.css']
})
export class EditExpertComponent implements OnInit {

  currentExpert = null;
  isSuccessful = false;
  errorMessage = '';

  constructor(private token: TokenStorageService, private expertService: ExpertService, private router: Router) {
  }

  ngOnInit() {
    this.getExpert();
  }

  onSubmit() {
    const {qualification, onlineHelp: onlineHelp} = this.currentExpert;
    let userId = this.token.getUser().id;
    this.expertService.edit(userId, qualification, onlineHelp).subscribe(
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
    let userId = this.token.getUser().id;
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
