import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InputFormFieldComponent} from './input-form-field.component';
import {MatFormFieldAppearance, MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {FormControl, Validators} from "@angular/forms";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatInputHarness} from "@angular/material/input/testing";
import {InputType} from "../models/input-type";
import {FormControlError} from "../models/form-control-error";
import {By} from "@angular/platform-browser";

describe('InputFormFieldComponent', () => {
  let component: InputFormFieldComponent;
  let fixture: ComponentFixture<InputFormFieldComponent>;
  let loader: HarnessLoader;

  let stringControl: FormControl<string | null>;
  let numberControl: FormControl<number | null>;

  beforeEach(async () => {
    stringControl = new FormControl<string | null>(null);
    stringControl.setValidators([Validators.required, Validators.email]);
    numberControl = new FormControl<number | null>(null);

    await TestBed.configureTestingModule({
      imports: [
        InputFormFieldComponent,
        MatFormFieldModule,
        MatInputModule,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
        TranslatePipe
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputFormFieldComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('formControl', stringControl);
    fixture.detectChanges()
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should contain one mat-form-field', async () => {
    fixture.componentRef.setInput('formControl', stringControl);
    const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
    expect(harnesses.length).toEqual(1);
  });

  it('should contain one input field', async () => {
    fixture.componentRef.setInput('formControl', stringControl);
    const harnesses = await loader.getAllHarnesses(MatInputHarness);
    expect(harnesses.length).toEqual(1);
  });

  describe('input base form fields tests', ()=> {
    let harness: MatFormFieldHarness;

    beforeEach(async () => {
      fixture.componentRef.setInput('formControl', stringControl);
      harness = await loader.getHarness(MatFormFieldHarness);
    });

    it('input should show label when label value is defined', async () => {
      fixture.componentRef.setInput('label', 'test');
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeTrue();
    });

    it('input should NOT show label by default', async () => {
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeFalse();
    });

    it('input should show correct label', async () => {
      const val = 'test'
      fixture.componentRef.setInput('label', val);
      const label = await harness.getLabel();
      expect(label).toBe(val);
    });

    it('input should have outline appearance by default', async () => {
      const appearance = await harness.getAppearance();
      expect(appearance).toBe('outline');
    });

    it('input should use the correct appearance', async () => {
      const val = 'fill' as MatFormFieldAppearance;
      fixture.componentRef.setInput('appearance', val);
      const appearance = await harness.getAppearance();
      console.debug(component.appearance());
      expect(appearance).toBe(val);
    });

  });

  describe('input field tests', () => {
    let harness: MatInputHarness;

    beforeEach(async () => {
      fixture.componentRef.setInput('formControl', stringControl);
      harness = await loader.getHarness(MatInputHarness);
    });

    it('should have text as the default type', async () => {
      const type = await harness.getType();
      expect(type).toBe('text');
    });

    it('should use the correct type', async () => {
      fixture.componentRef.setInput('inputType', InputType.Email);
      const type = await harness.getType();
      expect(type).toBe('email');
    });

  });

  describe('input field value tests', () => {
    let harness: MatInputHarness;

    it('value should be text', async () => {
      const test = 'this is a test';
      stringControl.setValue(test);
      fixture.componentRef.setInput('formControl', stringControl);
      harness = await loader.getHarness(MatInputHarness);

      const val = await harness.getValue();
      expect(val).toBe(test);
    });

    it('value should be numeric', async () => {
      const test = 100;
      numberControl.setValue(test);
      fixture.componentRef.setInput('formControl', numberControl);
      harness = await loader.getHarness(MatInputHarness);

      const val = await harness.getValue();
      expect(Number(val)).toEqual(test);
    });
  });

  describe('error tests, validators present', ()=> {
    let harness: MatInputHarness;
    const errors: FormControlError[]  = [
      {type: 'required', message:'required message'}
    ]
    beforeEach( async () => {
      fixture.componentRef.setInput('formControl', stringControl);
      harness = await loader.getHarness(MatInputHarness);
    });

    it('input should NOT show error by default', async () => {
      fixture.componentRef.setInput('controlErrors', errors);

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeFalsy();
    });

    it('input should NOT show error on blur when value is valid', async () => {
      fixture.componentRef.setInput('controlErrors', errors);
      stringControl.setValue('test@test.com');

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeFalsy();
    });

    it('input should not show error on blur when control has no validators', async () => {
      fixture.componentRef.setInput('formControl', numberControl);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeFalsy();
    })

    it('should should show error on blur', async() => {
      fixture.componentRef.setInput('controlErrors', errors);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeTruthy();
    });

    it('input should show correct error message', async() => {
      fixture.componentRef.setInput('controlErrors', errors);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element.nativeElement.textContent).toBe(errors[0].message);
    });
  });
});
