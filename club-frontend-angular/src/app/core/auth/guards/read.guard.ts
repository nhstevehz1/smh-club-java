import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {take} from "rxjs/operators";
import {tap} from "rxjs";

export const readGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.debug('read guard url', route.url);
  console.debug('read guard state', state);

  return authService.isAuthenticated$.pipe(
      take(1),
      tap(isAuthed => {
          if(!isAuthed) {
              // TODO: added target url
              router.navigate(['p/login']).then();
          }
      })
  );
};
