import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const role = route.data['role'] || '';


  if(authService.isAuthenticated) {
    if(role === '') {
      // case: No role is specified. The route is allowed.
      return true;
    }
    else if (authService.hasRole(role)) {
      // case: a role is specified and the user's claims include the role
      return true;
    } else {
      // case: a role is specified but the user's claims doesn't include the role
      // navigate to access denied screen
      return router.createUrlTree(['p/access-denied']);
    }
  }
  // navigate to the login page
  return router.createUrlTree(['p/login']);
};
