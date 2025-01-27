import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const role = route.data['role'];

  if(authService.isAuthenticated) {
    if (authService.hasRole(role)) {
      return true
    } else {
      // navigate to access denied screen
      router.navigate(['p/access-denied']);
      return false;
    }
  }

  router.navigate(['p/login']);
  return false;
};
