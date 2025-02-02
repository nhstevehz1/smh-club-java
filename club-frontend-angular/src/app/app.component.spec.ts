import {AppComponent} from "./app.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideRouter} from "@angular/router";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {OAuthService} from "angular-oauth2-oidc";

describe('AppComponent', () => {
   let fixture: ComponentFixture<AppComponent>;
   let component: AppComponent;
   let oAuthServiceMock: jasmine.SpyObj<OAuthService>;

    beforeEach(async () => {
        oAuthServiceMock = jasmine.createSpyObj('OAuthService', ['get']);
       await TestBed.configureTestingModule({
           imports: [AppComponent],
           providers: [
               provideHttpClient(),
               provideHttpClientTesting(),
               provideRouter([]),
               provideNoopAnimations(),
               {provide: OAuthService, useValue: oAuthServiceMock},
           ]
       }).compileComponents();
       fixture = TestBed.createComponent(AppComponent);
       component = fixture.componentInstance;
   });

    it('should create the app', () => {
        expect(component).toBeTruthy();
    });

    it(`should contain an 'app-main-layout' component`, async () => {
        const layout = fixture.nativeElement.querySelector('app-main-layout');
        expect(layout ).toBeTruthy();
    });

});
