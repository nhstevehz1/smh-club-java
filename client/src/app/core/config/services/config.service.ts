import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable, tap} from 'rxjs';
import {map} from 'rxjs/operators';

import {SystemConfig} from '@app/core/config';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private uri = '/api/v1/config';
  private config?: SystemConfig;

  constructor(private httpHttp: HttpClient) {}

  public get systemConfig(): SystemConfig | undefined {
   return this.config;
  }

  public loadSystemConfig(): Observable<void> {
      return this.httpHttp.get<SystemConfig>(this.uri).pipe(
          tap((config: SystemConfig) => this.config = config),
          map(():void => {return;})
      );
  }
}
