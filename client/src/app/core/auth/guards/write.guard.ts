import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from "../services/auth.service";
import {inject, isDevMode} from "@angular/core";
import {take} from "rxjs/operators";
import {PermissionType} from "../models/permission-type";
import {tap} from "rxjs";

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
