import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {FormControl} from '@angular/forms';

import {TranslateModule} from '@ngx-translate/core';

import {EmailTypeFormFieldComponent, EmailType} from '@app/features/emails';

describe('EmailTypeFormFieldComponent', () => {
  let component: EmailTypeFormFieldComponent;
  let fixture: ComponentFixture<EmailTypeFormFieldComponent>;
  let emailTypeControl: FormControl<EmailType | null>;

  beforeEach(async () => {
    emailTypeControl = new FormControl(null);

    await TestBed.configureTestingModule({
      imports: [
          EmailTypeFormFieldComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmailTypeFormFieldComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('formControl', emailTypeControl);
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain the correct number of options', () => {
    const options = component.options();
    expect(options.length).toEqual(Object.entries(EmailType).length);
  });

  it('should contain the correct option values', () => {
    const options = component.options();

    for ( const emailType of Object.values(EmailType)) {
      const values = options.map(v => v.value);
      expect(values).toContain(emailType);
    }
  });
});
