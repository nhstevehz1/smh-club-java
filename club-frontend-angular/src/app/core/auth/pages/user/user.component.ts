import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {MatCardModule} from "@angular/material/card";
import {MatLabel} from "@angular/material/form-field";
import {MatDivider} from "@angular/material/divider";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'app-user',
    imports: [
        MatCardModule,
        MatLabel,
        MatDivider,
        JsonPipe
    ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent {

  constructor(private auth: AuthService) {}

  get idToken(): string {
    return this.auth.idToken;
  }

  get userName(): string {
    return this.auth.userName;
  }

  get refreshToken(): string {
    return this.auth.refreshToken;
  }
  get accessToken(): string {
    return this.auth.accessToken;
  }

  get email(): string {
    return this.auth.email;
  }

  get roles(): string[] {
    return this.auth.roles;
  }
}
