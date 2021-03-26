import {Component} from '@angular/core';
import {ExpertService} from '../../services/expert-service/expert.service';

@Component({
  selector: 'app-experts-list',
  templateUrl: './experts-list.component.html',
  styleUrls: ['./experts-list.component.css']
})
export class ExpertsListComponent {
  experts: any;
  constructor(private expertService: ExpertService) {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    this.getExperts();
  }

  getExperts() {
    this.expertService.getAllExperts()
      .subscribe(
        data => {
          this.experts = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

}
