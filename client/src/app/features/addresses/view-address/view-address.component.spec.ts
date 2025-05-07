import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewAddressComponent} from './view-address.component';
import {Address, AddressType} from '@app/features/addresses/models';
import {AddressTest} from '@app/features/addresses/testing';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatIconHarness} from '@angular/material/icon/testing';
import {DataDisplayHarness} from '@app/shared/components/data-display/testing/data-display-harness';
import {TranslateModule} from '@ngx-translate/core';

describe('ViewAddressComponent', () => {
  let component: ViewAddressComponent;
  let fixture: ComponentFixture<ViewAddressComponent>;
  let loader: HarnessLoader;

  let address: Address;

  beforeEach(async () => {
    address = AddressTest.generateAddress();

    await TestBed.configureTestingModule({
      imports: [
        ViewAddressComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAddressComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('address', address);

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  it('should display an icon', async () => {
    fixture.componentRef.setInput('address', address);
    const harness = await loader.getHarnessOrNull(MatIconHarness);
    expect(harness).toBeTruthy();
  });

  it('should display the home icon', async () => {
    address.address_type = AddressType.Home;
    fixture.componentRef.setInput('address', address);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('home');
  });

  it('should display the work icon', async () => {
    address.address_type = AddressType.Work;
    fixture.componentRef.setInput('address', address);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('corporate_fare');
  });

  it('should display the other icon', async () => {
    address.address_type = AddressType.Other;
    fixture.componentRef.setInput('address', address);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('question_mark');
  });

  it('should display address1', async () => {
    fixture.componentRef.setInput('address', address);
    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: address.address1}));
    expect(harness).toBeTruthy();
  });

  it('address1 field should NOT have a label', async () => {
    fixture.componentRef.setInput('address', address);

    const harness = await
      loader.getHarness(DataDisplayHarness.with({value: address.address1}));
    const label = await harness.getLabel();

    expect(label).toBeFalsy();
  });

  it('should display address2 when address2 is defined', async () => {
    address.address2 = 'my address2';
    fixture.componentRef.setInput('address', address);
    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: address.address2}));
    expect(harness).toBeTruthy();
  });

  it('address 2 should not have a label', async () => {
    address.address2 = 'my address2';
    fixture.componentRef.setInput('address', address);

    const harness = await
      loader.getHarness(DataDisplayHarness.with({value: address.address2}));
    const label = await harness.getLabel();

    expect(label).toBeFalsy();
  });

  it('should NOT display address2 when address2 is NOT defined', async () => {
    address.address2 = '';
    fixture.componentRef.setInput('address', address);

    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: address.address2}));

    expect(harness).toBeFalsy();
  });

  it('should display city, state, postal code', async () => {
    fixture.componentRef.setInput('address', address);
    const str = `${address.city}, ${address.state} ${address.postal_code}`;

    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: str}));

    expect(harness).toBeTruthy();
  });

  it('city, state, postal code field should have a label', async () => {
    fixture.componentRef.setInput('address', address);
    const str = `${address.city}, ${address.state} ${address.postal_code}`;

    const harness = await
      loader.getHarness(DataDisplayHarness.with({value: str}));
    const label = await harness.getLabel();

    expect(label).toBeFalsy();
  });
});
