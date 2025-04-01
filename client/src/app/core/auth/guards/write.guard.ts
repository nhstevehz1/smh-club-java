import {inject, isDevMode} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {take, tap} from 'rxjs/operators';

import {AuthService, PermissionType} from '@app/core/auth';

export const writeGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

    if(isDevMode()) {
        console.debug('writeGuard  route.url', route.url);
        console.debug('writeGuard state.url', state.url);
    }

  return authService.isAuthenticated$.pipe(
      take(1),
      tap(authed => {
        if(!authed) {
          router.navigate(['p/login']).then();
        } else if(!authService.hasPermission(PermissionType.write)) {
          router.navigate(['p/access-denied']).then();
        }
      })
  );
};
