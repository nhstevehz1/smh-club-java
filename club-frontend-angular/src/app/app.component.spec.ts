import {MockBuilder, MockRender} from "ng-mocks";
import {AppComponent} from "./app.component";

describe('AppComponent', () => {
   beforeEach(() => {
       return MockBuilder(AppComponent);
   });

    it('should create the app', () => {
        const fixture = MockRender(AppComponent);
        const app = fixture.componentInstance;
        expect(app).toBeTruthy();
    });

    it(`should contain an 'app-main-layout' component`, async () => {
        const fixture = MockRender(AppComponent);
        const layout = fixture.nativeElement.querySelector('app-main-layout');
        expect(layout ).toBeTruthy();
    });

});
