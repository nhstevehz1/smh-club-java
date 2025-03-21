import {Component, OnInit} from '@angular/core';
import {AuthUser} from "../../models/auth-user";
import {AuthService} from "../../services/auth.service";
import {MatListModule} from "@angular/material/list";
import {MatCardModule} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";

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
