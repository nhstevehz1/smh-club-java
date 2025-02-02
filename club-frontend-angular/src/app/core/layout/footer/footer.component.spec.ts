import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FooterComponent} from './footer.component';
import {DateTime} from "luxon";

describe('FooterComponent', () => {
  let fixture: ComponentFixture<FooterComponent>;
  let component: FooterComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FooterComponent]
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
    expect(span.textContent).toContain(`Social Club ${DateTime.now().toFormat('yyyy')}`);
  });
});
