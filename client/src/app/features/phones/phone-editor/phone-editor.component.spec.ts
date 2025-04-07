import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormControl, FormGroup} from '@angular/forms';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {MatFormFieldAppearance} from '@angular/material/form-field';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatFormFieldHarness} from '@angular/material/form-field/testing';

import {TranslateModule} from '@ngx-translate/core';

import {FormModelGroup} from '@app/shared/components/base-editor/models';

import {Phone, PhoneType} from '@app/features/phones/models/phone';
import {PhoneEditorComponent} from './phone-editor.component';
import {TestHelpers} from '@app/shared/testing';

describe('PhoneEditorComponent', () => {
  let component: PhoneEditorComponent;
  let fixture: ComponentFixture<PhoneEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<Phone> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    country_code: new FormControl<string>('1', {nonNullable: true}),
    phone_number: new FormControl<string>('5555555555', {nonNullable: true}),
    phone_type: new FormControl<PhoneType>(PhoneType.Mobile, {nonNullable: true})
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
          PhoneEditorComponent,
          TranslateModule.forRoot({})
      ],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhoneEditorComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);


  });

  fit('should create', async () => {
    fixture.componentRef.setInput('editorForm', formGroup);
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('form field tests', ()=> {
    const outline: MatFormFieldAppearance = 'outline';
    const fill: MatFormFieldAppearance = 'fill';
    let harness: MatFormFieldHarness | null;
    let expected: string;
    let actual: string;

    beforeEach(() => {
      formGroup.reset();
      fixture.componentRef.setInput('editorForm', formGroup);
    });

    fit('should contain the correct number of phone form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(3);
    });

    describe('country code field tests', () => {
      beforeEach(async () => {
        harness =
            await  loader.getHarnessOrNull((MatFormFieldHarness.with(
                {floatingLabelText: 'phones.editor.countryCode.label'})));
      });

      fit('should contain country code field', async () => {
        expect(harness).toBeTruthy();
      });

      fit('country code form field should contain the correct value', async () => {
        expected = 'test';
        formGroup.controls.country_code.setValue(expected);
        actual = await TestHelpers.getFormFieldValue(harness);
        expect(actual).toBe(expected);
      });

      fit('country code should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      fit('country code should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('phone number field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with(
                {floatingLabelText: 'phones.editor.phoneNumber.label'}));
      });

      fit('should contain phone field', async () => {
        expect(harness).toBeTruthy();
      });

      fit('phone form field should contain the correct value', async () => {
        expected = 'test'
        formGroup.controls.phone_number.setValue(expected);
        actual = await TestHelpers.getFormFieldValue(harness);
        expect(actual).toBe(expected);
      });

      fit('phone should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      fit('phone should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('phone type field tests', async () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'phones.editor.phoneType.label'}))
      });

      fit('should contain phone type field', async () => {
        expect(harness).toBeTruthy();
      });

      fit('phone type should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe('phones.type.mobile');
      });

      fit('phone type should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      fit('phone type should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

    });
  });
});
