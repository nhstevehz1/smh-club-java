import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SelectFormFieldComponent} from './select-form-field.component';
import {TestStringEnum} from "./test-support/test-support";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {SelectOption} from "../models/select-option";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatSelectHarness} from "@angular/material/select/testing";

describe('SelectFormFieldComponent', () => {
  let component: SelectFormFieldComponent<TestStringEnum>;
  let fixture: ComponentFixture<SelectFormFieldComponent<TestStringEnum>>;
  let loader: HarnessLoader;

  let textControl: FormControl<TestStringEnum>;

  const stringOptions: SelectOption<TestStringEnum>[] = [
    {label: 'Test1', value: TestStringEnum.Test1},
    {label: 'Test2', value: TestStringEnum.Test2},
    {label: 'Test3', value: TestStringEnum.Test3}
  ];

  beforeEach(async () => {
    textControl = new FormControl<TestStringEnum>(TestStringEnum.Test1, {nonNullable: true});

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
  });

  fit('should create', async () => {
    fixture.componentRef.setInput('formControl', textControl);
    fixture.componentRef.setInput('options', stringOptions);
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  fit('select should contain one mat-form-field', async () => {
    fixture.componentRef.setInput('formControl', textControl);
    fixture.componentRef.setInput('options', stringOptions);
    const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
    expect(harnesses.length).toEqual(1);
  });

  fit('should contain one select field', async () => {
    fixture.componentRef.setInput('formControl', textControl);
    fixture.componentRef.setInput('options', stringOptions);
    const harnesses = await loader.getAllHarnesses(MatSelectHarness);
    expect(harnesses.length).toEqual(1);
  });

  describe('select base form field tests', () => {
    let harness: MatFormFieldHarness;

    beforeEach(async () => {
      fixture.componentRef.setInput('formControl', textControl);
      fixture.componentRef.setInput('options', stringOptions);
      harness = await loader.getHarness(MatFormFieldHarness);
    });

    fit('select should show label when label value is defined', async () => {
      fixture.componentRef.setInput('label', 'test');
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeTrue();
    });

    fit('select should NOT show label by default', async () => {
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeFalse();
    });

    fit('select should show correct label', async () => {
      const val = 'test'
      fixture.componentRef.setInput('label', val);
      const label = await harness.getLabel();
      expect(label).toBe(val);
    });

    fit('select should have outline appearance by default', async () => {
      const appearance = await harness.getAppearance();
      expect(appearance).toBe('outline');
    });

    fit('select should use the correct appearance', async () => {
      const val = 'fill'
      fixture.componentRef.setInput('appearance', val);
      const appearance = await harness.getAppearance();
      expect(appearance).toBe(val);
    });
  });

  describe('select field tests', () => {
    let harness: MatSelectHarness

    beforeEach(async () => {
      fixture.componentRef.setInput('formControl', textControl);
      fixture.componentRef.setInput('options', stringOptions);
      harness = await loader.getHarness(MatSelectHarness);
    });

    fit('should contain the correct number of options', async () => {
      const options = await harness.getOptions();
      expect(options.length).toEqual(stringOptions.keys.length);
    });

    fit('should contain the correct labels', async() => {
      const options = await harness.getOptions();

      for(let ii = 0; ii < options.length; ii++) {
        let option = options[ii];
        const label = await option.getText();
        expect(label).toBe(stringOptions[ii].label);
      }
    });

  });
});
