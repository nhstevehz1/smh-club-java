import {Component, ViewChild} from '@angular/core';
import {MatNavList} from "@angular/material/list";
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {Router, RouterOutlet} from "@angular/router";
import {NavItem} from "../menu-list-item/nav-item";
import {MenuListItemComponent} from "../menu-list-item/menu-list-item.component";

@Component({
  selector: 'app-content',
    imports: [
        MatNavList,
        MatSidenav,
        MatSidenavContainer,
        MatSidenavContent,
        RouterOutlet,
        MenuListItemComponent
    ],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})
export class ContentComponent {
    @ViewChild(MatSidenav, {static: true}) sidenav!: MatSidenav;

    constructor(private router: Router) {
    }

    public toggleSideNav(): void {
        this.sidenav.toggle()
            .then(r => console.log("side nav is: " + r));
    }

    onMenuClicked(route: string): void {
        this.router.navigate([route]).then(r =>
            this.sidenav.toggle()
        );
    }

    isUser(): boolean {
        return true;
    }

    isManager(): boolean {
        return true;
    }

    isAdmin(): boolean {
        return false;
    }

    userNav: NavItem[] = [
        {
            displayName: 'Members',
            iconName: 'group',
            route: 'p/members',
        },
        {
            displayName: 'Addresses',
            iconName: 'contact_mail',
            route: 'p/addresses',
        },
        {
            displayName: 'Email',
            iconName: 'mail',
            route: 'p/emails',
        }
    ];

    mgrNav: NavItem[] = [
        {
            displayName: 'Manage Members',
            iconName: 'groups',
            route: 'p/manage/members'},
        {
            displayName: 'Utilities',
            iconName: 'settings',
            route: 'p/utils',
        }
    ];

    adminNav: NavItem[] = [
        {
            displayName: 'Manage Users',
            iconName: 'people',
            route: 'p/users'
        },
    ];
}
