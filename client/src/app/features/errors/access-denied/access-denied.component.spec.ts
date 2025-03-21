import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccessDeniedComponent} from './access-denied.component';
import {TranslateModule} from "@ngx-translate/core";
import {By} from "@angular/platform-browser";

describe('AccessDeniedComponent', () => {
  let component: AccessDeniedComponent;
  let fixture: ComponentFixture<AccessDeniedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AccessDeniedComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccessDeniedComponent);
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
