import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SelectFormFieldComponent} from './select-form-field.component';
import {TestStringEnum} from "./test-support/test-support";
import {FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {SelectOption} from "../models/select-option";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatSelectHarness} from "@angular/material/select/testing";
import {By} from "@angular/platform-browser";
import {FormControlError} from "../models/form-control-error";

describe('SelectFormFieldComponent', () => {
  let component: SelectFormFieldComponent<TestStringEnum>;
  let fixture: ComponentFixture<SelectFormFieldComponent<TestStringEnum>>;
  let loader: HarnessLoader;

  let textControl: FormControl<TestStringEnum | null>;

  const stringOptions: SelectOption<TestStringEnum>[] = [
    {label: 'Test1', value: TestStringEnum.Test1},
    {label: 'Test2', value: TestStringEnum.Test2},
    {label: 'Test3', value: TestStringEnum.Test3}
  ];

  beforeEach(async () => {
    textControl = new FormControl<TestStringEnum | null>(null);
    textControl.setValidators(Validators.required);

    await TestBed.configureTestingModule({
      imports: [
        SelectFormFieldComponent,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
        TranslatePipe
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectFormFieldComponent<TestStringEnum>);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);

    fixture.componentRef.setInput('formControl', textControl);
    fixture.componentRef.setInput('options', stringOptions);
  });

 it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('select should contain one mat-form-field', async () => {
    const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
    expect(harnesses.length).toEqual(1);
  });

  it('should contain one select field', async () => {
    const harnesses = await loader.getAllHarnesses(MatSelectHarness);
    expect(harnesses.length).toEqual(1);
  });

  describe('select base form field tests', () => {
    let harness: MatFormFieldHarness;

    beforeEach(async () => {
      harness = await loader.getHarness(MatFormFieldHarness);
    });

    it('select should show label when label value is defined', async () => {
      fixture.componentRef.setInput('label', 'test');
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeTrue();
    });

    it('select should NOT show label by default', async () => {
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeFalse();
    });

    it('select should show correct label', async () => {
      const val = 'test'
      fixture.componentRef.setInput('label', val);
      const label = await harness.getLabel();
      expect(label).toBe(val);
    });

    it('select should have outline appearance by default', async () => {
      const appearance = await harness.getAppearance();
      expect(appearance).toBe('outline');
    });

    it('select should use the correct appearance', async () => {
      const val = 'fill'
      fixture.componentRef.setInput('appearance', val);
      const appearance = await harness.getAppearance();
      expect(appearance).toBe(val);
    });
  });

  describe('select field tests', () => {
    let harness: MatSelectHarness

    beforeEach(async () => {
      harness = await loader.getHarness(MatSelectHarness);
      // need to call open before examining the options list.
      await harness.open();
    });

    it('should contain the correct number of options', async () => {
      const options = await harness.getOptions();
      expect(options.length).toEqual(stringOptions.length);
    });

    it('should contain the correct labels', async() => {
      const options = await harness.getOptions();

      for(let ii = 0; ii < options.length; ii++) {
        const option = options[ii];
        const label = await option.getText();
        expect(label).toBe(stringOptions[ii].label);
      }
    });
  });

  describe('select error tests', () => {
    let harness: MatSelectHarness;
    const errors: FormControlError[]  = [
      {type: 'required', message:'required message'}
    ]

    beforeEach(async () => {
      harness = await loader.getHarness(MatSelectHarness);
    });

    it('select should NOT show error on blur when value is valid', async () => {
      fixture.componentRef.setInput('controlErrors', errors);
      textControl.setValue(TestStringEnum.Test1);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeFalsy();
    });

   it('select should show error on blur', async() => {
      fixture.componentRef.setInput('controlErrors', errors);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element).toBeTruthy();
    });

    it('select should show correct error message', async() => {
      fixture.componentRef.setInput('controlErrors', errors);

      await harness.blur();

      const element = fixture.debugElement.query(By.css('mat-error'));
      expect(element.nativeElement.textContent).toBe(errors[0].message);
    });
  });
});
