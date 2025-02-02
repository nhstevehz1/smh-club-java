import {Component, ViewChild} from '@angular/core';
import {MatListModule} from "@angular/material/list";
import {MatSidenav, MatSidenavModule} from "@angular/material/sidenav";
import {Router, RouterOutlet} from "@angular/router";
import {NavItem} from "./models/nav-item";
import {AuthService} from "../../auth/services/auth.service";
import {PermissionType} from "../../auth/models/permission-type";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-content',
  imports: [
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    RouterOutlet
  ],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})
export class ContentComponent {
    @ViewChild(MatSidenav, {static: true}) sidenav!: MatSidenav;

    constructor(private router: Router, private authService: AuthService) {}

    public toggleSideNav(): void {
        this.sidenav.toggle()
            .then(r => console.log("side nav is: " + r));
    }

    onMenuClicked(route: string): void {
        this.router.navigate([route]).then(() =>
            this.sidenav.toggle()
        );
    }

    hasPermission(permission: PermissionType): boolean {
        return this.authService.hasPermission(permission);
    }

    navList: NavItem[] = [
        {
            displayName: 'Members',
            iconName: 'group',
            route: 'p/members',
            permission: PermissionType.read
        },
        {
            displayName: 'Addresses',
            iconName: 'contact_mail',
            route: 'p/addresses',
            permission: PermissionType.read
        },
        {
            displayName: 'Emails',
            iconName: 'mail',
            route: 'p/emails',
            permission: PermissionType.read
        },
        {
            displayName: 'Phones',
            iconName: 'phone',
            route: 'p/phones',
            permission: PermissionType.read
        }
    ];
}
