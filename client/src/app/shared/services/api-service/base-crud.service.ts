import { Injectable } from '@angular/core';
import {CrudService} from '@app/shared/services/api-service/crud-service';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Updatable} from '@app/shared/models/updatable';

@Injectable()
export class BaseCrudService<T extends Updatable> implements CrudService<T>{

  readonly baseUri: string;

  protected constructor(baseUri: string,
                        protected http: HttpClient) {
    this.baseUri = baseUri;
  }

  get(id: number) : Observable<T> {
    return this.http.get<T>(`${this.baseUri}/${id}`);
  }

  getAll(): Observable<T[]> {
    return this.http.get<T[]>(`${this.baseUri}/all`)
  }

  create(create: T): Observable<T> {
    return this.http.post<T>(this.baseUri, create);
  }

  update(update: T): Observable<T> {
    return this.http.put<T>(`${this.baseUri}/${update.id}`, update );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUri}/${id}`);
  }
}
