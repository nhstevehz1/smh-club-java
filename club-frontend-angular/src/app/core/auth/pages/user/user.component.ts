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
    return this.auth.getIdToken();
  }

  get name(): string {
    return this.auth.getGivenName();
  }

  get refreshToken(): string {
    return this.auth.getRefreshToken();
  }
  get accessToken(): string {
    return this.auth.getAccessToken();
  }

  get email(): string {
    return this.auth.getEmail();
  }

  get roles(): string[] {
    return this.auth.getRoles();
  }
}
