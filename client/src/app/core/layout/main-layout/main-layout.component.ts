import {Component, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {MatDividerModule} from '@angular/material/divider';

import {HeaderComponent} from '@app/core/layout/header/header.component';
import {FooterComponent} from '@app/core/layout/footer/footer.component';
import {ContentComponent} from '@app/core/layout/content/content.component';
import {AuthService} from '@app/core/auth/services/auth.service';

@Component({
  selector: 'app-main-layout',
    imports: [
        HeaderComponent,
        FooterComponent,
        ContentComponent,
        MatDividerModule
    ],
  providers: [],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {
  @ViewChild(ContentComponent, {static: true}) content!: ContentComponent;

  constructor(private authService: AuthService,
              private router: Router,) {}

  get isLoggedIn(): boolean {
    return this.authService!.isLoggedIn()
  }

  get name(): string | undefined {
    return this.authService.getCurrentUser()?.fullName;
  }

  sideNavHandler(): void {
    this.content.toggleSideNav();
  }

  profileHandler(): void {
    this.router.navigate(['p/profile']).then();
  }

  logoutHandler(): void {
    this.authService.logOut();
  }
}
