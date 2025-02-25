import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MemberEditorComponent} from './member-editor.component';
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {HarnessLoader} from "@angular/cdk/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Member} from "../models/member";
import {FormControl, FormGroup, ValidationErrors} from "@angular/forms";
import {DateTime} from "luxon";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";

describe('MemberEditorComponent', () => {
  let component: MemberEditorComponent;
  let fixture: ComponentFixture<MemberEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<Member> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_number: new FormControl<number>(0, {nonNullable: true}),
    first_name: new FormControl<string>('First', {nonNullable: true}),
    middle_name: new FormControl<string>('Middle', {nonNullable: true}),
    last_name: new FormControl<string>('Last', {nonNullable: true}),
    suffix: new FormControl<string>('Suffix', {nonNullable: true}),
    birth_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true}),
    joined_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true})
  })

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberEditorComponent],
      providers: [
          provideLuxonDateAdapter(),
          provideNoopAnimations(),
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberEditorComponent);
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

    it('should contain the correct number of member form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);

      expect(harnesses.length).toEqual(6);
    });

    describe('first name field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'First name'}))
      });
      
      it('should contain member field', async () => {
        expect(harness).toBeTruthy();
      });

      it('member form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.first_name.value);
      });

      it('first name should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('first name should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('first name should show error', async () => {
        formGroup.controls.first_name.markAsTouched()
        formGroup.controls.first_name.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('first name should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });

    });

    describe('middle name field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Middle name or initial'}))
      });

      it('should contain middle name field', async () => {
        expect(harness).toBeTruthy();
      });

      it('middle name form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.middle_name.value);
      });

      it('middle name should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('middle name should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('middle name should show error', async () => {
        formGroup.controls.middle_name.markAsTouched()
        formGroup.controls.middle_name.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('middle should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });

    });

    describe('last name field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Last name'}))
      });

      it('should contain last name field', async () => {
        expect(harness).toBeTruthy();
      });

      it('last name form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.last_name.value);
      });

      it('last name should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('last name should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('last name should show error', async () => {
        formGroup.controls.last_name.markAsTouched()
        formGroup.controls.last_name.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('last name should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('suffix field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Suffix'}))
      });

      it('should contain suffix field', async () => {
        expect(harness).toBeTruthy();
      });

      it('suffix form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.suffix.value);
      });

      it('suffix should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('suffix should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('suffix should show error', async () => {
        formGroup.controls.suffix.markAsTouched()
        formGroup.controls.suffix.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('suffix should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('birthdate field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Birth date'}))
      });

      it('should contain birthdate field', async () => {
        expect(harness).toBeTruthy();
      });

      it('birthdate form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        const shortDate = formGroup.controls.birth_date.value.toLocaleString(DateTime.DATE_SHORT);
        expect(value).toBe(shortDate);
      });

      it('birthdate should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('birthdate should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('birthdate should show error', async () => {
        formGroup.controls.birth_date.markAsTouched()
        formGroup.controls.birth_date.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('birthdate should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('joined date field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Joined date'}))
      });

      it('should contain joined date field', async () => {
        expect(harness).toBeTruthy();
      });

      it('joined date form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        const shortDate = formGroup.controls.joined_date.value.toLocaleString(DateTime.DATE_SHORT);
        expect(value).toBe(shortDate);
      });

      it('joined date should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('joined date should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('joined date should show error', async () => {
        formGroup.controls.joined_date.markAsTouched()
        formGroup.controls.joined_date.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('joined date should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });
  });
});
