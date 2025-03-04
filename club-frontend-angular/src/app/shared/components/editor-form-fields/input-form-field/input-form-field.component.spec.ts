import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InputFormFieldComponent} from './input-form-field.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {FormControl} from "@angular/forms";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatInputHarness} from "@angular/material/input/testing";
import {InputType} from "../models/input-type";

describe('InputFormFieldComponent', () => {
  let component: InputFormFieldComponent;
  let fixture: ComponentFixture<InputFormFieldComponent>;
  let loader: HarnessLoader;

  let stringControl: FormControl<string>;
  let numberControl: FormControl<number>;

  beforeEach(async () => {
    stringControl = new FormControl<string>('test', {nonNullable: true});
    numberControl = new FormControl<number>(10, {nonNullable: true});


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
      const val = 'fill'
      fixture.componentRef.setInput('appearance', val);
      const appearance = await harness.getAppearance();
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
});
