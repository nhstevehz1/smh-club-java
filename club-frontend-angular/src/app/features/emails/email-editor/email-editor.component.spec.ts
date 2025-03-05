import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EmailEditorComponent} from './email-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {FormControl, FormGroup} from "@angular/forms";
import {EmailType} from "../models/email-type";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Email} from "../models/email";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {MatFormFieldAppearance} from "@angular/material/form-field";

describe('EmailEditorComponent', () => {
  let component: EmailEditorComponent;
  let fixture: ComponentFixture<EmailEditorComponent>;
  let loader: HarnessLoader;

  let formGroup: FormModelGroup<Email> = new FormGroup({
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
    let buttonHarness: MatButtonHarness | null;

    beforeEach(async () => {
      fixture.componentRef.setInput('editorForm', formGroup);
    });

   it('should NOT show email remove button when showRemoveButton is set to false', async () => {
      fixture.componentRef.setInput('showRemoveButton', false);
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeFalsy();
    });

    it('should show email remove button when showRemoveButton is set to true', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeTruthy();
    });

    it('should call on remove when remove email button is clicked', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const spy = spyOn(component, 'onRemove').and.stub();

      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      await buttonHarness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when email title is defined', async () => {
      fixture.componentRef.setInput('title', 'test');

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeTruthy()
    });

    it('should NOT display title when email title is undefined', async () => {
      fixture.componentRef.setInput('title', undefined);

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeFalsy()
    });

    it('should display correct email title', async () => {
      const title = 'title';
      fixture.componentRef.setInput('title', title);

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element.nativeElement.textContent).toBe(title);
    });
  });
});
