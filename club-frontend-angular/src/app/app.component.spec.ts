import {AppComponent} from "./app.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideRouter} from "@angular/router";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {AuthService} from "./core/auth/services/auth.service";
import {BehaviorSubject, Observable} from "rxjs";
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatProgressSpinnerHarness} from "@angular/material/progress-spinner/testing";

describe('AppComponent', () => {
   let fixture: ComponentFixture<AppComponent>;
   let component: AppComponent;
   let loader: HarnessLoader;

   let authSvcMock: jasmine.SpyObj<AuthService>;

   let isLoadedSubjectMock$: BehaviorSubject<boolean>;
   let isLoadedMock$: Observable<boolean>;

    beforeEach(async () => {
        authSvcMock = jasmine.createSpyObj('AuthService', [
            'getCurrentUser', 'hasPermission',
            'isLoggedIn', 'rolesLoaded$']);

        isLoadedSubjectMock$ = new BehaviorSubject(false);
        isLoadedMock$ = isLoadedSubjectMock$.asObservable();
        authSvcMock.isLoaded$ = isLoadedMock$;

       await TestBed.configureTestingModule({
           imports: [AppComponent, MainLayoutComponent],
           providers: [
               provideHttpClient(),
               provideHttpClientTesting(),
               provideRouter([]),
               provideNoopAnimations(),
               {provide: AuthService, useValue: {}},
           ]
       }).overrideComponent(AppComponent,
           {set: {providers: [{provide: AuthService, useValue: authSvcMock}]}
       }).compileComponents();

       fixture = TestBed.createComponent(AppComponent);
       component = fixture.componentInstance;
       loader = TestbedHarnessEnvironment.loader(fixture);
   });

    it('should create the app', () => {
        expect(component).toBeTruthy();
    });

    it(`should display an 'app-main-layout' component when isLoaded is true`, async () => {
        isLoadedSubjectMock$.next(true);

        fixture.detectChanges();
        await fixture.whenStable();

        const layout = fixture.nativeElement.querySelector('app-main-layout');
        expect(layout ).toBeTruthy();
    });

    it(`should NOT display an 'app-main-layout' component when isLoaded is false`, async () => {
        isLoadedSubjectMock$.next(false);

        fixture.detectChanges();
        await fixture.whenStable();

        const layout = fixture.nativeElement.querySelector('app-main-layout');
        expect(layout).toBeFalsy();
    });

    it('should display spinner when isLoaded is false', async () => {
        isLoadedSubjectMock$.next(false);
        const harnesses = await loader.getAllHarnesses(MatProgressSpinnerHarness);

        expect(harnesses.length).toEqual(1);
    });

    it('should NOT display spinner when isLoaded is true', async () => {
        isLoadedSubjectMock$.next(true);
        const harnesses = await loader.getAllHarnesses(MatProgressSpinnerHarness);

        expect(harnesses.length).toEqual(0);
    });
});
