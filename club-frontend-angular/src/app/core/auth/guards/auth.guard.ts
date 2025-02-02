import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const permission = route.data['permission'] || '';

  console.log(state.url);

  // no need to determine if the user is logged in.
  // the oauth client should automatically redirect to the realm sign in page
  if(authService.hasPermission(permission)) {
    return true;
  } else {
    return router.createUrlTree(['p/access-denied']);
  }
};
