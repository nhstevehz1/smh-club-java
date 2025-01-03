import {Injectable} from '@angular/core';
import {BehaviorSubject, filter} from "rxjs";
import {NavigationEnd, Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavService {

  public set appDrawer(drawer: any) {
    this._appDrawer  = drawer;
  }

  public get currentUrl(): BehaviorSubject<string | undefined> {
    return this._currentUrl;
  }

  private _appDrawer: any;
  private _currentUrl = new BehaviorSubject<string | undefined>(undefined);

  constructor(private router: Router ) {

    this.router.events
        .pipe(filter(event => event instanceof NavigationEnd))
        .subscribe((event: NavigationEnd) => {
      this.currentUrl.next(event.urlAfterRedirects)
    });
  }

  public closeNav() {
    this._appDrawer.close();
  }

  public openNav() {
    this._appDrawer.open();
  }
}
