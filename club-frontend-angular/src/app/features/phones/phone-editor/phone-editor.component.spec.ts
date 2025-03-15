import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PhoneEditorComponent} from './phone-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {PhoneUpdate} from '../models/phone';
import {FormControl, FormGroup} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {MatButtonHarness} from "@angular/material/button/testing";
import {TranslateModule} from "@ngx-translate/core";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {EditorHeaderHarness} from "../../../shared/components/editor-header/test-support/editor-header-harness";

describe('PhoneEditorComponent', () => {
  let component: PhoneEditorComponent;
  let fixture: ComponentFixture<PhoneEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<PhoneUpdate> = new FormGroup({
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

  it('should create', async () => {
    fixture.componentRef.setInput('editorForm', formGroup);
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should contain editor header', async () => {
    fixture.componentRef.setInput('editorForm', formGroup);
    const header = await loader.getAllHarnesses(EditorHeaderHarness);
    expect(header.length).toEqual(1);
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

    it('should contain the correct number of phone form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(3);
    });

    describe('country code field tests', () => {
      beforeEach(async () => {
        harness =
            await  loader.getHarnessOrNull((MatFormFieldHarness.with(
                {floatingLabelText: 'phones.editor.countryCode.label'})));
      });

      it('should contain country code field', async () => {
        expect(harness).toBeTruthy();
      });

      it('country code form field should contain the correct value', async () => {
        expected = 'test';
        formGroup.controls.country_code.setValue(expected);
        actual = await getFormFieldValue(harness);
        expect(actual).toBe(expected);
      });

      it('country code should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('country code should use fill appearance', async() => {
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

      it('should contain phone field', async () => {
        expect(harness).toBeTruthy();
      });

      it('phone form field should contain the correct value', async () => {
        expected = 'test'
        formGroup.controls.phone_number.setValue(expected);
        actual = await getFormFieldValue(harness);
        expect(actual).toBe(expected);
      });

      it('phone should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('phone should use fill appearance', async() => {
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

      it('should contain phone type field', async () => {
        expect(harness).toBeTruthy();
      });

      it('phone type should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe('phones.type.mobile');
      });

      it('phone type should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('phone type should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

    });
  });

  describe('phone header tests', () => {
    let headerHarness: EditorHeaderHarness | null;

    beforeEach(async () => {
      fixture.componentRef.setInput('editorForm', formGroup);
      headerHarness = await loader.getHarnessOrNull(EditorHeaderHarness);
    });

    it('should contain one editor header', async () => {
      const harnesses = await loader.getAllHarnesses(EditorHeaderHarness);
      expect(harnesses.length).toEqual(1);
    });

    it('should NOT show phone remove button when showRemoveButton is set to false', async () => {
      fixture.componentRef.setInput('showRemoveButton', false);
      const visible = await headerHarness?.isButtonVisible();
      expect(visible).not.toBeTrue();
    });

    it('should show phone remove button when showRemoveButton is set to true', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const visible = await headerHarness?.isButtonVisible();
      expect(visible).toBeTrue();
    });

    it('should call on remove when remove phone button is clicked', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const spy = spyOn(component, 'onRemove').and.stub();

      const harness = await headerHarness?.getHarness(MatButtonHarness);
      await harness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when phone title is defined', async () => {
      fixture.componentRef.setInput('title', 'test');
      const visible = await headerHarness?.isTitleVisible();
      expect(visible).toBeTrue();
    });

    it('should NOT display title when phone title is undefined', async () => {
      fixture.componentRef.setInput('title', undefined);

      const visible = await headerHarness?.isTitleVisible();
      expect(visible).not.toBeTrue();
    });

    it('should display correct phone title', async () => {
      const title= 'title';
      fixture.componentRef.setInput('title', title);

      const titleText = await headerHarness?.titleText();
      expect(titleText).toBe(title);
    });
  });
});
