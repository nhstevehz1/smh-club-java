import {PageNotFoundComponent} from './page-not-found.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {TranslateModule} from "@ngx-translate/core";
import {By} from "@angular/platform-browser";

describe('PageNotFoundComponent', () => {
  let fixture: ComponentFixture<PageNotFoundComponent>;
  let component: PageNotFoundComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
          PageNotFoundComponent,
          TranslateModule.forRoot({})
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain title page component', () => {
    const element = fixture.debugElement.query(By.css('app-title-page'));
    expect(element).toBeTruthy();
  });
});
