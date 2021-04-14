import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-edit-pet',
  templateUrl: './edit-pet.component.html',
  styleUrls: ['./edit-pet.component.css']
})
export class EditPetComponent implements OnInit {
  currentPet = null;
  isSuccessful = false;
  errorMessage = '';

  constructor(private userService: UserService, private route: ActivatedRoute, private token: TokenStorageService, private router: Router) {
  }

  ngOnInit(): void {
    this.getPet(this.route.snapshot.paramMap.get('petId'));
  }

  onSubmit(): void {
    const {id, name, species, breed, gender, birthdate} = this.currentPet;
    let userId = this.token.getUser().id;
    this.userService.editPet(userId, id, name, species, breed, gender, birthdate).subscribe(
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


  getPet(petId) {
    let userId = this.token.getUser().id;
    this.userService.getPet(userId, petId)
      .subscribe(
        data => {
          this.currentPet = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }
}
