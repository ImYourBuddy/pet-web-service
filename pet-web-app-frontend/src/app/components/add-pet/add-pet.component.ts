import {Component} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage/token-storage.service';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-add-pet',
  templateUrl: './add-pet.component.html',
  styleUrls: ['./add-pet.component.css']
})
export class AddPetComponent {
  pet: any = {
    species: null,
    name: null,
    breed: null,
    gender: null,
    birthdate: null,
    owner: null
  };

  errorMessage = '';

  constructor(private userService: UserService, private token: TokenStorageService) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit(): void {
  }

  onSubmit(): void {
    this.pet.owner = this.token.getUser().id;
    const {species, name, breed, gender, birthdate, owner} = this.pet;

    this.userService.addPet(owner, species, name, breed, gender, birthdate).subscribe(
      data => {
        console.log(data);
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

}
