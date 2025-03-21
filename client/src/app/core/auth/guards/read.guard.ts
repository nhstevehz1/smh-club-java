import {CanActivateFn, Router} from '@angular/router';
import {inject, isDevMode} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {take} from "rxjs/operators";
import {tap} from "rxjs";
import {PermissionType} from "../models/permission-type";

export const readGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if(isDevMode()) {
      console.debug('readGuard  route.url', route.url);
      console.debug('readGuard state.url', state.url);
  }

  return authService.isAuthenticated$.pipe(
      take(1),
      tap(isAuthed => {
          if(!isAuthed) {
              router.navigate(['p/login']).then();
          } else if(!authService.hasPermission(PermissionType.read)){
              router.navigate(['p/access-denied']);
          }
      })
  );
};
