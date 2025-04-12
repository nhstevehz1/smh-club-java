import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewRenewalComponent} from './view-renewal.component';
import {Renewal} from '@app/features/renewals/models';
import {RenewalTest} from '@app/features/renewals/testing/test-support';
import {TranslateModule} from '@ngx-translate/core';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {DataDisplayHarness} from '@app/shared/components/data-display/testing/data-display-harness';
import {DateTime} from 'luxon';

describe('ViewRenewalComponent', () => {
  let component: ViewRenewalComponent;
  let fixture: ComponentFixture<ViewRenewalComponent>;
  let loader: HarnessLoader

  let renewal: Renewal;

  beforeEach(async () => {
    renewal = RenewalTest.generateRenewal();

    await TestBed.configureTestingModule({
      imports: [
        ViewRenewalComponent,
        TranslateModule.forRoot({})
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewRenewalComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  fit('should create', async () => {
    fixture.componentRef.setInput('renewal', renewal);

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  fit('should display year', async () => {
    fixture.componentRef.setInput('renewal', renewal);
    const str = renewal.renewal_year.toString();

    const harness
      = await loader.getHarnessOrNull(DataDisplayHarness.with({value: str}));

    expect(harness).toBeTruthy();
  });

  fit('year field should NOT display label', async () => {
    fixture.componentRef.setInput('renewal', renewal);
    const str = renewal.renewal_year.toString();

    const harness
      = await loader.getHarness(DataDisplayHarness.with({value: str}));
    const label = await harness?.getLabel();

    expect(label).toBeFalsy();
  });

  fit('should display renewal date', async () => {
    fixture.componentRef.setInput('renewal', renewal);
    const dtString =
      renewal.renewal_date.toLocaleString(DateTime.DATE_SHORT, {locale: 'en'})

    const harness
      = await loader.getHarnessOrNull(DataDisplayHarness.with({value: dtString}));

    expect(harness).toBeTruthy();
  });

  fit('renewal date filed should NOT should display label', async () => {
    fixture.componentRef.setInput('renewal', renewal);
    const dtString =
      renewal.renewal_date.toLocaleString(DateTime.DATE_SHORT, {locale: 'en'})

    const harness
      = await loader.getHarness(DataDisplayHarness.with({value: dtString}));
    const label = await harness.getLabel();

    expect(label).toBeFalsy();
  });
});
