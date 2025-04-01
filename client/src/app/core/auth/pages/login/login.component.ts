import { Component } from '@angular/core';
import {OkCancelComponent} from '@app/shared/components/ok-cancel';
import {AuthService} from '@app/core/auth';

@Component({
  selector: 'app-login',
  imports: [
    OkCancelComponent
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
