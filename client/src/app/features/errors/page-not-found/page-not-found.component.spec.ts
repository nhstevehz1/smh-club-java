import {ComponentFixture, TestBed} from '@angular/core/testing';
import {By} from '@angular/platform-browser';
import {TranslateModule} from '@ngx-translate/core';

import {PageNotFoundComponent} from '@app/features/errors';

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
