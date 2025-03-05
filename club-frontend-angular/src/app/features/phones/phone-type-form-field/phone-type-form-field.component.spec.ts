import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PhoneTypeFormFieldComponent} from './phone-type-form-field.component';
import {FormControl, Validators} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {TranslateModule} from "@ngx-translate/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";

describe('PhoneTypeFormFieldComponent', () => {
  let component: PhoneTypeFormFieldComponent;
  let fixture: ComponentFixture<PhoneTypeFormFieldComponent>;
  let phoneTypeControl: FormControl<PhoneType | null>;

  beforeEach(async () => {
    phoneTypeControl = new FormControl<PhoneType | null>(null);
    phoneTypeControl.setValidators(Validators.required);

    await TestBed.configureTestingModule({
      imports: [
          PhoneTypeFormFieldComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhoneTypeFormFieldComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('formControl', phoneTypeControl);
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain the correct number of options',  () => {
    const options = component.optionsSignal();
    expect(options.length).toEqual(Object.entries(PhoneType).length);
  });

  it('should contain the correct option values', () => {
    const options = component.optionsSignal();

    for ( let phoneType of Object.values(PhoneType)) {
      let values = options.map(v => v.value);
      expect(values).toContain(phoneType);
    }
  });
});
