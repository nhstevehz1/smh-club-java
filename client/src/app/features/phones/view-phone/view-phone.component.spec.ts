import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewPhoneComponent} from './view-phone.component';
import {Phone, PhoneType} from '@app/features/phones/models';
import {PhoneTest} from '@app/features/phones/testing';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatIconHarness} from '@angular/material/icon/testing';
import {DataDisplayHarness} from '@app/shared/components/data-display/testing/data-display-harness';
import {TranslateModule} from '@ngx-translate/core';

describe('ViewPhoneComponent', () => {
  let component: ViewPhoneComponent;
  let fixture: ComponentFixture<ViewPhoneComponent>;
  let loader: HarnessLoader;

  let phone: Phone;

  beforeEach(async () => {
    phone = PhoneTest.generatePhone();

    await TestBed.configureTestingModule({
      imports: [
        ViewPhoneComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewPhoneComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('phone', phone);

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  it('should display an icon', async () => {
    fixture.componentRef.setInput('phone', phone);
    const harness = await loader.getHarnessOrNull(MatIconHarness);
    expect(harness).toBeTruthy();
  });

  it('should display the home icon', async () => {
    phone.phone_type = PhoneType.Home;
    fixture.componentRef.setInput('phone', phone);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('home');
  });

  it('should display the work icon', async () => {
    phone.phone_type = PhoneType.Work;
    fixture.componentRef.setInput('phone', phone);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('corporate_fare');
  });

  it('should display the mobile icon', async () => {
    phone.phone_type = PhoneType.Mobile;
    fixture.componentRef.setInput('phone', phone);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();

    expect(name).toEqual('smartphone');
  });

  it('should display phone number', async () => {
    fixture.componentRef.setInput('phone', phone);
    const str = `+${phone.country_code} ${phone.phone_number}`;

    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: str}));

    expect(harness).toBeTruthy();
  });

  it('phone number field should NOT contain a label', async () => {
    fixture.componentRef.setInput('phone', phone);
    const str = `+${phone.country_code} ${phone.phone_number}`;

    const harness = await
      loader.getHarness(DataDisplayHarness.with({value: str}));
    const label = await harness.getLabel();

    expect(label).toBeFalsy();
  });
});
