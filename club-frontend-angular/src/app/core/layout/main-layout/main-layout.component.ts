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
  providers: [AuthService],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {
  @ViewChild(ContentComponent, {static: true}) content!: ContentComponent;

  constructor(private authService: AuthService,
              private router: Router,) {}

  get isLoggedIn(): boolean {
    return this.authService!.isLoggedIn
  }

  get name(): string {
    return this.authService!.userName
  }

  get lastLogin(): string | null{
    // TODO: implement
    return null;
  }

  sideNaveHandler(): void {
    this.content.toggleSideNav();
  }

  async profileHandler(): Promise<void> {
    await this.router.navigate(['p/profile']);
  }

  logoutHandler(): void {
    this.authService.signOut();
  }
}
