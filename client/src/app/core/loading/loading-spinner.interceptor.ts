import {HttpInterceptorFn, HttpResponse} from '@angular/common/http';
import {inject} from '@angular/core';
import {LoadingSpinnerService} from './loading-spinner.service';
import {catchError} from 'rxjs/operators';
import {tap} from 'rxjs';

export const loadingSpinnerInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingSvc = inject(LoadingSpinnerService);

  return next(req)
      .pipe(
          tap(event => {
          if (event instanceof HttpResponse) {
            loadingSvc.setLoading(false, req.url);
          }
        }),
        catchError(err => {
          loadingSvc.setLoading(false, req.url);
          return next(err)
      }));
}
