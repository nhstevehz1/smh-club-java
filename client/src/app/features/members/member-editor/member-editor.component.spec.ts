import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormControl, FormGroup} from '@angular/forms';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {provideLuxonDateAdapter} from '@angular/material-luxon-adapter';
import {MatFormFieldAppearance} from '@angular/material/form-field';

import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatFormFieldHarness} from '@angular/material/form-field/testing';

import {DateTime} from 'luxon';
import {TranslateModule} from '@ngx-translate/core';

import {FormModelGroup} from '@app/shared/components/base-editor/models';

import {MemberEditorComponent} from './member-editor.component';
import {Member} from '@app/features/members/models/member';
import {TestHelpers} from '@app/shared/testing';

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
      imports: [
          MemberEditorComponent,
          TranslateModule.forRoot({})
      ],
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
    const outline: MatFormFieldAppearance = 'outline';
    const fill: MatFormFieldAppearance = 'fill';
    let harness: MatFormFieldHarness | null;

    beforeEach(() => {
      formGroup.reset();
      fixture.componentRef.setInput('editorForm', formGroup);
    });

    it('should contain the correct number of member form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(7);
    });

    describe('member number field tests', () => {
      beforeEach(async () => {
        harness =
          await loader.getHarnessOrNull(MatFormFieldHarness.with({
            floatingLabelText: 'members.editor.memberNumber.label'}))
      });

      it('should contain member number field', async () => {
        expect(harness).toBeTruthy();
      });

      it('member number form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.member_number.value.toString());
      });

      it('member number should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('member number should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('first name field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.firstName.label'}))
      });

      it('should contain first name field', async () => {
        expect(harness).toBeTruthy();
      });

      it('first name form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.first_name.value);
      });

      it('first name should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('first name should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('middle name field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.middleName.label'}))
      });

      it('should contain middle name field', async () => {
        expect(harness).toBeTruthy();
      });

      it('middle name form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.middle_name.value);
      });

      it('middle name should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('middle name should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

    });

    describe('last name field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.lastName.label'}))
      });

      it('should contain last name field', async () => {
        expect(harness).toBeTruthy();
      });

      it('last name form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.last_name.value);
      });

      it('last name should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('last name should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

    });

    describe('suffix field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.suffix.label'}))
      });

      it('should contain suffix field', async () => {
        expect(harness).toBeTruthy();
      });

      it('suffix form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.suffix.value);
      });

      it('suffix should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('suffix should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('birthdate field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.birthDate.label'}))
      });

      it('should contain birthdate field', async () => {
        expect(harness).toBeTruthy();
      });

      it('birthdate form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        const shortDate = formGroup.controls.birth_date.value.toLocaleString(DateTime.DATE_SHORT);
        expect(value).toBe(shortDate);
      });

      it('birthdate should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('birthdate should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('joined date field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.birthDate.label'}));
      });

      it('should contain joined date field', async () => {
        expect(harness).toBeTruthy();
      });

      it('joined date form field should contain the correct value', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        const shortDate = formGroup.controls.joined_date.value.toLocaleString(DateTime.DATE_SHORT);
        expect(value).toBe(shortDate);
      });

      it('joined date should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('joined date should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });
  });
});
