import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {take} from "rxjs/operators";
import {PermissionType} from "../models/permission-type";
import {tap} from "rxjs";

export const writeGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.rolesLoaded$.pipe(
      take(1),
      tap(rolesLoaded => {

        if(!authService.isLoggedIn()) {
          router.navigate(['p/login']).then();
        }

        if(rolesLoaded && !authService.hasPermission(PermissionType.write)) {
          router.navigate(['p/access-denied']).then();
        }
      })
  );
};
