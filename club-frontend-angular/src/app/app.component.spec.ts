import {AppComponent} from "./app.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideRouter} from "@angular/router";
import {provideNoopAnimations} from "@angular/platform-browser/animations";

describe('AppComponent', () => {
   let fixture: ComponentFixture<AppComponent>;
   let component: AppComponent;

    beforeEach(async () => {
       await TestBed.configureTestingModule({
           providers: [
               AppComponent,
               provideHttpClient(),
               provideHttpClientTesting(),
               provideRouter([]),
               provideNoopAnimations()
           ],
           schemas: [NO_ERRORS_SCHEMA]
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
