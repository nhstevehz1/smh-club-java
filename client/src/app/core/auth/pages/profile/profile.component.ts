import {Component, OnInit} from '@angular/core';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import {TranslatePipe} from '@ngx-translate/core';
import {AuthUser, AuthService} from '@app/core/auth';

@Component({
  selector: 'app-profile',
    imports: [
        MatListModule,
        MatCardModule,
        TranslatePipe
    ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  user?: AuthUser;

  constructor(private authSvc: AuthService) {}

  ngOnInit(): void {
    this.user = this.authSvc.getCurrentUser();
  }

}
