import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormControl} from '@angular/forms';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {TranslateModule} from '@ngx-translate/core';

import {PhoneTypeFormFieldComponent, PhoneType} from '@app/features/phones';

describe('PhoneTypeFormFieldComponent', () => {
  let component: PhoneTypeFormFieldComponent;
  let fixture: ComponentFixture<PhoneTypeFormFieldComponent>;
  let phoneTypeControl: FormControl<PhoneType | null>;

  beforeEach(async () => {
    phoneTypeControl = new FormControl(null);

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
    const options = component.options();
    expect(options.length).toEqual(Object.entries(PhoneType).length);
  });

  it('should contain the correct option values', () => {
    const options = component.options();

    for ( const phoneType of Object.values(PhoneType)) {
      const values = options.map(v => v.value);
      expect(values).toContain(phoneType);
    }
  });
});
