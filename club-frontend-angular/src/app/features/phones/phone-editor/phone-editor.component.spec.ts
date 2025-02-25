import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PhoneEditorComponent} from './phone-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Phone} from '../models/phone';
import {FormControl, FormGroup, ValidationErrors} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";

describe('PhoneEditorComponent', () => {
  let component: PhoneEditorComponent;
  let fixture: ComponentFixture<PhoneEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<Phone> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    country_code: new FormControl<string>('1', {nonNullable: true}),
    phone_number: new FormControl<string>('5555555555', {nonNullable: true}),
    phone_type: new FormControl<PhoneType | string>(PhoneType.Mobile, {nonNullable: true})
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhoneEditorComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhoneEditorComponent);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('form field tests', ()=> {
    const outline = 'outline';
    const fill = 'fill';
    const errors: ValidationErrors = [{p: 'error'}];
    let harness: MatFormFieldHarness | null;

    beforeEach(() => {
      formGroup.reset();
      component.editorForm = formGroup;
    });

    it('should contain the correct number of phone form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);

      expect(harnesses.length).toEqual(2);
    });
    
    describe('phone number field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Phone'}))
      });

      it('should contain phone field', async () => {
        expect(harness).toBeTruthy();
      });

      it('phone form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.phone_number.value);
      });

      it('phone should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('phone should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('phone should show error', async () => {
        formGroup.controls.phone_number.markAsTouched()
        formGroup.controls.phone_number.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('phone should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('phone type field tests', async () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Phone type'}))
      });

      it('should contain phone type field', async () => {
        expect(harness).toBeTruthy();
      });

      it('phone type should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.phone_type.value);
      });

      it('phone type should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('phone type should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('phone type should show error', async () => {
        formGroup.controls.phone_type.markAsTouched()
        formGroup.controls.phone_type.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('emails type should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });
  });

  describe('phone remove button and title tests', () => {
    let buttonHarness: MatButtonHarness | null;

    beforeEach(async () => {
      component.editorForm = formGroup;
    });

    it('should NOT show phone remove button when showRemoveButton is set to false', async () => {
      component.showRemoveButton = false;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeFalsy();
    });

    it('should show phone remove button when showRemoveButton is set to true', async () => {
      component.showRemoveButton = true;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeTruthy();
    });

    it('should call on remove when remove phone button is clicked', async () => {
      component.showRemoveButton = true;
      const spy = spyOn(component, 'onRemove').and.stub();

      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      await buttonHarness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when phone title is defined', async () => {
      component.title = 'test';

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeTruthy()
    });

    it('should display title when phone title is undefined', async () => {
      component.title = undefined;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeFalsy()
    });

    it('should display correct phone title', async () => {
      const title = 'title';
      component.title = title;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element.nativeElement.textContent).toBe(title);
    });
  });
});
