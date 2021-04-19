import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user-service/user.service';
import {Pet} from '../../models/pet.model';

@Component({
  selector: 'app-edit-pet',
  templateUrl: './edit-pet.component.html',
  styleUrls: ['./edit-pet.component.css']
})
export class EditPetComponent implements OnInit {
  currentPet: Pet;
  showErrorMessage = false;
  errorMessage = '';

  constructor(private userService: UserService, private route: ActivatedRoute, private token: TokenStorageService, private router: Router) {
  }

  ngOnInit(): void {
    const tok = this.token.getToken();
    if (tok == null) {
      this.router.navigate(['/login']);
    }
    this.getPet(this.route.snapshot.paramMap.get('petId'));
  }

  onSubmit(): void {
    this.userService.editPet(this.currentPet).subscribe(
      data => {
        console.log(data);
        this.router.navigate(['profile']);
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }


  getPet(petId): void {
    const userId = this.token.getUser().id;
    this.userService.getPet(userId, petId)
      .subscribe(
        data => {
          this.currentPet = data;
          console.log(this.currentPet.birthdate);
        },
        error => {
          this.showErrorMessage = true;
          this.errorMessage = error.error;
        });
  }
}
