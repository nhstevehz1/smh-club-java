import {Component} from '@angular/core';
import {AuthUser} from "../../models/auth-user";
import {AuthService} from "../../services/auth.service";
import {MatList, MatListItemTitle, MatListModule} from "@angular/material/list";
import {MatCard, MatCardModule, MatCardTitle} from "@angular/material/card";

@Component({
  selector: 'app-profile',
  imports: [
    MatListModule,
    MatCardModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {

  user?: AuthUser;

  constructor(private authSvc: AuthService) {
    this.user = this.authSvc.currentUser;
  }

}
