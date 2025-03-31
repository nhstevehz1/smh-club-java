import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {FormControl, FormGroup} from "@angular/forms";

import {MatFormFieldAppearance} from "@angular/material/form-field";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatButtonHarness} from "@angular/material/button/testing";

import {TranslateModule} from "@ngx-translate/core";

import {FormModelGroup} from "@app/shared/components/base-editor";
import {getFormFieldValue} from "@app/shared/testing";
import {EditorHeaderHarness} from "@app/shared/components/editor-header/testing";

import {EmailEditorComponent, Email, EmailType} from '@app/features/emails';

describe('EmailEditorComponent', () => {
  let component: EmailEditorComponent;
  let fixture: ComponentFixture<EmailEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<Email> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    email: new FormControl<string>('email@email.com', {nonNullable: true}),
    email_type: new FormControl<EmailType>(EmailType.Home, {nonNullable: true}),
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
          EmailEditorComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
          provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmailEditorComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('editorForm', formGroup);
    fixture.detectChanges()
    await fixture.whenStable();
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

    it('should contain the correct number of form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(2);
    });

    describe('email field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'emails.editor.email.label'}));
      });

      it('should contain email field', async () => {
        expect(harness).toBeTruthy();
      });

      it('email form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.email.value);
      });

      it('email should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('email should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('email type field tests', async () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'emails.editor.emailType.label'}));
      });

      it('should contain email type field', async () => {
        expect(harness).toBeTruthy();
      });

      it('email type should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe('emails.type.home');
      });

      it('email type should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('email type should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });
  });

  describe('email remove button and title tests', () => {
    let headerHarness: EditorHeaderHarness | null;

    beforeEach(async () => {
      fixture.componentRef.setInput('editorForm', formGroup);
      headerHarness = await loader.getHarnessOrNull(EditorHeaderHarness);
    });

    it('should contain one editor header', async () => {
      const harnesses = await loader.getAllHarnesses(EditorHeaderHarness);
      expect(harnesses.length).toEqual(1);
    });

     it('should NOT show email remove button when showRemoveButton is set to false', async () => {
        fixture.componentRef.setInput('showRemoveButton', false);
        const visible = await headerHarness?.isButtonVisible();
        expect(visible).not.toBeTrue();
      });

    it('should show email remove button when showRemoveButton is set to true', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const visible = await headerHarness?.isButtonVisible();
      expect(visible).toBeTrue();
    });

    it('should call on remove when remove email button is clicked', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const spy = spyOn(component, 'onRemove').and.stub();

      const button = await headerHarness?.getHarnessOrNull(MatButtonHarness);
      await button?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when email title is defined', async () => {
      fixture.componentRef.setInput('title', 'test');
      const visible = await headerHarness?.isTitleVisible();
      expect(visible).toBeTrue()
    });

    it('should NOT display title when email title is undefined', async () => {
      fixture.componentRef.setInput('title', undefined);
      const visible = await headerHarness?.isTitleVisible();
      expect(visible).not.toBeTrue();
    });

    it('should display correct email title', async () => {
      const title = 'title';
      fixture.componentRef.setInput('title', title);

      const titleText = await headerHarness?.titleText();
      expect(titleText).toBe(title);
    });
  });
});
