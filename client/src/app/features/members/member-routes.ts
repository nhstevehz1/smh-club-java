import {Routes} from '@angular/router';
import {readGuard} from '@app/core/auth/guards/read.guard';
import {writeGuard} from '@app/core/auth/guards/write.guard';

export const memberRoutes: Routes = [{
    path: '',
    loadComponent: () => import('./list-members/list-members.component')
      .then(mod => mod.ListMembersComponent),
    canActivate: [readGuard]
  }, {
    path: 'add',
    loadComponent: () => import('./add-member/add-member.component')
        .then(mod => mod.AddMemberComponent),
    canActivate: [writeGuard]
  }, {
    path: 'view/:id',
    loadComponent: () => import('./view-member/view-member.component')
      .then(mod => mod.ViewMemberComponent),
    canActivate: [readGuard]
  }
]
