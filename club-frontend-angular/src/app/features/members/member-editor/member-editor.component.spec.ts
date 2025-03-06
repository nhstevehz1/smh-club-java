import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MemberEditorComponent} from './member-editor.component';
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {HarnessLoader} from "@angular/cdk/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Member} from "../models/member";
import {FormControl, FormGroup} from "@angular/forms";
import {DateTime} from "luxon";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {TranslateModule} from "@ngx-translate/core";
import {MatButtonHarness} from "@angular/material/button/testing";
import {
  EditorHeaderHarness,
  TitleHarness
} from "../../../shared/components/editor-header/test-support/editor-header-harness";

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
      expect(harnesses.length).toEqual(6);
    });

    describe('first name field tests', () => {

      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'members.editor.firstName.label'}))
      });
      
      it('should contain member field', async () => {
        expect(harness).toBeTruthy();
      });

      it('member form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
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
        const value = await getFormFieldValue(harness);
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
        const value = await getFormFieldValue(harness);
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
        const value = await getFormFieldValue(harness);
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
        const value = await getFormFieldValue(harness);
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
        const value = await getFormFieldValue(harness);
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

  describe('member remove button and title tests', () => {
    let headerHarness: EditorHeaderHarness | null;

    beforeEach(async () => {
      fixture.componentRef.setInput('editorForm', formGroup);
      headerHarness = await loader.getHarnessOrNull(EditorHeaderHarness);
    });

    fit('should contain an editor header', async () => {
      expect(headerHarness).toBeTruthy()
    });

    describe('title tests', async () => {
      let titleHarness: TitleHarness | null | undefined;

      fit('member should display title when title is defined', async () => {
        fixture.componentRef.setInput('title', 'test');
        titleHarness = await headerHarness?.title();
        expect(titleHarness).toBeTruthy()
      });

      fit('member should NOT display title when title is undefined', async () => {
        fixture.componentRef.setInput('title', undefined);
        titleHarness = await headerHarness?.title();
        expect(titleHarness).toBeFalsy()
      });

      fit('member should display correct title', async () => {
        const title = 'title';
        fixture.componentRef.setInput('title', title);
        titleHarness = await headerHarness?.title();
        const titleText = await titleHarness?.titleLabel();
        expect(titleText).toBe(title);
      });
    });

    describe('member remove button tests', () => {
      let buttonHarness: MatButtonHarness | null | undefined;

      fit('should NOT show remove button when showRemoveButton is set to false', async () => {
        fixture.componentRef.setInput('showRemoveButton', false);
        buttonHarness = await headerHarness?.removeButton();
        expect(buttonHarness).toBeFalsy();
      });

      fit('should show remove button when showRemoveButton is set to true', async () => {
        fixture.componentRef.setInput('showRemoveButton', true);
        buttonHarness = await headerHarness?.removeButton();
        expect(buttonHarness).toBeTruthy();
      });

      fit('member should call on remove when remove button is clicked', async () => {
        fixture.componentRef.setInput('showRemoveButton', true);
        const spy = spyOn(component, 'onRemove').and.stub();

        buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
        await buttonHarness?.click();

        expect(spy).toHaveBeenCalled();
      });
    });
  });
});
