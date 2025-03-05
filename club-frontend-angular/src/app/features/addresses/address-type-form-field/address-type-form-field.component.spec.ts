import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressTypeFormFieldComponent} from './address-type-form-field.component';
import {TranslateModule} from "@ngx-translate/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {FormControl} from "@angular/forms";
import {AddressType} from "../models/address-type";

describe('AddressTypeFormFieldComponent', () => {
  let component: AddressTypeFormFieldComponent;
  let fixture: ComponentFixture<AddressTypeFormFieldComponent>;
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

    for (let addressType of Object.values(AddressType)) {
      let values = options.map(v => v.value);
      expect(values).toContain(addressType);
    }
  });
});
