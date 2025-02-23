import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EmailEditorComponent} from './email-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {FormControl, FormGroup, ValidationErrors} from "@angular/forms";
import {EmailType} from "../models/email-type";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Email} from "../models/email";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";

describe('EmailEditorComponent', () => {
  let component: EmailEditorComponent;
  let fixture: ComponentFixture<EmailEditorComponent>;
  let loader: HarnessLoader;
  let formGroup: FormModelGroup<Email> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    email: new FormControl<string>('email@email.com', {nonNullable: true}),
    email_type: new FormControl<EmailType | string>(EmailType.Home, {nonNullable: true}),
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmailEditorComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmailEditorComponent);
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

    it('should contain the correct number of form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);

      expect(harnesses.length).toEqual(2);
    });

    describe('email field tests', () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Email'}))
      });

      it('should contain email field', async () => {
        expect(harness).toBeTruthy();
      });

      it('email form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.email.value);
      });

      it('email should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('email should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('emails should show error', async () => {
        formGroup.controls.email.markAsTouched()
        formGroup.controls.email.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('emails should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('email type field tests', async () => {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Email type'}))
      });

      it('should contain email type field', async () => {
        expect(harness).toBeTruthy();
      });

      it('email type should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.email_type.value);
      });

      it('email type should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('email type should use fill appearance', async() => {
        component.fieldAppearance = fill;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('email type should show error', async () => {
        formGroup.controls.email.markAsTouched()
        formGroup.controls.email.setErrors(errors);

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

  describe('email remove button and title tests', () => {
    let buttonHarness: MatButtonHarness | null;

    beforeEach(async () => {
      component.editorForm = formGroup;
    });

   it('should NOT show email remove button when showRemoveButton is set to false', async () => {
      component.showRemoveButton = false;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeFalsy();
    });

    it('should show email remove button when showRemoveButton is set to true', async () => {
      component.showRemoveButton = true;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeTruthy();
    });

    it('should call on remove when remove email button is clicked', async () => {
      component.showRemoveButton = true;
      const spy = spyOn(component, 'onRemove').and.stub();

      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      await buttonHarness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when email title is defined', async () => {
      component.title = 'test';

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeTruthy()
    });

    it('should display title when email title is undefined', async () => {
      component.title = undefined;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeFalsy()
    });

    it('should display correct email title', async () => {
      const title = 'title';
      component.title = title;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element.nativeElement.textContent).toBe(title);
    });
  });
});
