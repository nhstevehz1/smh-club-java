import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const permission = route.data['permission'] || '';

  // no need to determine if the user is logged in.
  // the oauth client should automatically redirect to the realm sign in page
  if(authService.hasPermission(permission)) {
    return true;
  } else {
    return router.createUrlTree(['p/access-denied']);
  }

  /*if(authService.isLoggedIn) {
    if(permission === '') {
      // case: No permission is specified. The route is not allowed.
      // this is only a safeguard.  No route should ever be configured without a permission.
      return false;
    }
    else if (authService.hasPermission(permission)) {
      // case: a permission is specified and the user has the permission
      return true;
    } else {
      // case: a permission  is specified but the user does not have the permission
      // navigate to access denied screen
      return router.createUrlTree(['p/access-denied']);
    }
  }
  // navigate to the login page.  User is not logged in.
  return router.createUrlTree(['p/login']);*/
};
