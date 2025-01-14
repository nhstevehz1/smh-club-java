import {ComponentFixture, ComponentFixtureAutoDetect, TestBed} from '@angular/core/testing';

import {FooterComponent} from './footer.component';
import {MockBuilder, MockRender} from "ng-mocks";
import {DateTime} from "luxon";
import {AppComponent} from "../../../app.component";

describe('FooterComponent', () => {

  beforeEach(async () => {
    return MockBuilder(FooterComponent)
        .keep(ComponentFixtureAutoDetect);
  });

  it('should create', () => {
    const fixture = MockRender(FooterComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it('should return correct year', () => {
    const fixture = MockRender(FooterComponent);
    const component = fixture.componentInstance;
    const year = component.year;
    expect(year).toBeTruthy();
    expect(year === DateTime.now().toFormat('yyyy')).toBeTrue();
  });

  it('should render year', () => {
    const fixture = MockRender(FooterComponent);
    fixture.detectChanges();
    const span = fixture.nativeElement.querySelector('span');
    expect(span.textContent).toContain(`Social Club ${DateTime.now().toFormat('yyyy')}`);
  });
});
