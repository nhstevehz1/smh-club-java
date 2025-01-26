import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
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
export class MainLayoutComponent implements OnInit, OnDestroy {
  @ViewChild(ContentComponent, {static: true}) content!: ContentComponent;

  constructor(private authService: AuthService,
              private router: Router,) {}

  get isAuthed(): boolean {
    return this.authService!.isAuthenticated
  }

  get name(): string {
    return this.authService!.userName
  }

  get lastLogin(): string | null{
    return null; //this.authUser!.lastLogin
  }

  ngOnInit() {
    /*this.userSubscription = this.authService.user.subscribe(u => {
      console.info("Main layout sub fired: " + u );

      if (u) {
        this.authUser = u;
      }
    });*/
  }

  ngOnDestroy() {
   /* if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }*/
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
    this.router.navigate(['p/profile']).then(() =>
    console.log("Profile menu clicked"));
  }

}
