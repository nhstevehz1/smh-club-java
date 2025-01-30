import {Routes} from '@angular/router';
import {authGuard} from "./core/auth/guards/auth.guard";
import {PermissionType} from "./core/auth/models/permission-type";

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'p/home'
    },
    {
      path: 'p/user',
      loadComponent: () =>
          import('./core/auth/pages/user/user.component')
              .then(mod => mod.UserComponent)
    },
    {
        path: 'p/home',
        loadComponent: () =>
            import('./features/home/home.component')
                .then(mod => mod.HomeComponent),
        canActivate: [authGuard], data: { permission: PermissionType.read}
    },
    {
        path: 'p/members',
        loadComponent: () =>
            import('./features/members/list-members/list-members.component')
                .then(mod => mod.ListMembersComponent),
        canActivate: [authGuard], data: { permission: PermissionType.read}
    },
    {
        path: 'p/addresses',
        loadComponent: () =>
            import('./features/addresses/list-addresses/list-addresses.component')
                .then(mod => mod.ListAddressesComponent),
        canActivate: [authGuard], data: { permission: PermissionType.read}
    },
    {
        path: 'p/emails',
        loadComponent: () =>
            import('./features/emails/list-emails/list-emails.component')
                .then(mod => mod.ListEmailsComponent),
        canActivate: [authGuard], data: { permission: PermissionType.read}
    },
    {
      path: 'p/phones',
      loadComponent: () =>
          import('./features/phones/list-phones/list-phones.component')
              .then(mod => mod.ListPhonesComponent),
        canActivate: [authGuard], data: { permission: PermissionType.read}
    },
    {
      path: 'p/access-denied',
      loadComponent: () =>
          import('./features/errors/access-denied/access-denied.component')
              .then(mod => mod.AccessDeniedComponent)
    },
    {
        path: '**',
        redirectTo: 'p/home'
    }
];
