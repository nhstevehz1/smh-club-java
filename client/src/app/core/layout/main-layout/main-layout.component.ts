import {Component, ViewChild} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/services/auth.service";
import {FooterComponent} from "../footer/footer.component";
import {ContentComponent} from "../content/content.component";
import {MatDividerModule} from "@angular/material/divider";

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
