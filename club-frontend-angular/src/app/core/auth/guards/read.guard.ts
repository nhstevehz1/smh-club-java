import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {take} from "rxjs/operators";
import {tap} from "rxjs";

export const readGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated$.pipe(
      take(1),
      tap(isAuthed => {
          if(!isAuthed) {
              router.navigate(['p/login']).then();
          }
      })
  );
};
