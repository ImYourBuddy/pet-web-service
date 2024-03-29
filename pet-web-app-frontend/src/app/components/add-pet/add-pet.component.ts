import {Component} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Router} from '@angular/router';
import {Pet} from '../../models/pet.model';
import {AuthService} from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-add-pet',
  templateUrl: './add-pet.component.html',
  styleUrls: ['./add-pet.component.css']
})
export class AddPetComponent {
  pet: Pet = {
    species: '',
    name: '',
    breed: '',
    gender: '',
    birthdate: null,
    owner: null
  };

  isSuccessful = false;
  errorMessage = '';

  constructor(private userService: UserService, private token: TokenStorageService,
              private router: Router, private authService: AuthService) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
  }

  onSubmit(): void {
    this.pet.owner = this.token.getUser().id;

    this.userService.addPet(this.pet).subscribe(
      data => {
        this.isSuccessful = true;
        console.log(data);
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
