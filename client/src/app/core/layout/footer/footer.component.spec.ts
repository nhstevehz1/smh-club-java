import {ComponentFixture, TestBed} from '@angular/core/testing';
import {DateTime} from 'luxon';
import {TranslateModule} from '@ngx-translate/core';

import {FooterComponent} from './footer.component';

describe('FooterComponent', () => {
  let fixture: ComponentFixture<FooterComponent>;
  let component: FooterComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        FooterComponent,
        TranslateModule.forRoot({})
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return correct year', () => {
    const year = component.getYear();
    expect(year).toBeTruthy();
    expect(year === DateTime.now().toFormat('yyyy')).toBeTrue();
  });

  it('should render year', () => {
    fixture.detectChanges();
    const span = fixture.nativeElement.querySelector('span');
    expect(span.textContent).toContain(`layout.footer.title ${DateTime.now().toFormat('yyyy')}`);
  });
});
