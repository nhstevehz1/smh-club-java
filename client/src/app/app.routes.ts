import {Routes} from '@angular/router';
import {readGuard} from '@app/core/auth';

export const routes: Routes = [{
    path: '',
    pathMatch: 'full',
    redirectTo: 'p/home'
  }, {
    path: 'p/home',
    loadComponent: () =>
        import('./features/home/home.component')
            .then(mod => mod.HomeComponent),
    canActivate: [readGuard]
  }, {
    path: 'p/members',
    loadChildren: () => import('./features/members/member-routes')
      .then(m => m.memberRoutes),
    canActivate:[readGuard]
  }, {
    path: 'p/addresses',
    loadComponent: () =>
        import('./features/addresses/list-addresses/list-addresses.component')
            .then(mod => mod.ListAddressesComponent),
    canActivate: [readGuard]
  }, {
    path: 'p/emails',
    loadComponent: () =>
        import('./features/emails/list-emails/list-emails.component')
            .then(mod => mod.ListEmailsComponent),
    canActivate: [readGuard],
  }, {
  path: 'p/phones',
  loadComponent: () =>
      import('./features/phones/list-phones/list-phones.component')
          .then(mod => mod.ListPhonesComponent),
    canActivate: [readGuard]
  }, {
    path: 'p/access-denied',
    loadComponent: () =>
        import('./features/errors/access-denied/access-denied.component')
            .then(mod => mod.AccessDeniedComponent)
  }, {
    path: 'p/login',
    loadComponent: () =>
        import('./core/auth/pages/login/login.component')
            .then(mod => mod.LoginComponent)
  }, {
    path: 'p/profile',
    loadComponent: () =>
      import('./core/auth/pages/profile/profile.component')
          .then(mod => mod.ProfileComponent)
  }, {
    path: '**',
    redirectTo: 'p/home'
  }
];
