import {Routes} from '@angular/router';
import {readGuard, writeGuard} from '@app/core/auth';

export const memberRoutes: Routes = [{
    path: '',
    loadComponent: () => import('./list-members')
      .then(mod => mod.ListMembersComponent),
    canActivate: [readGuard]
  }, {
    path: 'p/members/add',
    loadComponent: () => import('./add-member')
        .then(mod => mod.AddMemberComponent),
    canActivate: [writeGuard]
  }, {
    path: 'p/members/view:id',
    loadComponent: () => import('./view-member')
      .then(mod => mod.ViewMemberComponent),
    canActivate: [readGuard]
  }
]
