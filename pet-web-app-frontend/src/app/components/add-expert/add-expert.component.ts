import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {Router} from '@angular/router';
import {Expert} from '../../models/expert.model';
import {AuthService} from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-add-expert',
  templateUrl: './add-expert.component.html',
  styleUrls: ['./add-expert.component.css']
})
export class AddExpertComponent {
  expert: Expert = {
    qualification: '',
    onlineHelp: null,
    userId: null
  };

  isSuccessful = false;
  errorMessage = '';

  constructor(private expertService: ExpertService, private token: TokenStorageService,
              private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
  }

  onSubmit(): void {
    this.expert.userId = this.token.getUser().id;

    this.expertService.requestExpert(this.expert).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.router.navigate(['/profile']);
      },
      err => {
        if (err.status == 401) {
          this.authService.logoutUser().subscribe();
          this.token.signOut();
          window.location.reload();
          this.router.navigate(['/login']);
        }
        this.errorMessage = err.error.message;
      }
    );

  }
}
