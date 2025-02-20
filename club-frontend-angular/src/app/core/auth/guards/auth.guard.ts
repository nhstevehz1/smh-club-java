import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {map} from "rxjs/operators";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const permission = route.data['permission'] || '';

  console.log('auth guard url: ',state.url);
  console.log('auth guard permission: ', permission);

  // no need to determine if the user is logged in.
  // the oauth client should automatically redirect to the realm sign in page


    return true;

  /*return authService.canActivateRoutes$.pipe(
      map(() => {
          if(authService.hasPermission(permission)) {
              console.log('hasPermission: false');
            return true;
          } else {
              console.log('hasPermission: false');
            return router.createUrlTree(['p/access-denied']);
          }
      })
  )*/
};
