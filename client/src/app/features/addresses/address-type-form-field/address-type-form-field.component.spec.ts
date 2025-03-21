import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressTypeFormFieldComponent} from './address-type-form-field.component';
import {TranslateModule} from "@ngx-translate/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {FormControl} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatSelectHarness} from "@angular/material/select/testing";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatFormFieldAppearance} from "@angular/material/form-field";

describe('AddressTypeFormFieldComponent', () => {
  let component: AddressTypeFormFieldComponent;
  let fixture: ComponentFixture<AddressTypeFormFieldComponent>;
  let loader: HarnessLoader;

  let addressTypeControl: FormControl<AddressType | null>;

  beforeEach(async () => {
    addressTypeControl = new FormControl(null);

    await TestBed.configureTestingModule({
      imports: [
          AddressTypeFormFieldComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressTypeFormFieldComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('formControl', addressTypeControl);
    fixture.detectChanges();
    await fixture.whenStable();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain the correct number of options',  () => {
    const options = component.optionsSignal();
    expect(options.length).toEqual(Object.entries(AddressType).length);
  });

  it('should contain the correct option values', () => {
    const options = component.optionsSignal();

    for (const addressType of Object.values(AddressType)) {
      const values = options.map(v => v.value);
      expect(values).toContain(addressType);
    }
  });

  it('should contain a select form field', async () => {
    const harness = await loader.getHarnessOrNull(MatSelectHarness);
    expect(harness).toBeTruthy();
  });

  describe('form field tests', () => {
    let harness: MatFormFieldHarness;

    beforeEach(async () => {
      harness = await loader.getHarness(MatFormFieldHarness);
    });

    it('should set appearance to outline', async () => {
      const outline: MatFormFieldAppearance = 'outline';
      fixture.componentRef.setInput('appearance', outline);
      const appearance = await harness.getAppearance();
      expect(appearance).toBe(outline);
    });

    it('should set appearance to fill', async () => {
      const fill: MatFormFieldAppearance = 'fill';
      fixture.componentRef.setInput('appearance', fill);
      const appearance = await harness.getAppearance();
      expect(appearance).toBe(fill);
    });
  });
});
