import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {Subscription} from "rxjs";
import {HeaderComponent} from "../header/header.component";
import {MatNavList} from "@angular/material/list";
import {MenuListItemComponent} from "../menu-list-item/menu-list-item.component";
import {MatDivider} from "@angular/material/divider";
import {Router, RouterOutlet} from "@angular/router";
import {NavService} from "../services/nav.service";
import {NavItem} from "../menu-list-item/nav-item";
import {AuthService} from "../../auth/services/auth.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-main-layout',
  imports: [
    MatSidenavContainer,
    MatSidenavContent,
    HeaderComponent,
    MatNavList,
    MenuListItemComponent,
    MatDivider,
    RouterOutlet,
    MatSidenav,
    NgIf
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild(MatSidenav, {static: true}) sidenav!: MatSidenav;

  name: string = '';
  isAuthed: boolean = false;
  lastLogin: string = '';

  private userSubscription: Subscription | null = null;

  constructor(public navService: NavService,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.userSubscription = this.authService.user.subscribe(u => {
      console.info("Main layout sub fired: " + u );

      if (u) {
        this.name = u.username;
        this.isAuthed = this.authService.isAuthenticated();
        this.lastLogin = u.lastLogin;
      } else {
        this.name = '';
        this.isAuthed = false;
        this.lastLogin = '';
      }
    })
  }

  ngAfterViewInit() {
    this.navService.appDrawer = this.sidenav;
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  isUser(): boolean {
    return true;
    /*return this.auth.isAuthenticated
        && this.auth.isInRoles(RoleType.User)*/
  }

  isManager(): boolean {
    return false;
    /*return this.auth.isAuthenticated
        && this.auth.isInRoles(RoleType.Manager);*/
  }

  isAdmin(): boolean {
    return false;
    /*return this.auth.isAuthenticated
        && this.auth.isInRoles(RoleType.Admin);*/
  }

  logoutHandler(): void {

    /*this.auth.logout().subscribe(() => {
      this.sidenav.close();
      this.router.navigate(['p/login']);
    });*/

  }

  profileHandler(): void {
    this.router.navigate(['p/profile']);
  }

  userNav: NavItem[] = [
    {
      displayName: 'Members',
      iconName: 'group',
      route: 'p/home',
      disabled: false,
      children: []
    }
  ];

  mgrNav: NavItem[] = [
    {
      displayName: 'Manage Members',
      iconName: 'groups',
      route: 'p/manage/members',
      disabled: false,
      children: []
    },
    {
      displayName: 'Utilities',
      iconName: 'settings',
      route: 'p/utils',
      disabled: false,
      children: []
    }
  ];

  adminNav: NavItem[] = [
    {
      displayName: 'Manage Users',
      iconName: 'people',
      route: 'p/users',
      disabled: false,
      children: []
    },
  ];
}
