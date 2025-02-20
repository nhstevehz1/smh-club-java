import { Component } from '@angular/core';
import {SuccessComponent} from "../../../../shared/components/success/success.component";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-login',
  imports: [
    SuccessComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(private authService: AuthService) {}

  onLogin(): void {
    this.authService.login('p/home');
  }
}
