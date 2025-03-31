import {Routes} from '@angular/router';
import {readGuard, writeGuard} from '@app/core/auth';

export const memberRoutes: Routes = [{
    path: '',
    loadComponent: () => import('./list-members/list-members.component')
      .then(mod => mod.ListMembersComponent),
    canActivate: [readGuard]
  }, {
    path: 'p/members/add',
    loadComponent: () => import('./add-member/add-member.component')
        .then(mod => mod.AddMemberComponent),
    canActivate: [writeGuard]
  }, {
    path: 'p/members/view:id',
    loadComponent: () => import('./view-member/view-member.component')
      .then(mod => mod.ViewMemberComponent),
    canActivate: [readGuard]
  }
]
