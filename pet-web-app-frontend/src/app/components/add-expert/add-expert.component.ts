import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';

@Component({
  selector: 'app-add-expert',
  templateUrl: './add-expert.component.html',
  styleUrls: ['./add-expert.component.css']
})
export class AddExpertComponent {
  expert: any = {
    qualification: null,
    onlineHelp: null,
    userId: null
  };

  isSuccessful = false;
  errorMessage = '';

  constructor(private expertService: ExpertService, private token: TokenStorageService) {
  }

  onSubmit(): void {
    this.expert.userId = this.token.getUser().id;
    const {qualification, onlineHelp, userId} = this.expert;

    this.expertService.requestExpert(qualification, onlineHelp, userId).subscribe(
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
