import {Routes} from '@angular/router';

export const routes: Routes = [
    {
        path: 'p/home',
        loadComponent: () => import('./features/home/home.component')
            .then(mod => mod.HomeComponent)
    },
    {
        path: 'p/members',
        loadComponent: () => import('./features/members/list-members/list-members.component')
            .then(mod => mod.ListMembersComponent)
    },
    {
        path: 'p/addresses',
        loadComponent: () => import('./features/addresses/addresses.component')
            .then(mod => mod.AddressesComponent)
    },
    {
        path: 'p/emails',
        loadComponent: () => import('./features/emails/emails.component')
            .then(mod => mod.EmailsComponent)
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'p/home'
    },
    {
        path: '**',
        loadComponent: () => import('./features/page-not-found/page-not-found.component')
            .then(mod => mod.PageNotFoundComponent)
    }
];
