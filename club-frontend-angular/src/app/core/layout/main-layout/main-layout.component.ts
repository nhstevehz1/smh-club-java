import {Component, inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {HeaderComponent} from "../header/header.component";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/services/auth.service";
import {AuthUser} from "../../auth/models/auth-user";
import {FooterComponent} from "../footer/footer.component";
import {ContentComponent} from "../content/content.component";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-main-layout',
    imports: [
        HeaderComponent,
        FooterComponent,
        ContentComponent,
        MatDivider
    ],
  providers: [AuthService],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  private _authService = inject(AuthService);
  private _router = inject(Router);

  @ViewChild(ContentComponent, {static: true}) content!: ContentComponent;

  get isAuthed(): boolean {
    return this._authService!.isAuthenticated();
  }

  get name(): string {
    return this.authUser!.username
  }

  get lastLogin(): string | null{
    return this.authUser!.lastLogin
  }

  private authUser: AuthUser | null = null;

  private userSubscription: Subscription | null = null;

  ngOnInit() {
    this.userSubscription = this._authService.user.subscribe(u => {
      console.info("Main layout sub fired: " + u );

      if (u) {
        this.authUser = u;
      }
    })
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  logoutHandler(): void {

    /*this.auth.logout().subscribe(() => {
      this.sidenav.close();
      this.router.navigate(['p/login']);
    });*/

  }

  sideNaveHandler(): void {
    this.content.toggleSideNav();
  }

  profileHandler(): void {
    this._router.navigate(['p/profile']).then(() =>
    console.log("Profile menu clicked"));
  }

}
