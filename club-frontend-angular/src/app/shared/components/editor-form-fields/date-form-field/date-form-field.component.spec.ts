import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DateFormFieldComponent} from './date-form-field.component';
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {MatFormFieldModule} from "@angular/material/form-field";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {FormControl} from "@angular/forms";
import {DateTime} from "luxon";
import {MatInputModule} from "@angular/material/input";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatDatepickerInputHarness, MatDatepickerToggleHarness} from "@angular/material/datepicker/testing";

describe('DateFormFieldComponent', () => {
  let component: DateFormFieldComponent;
  let fixture: ComponentFixture<DateFormFieldComponent>;
  let loader: HarnessLoader;
  let dateControl: FormControl<DateTime>;

  beforeEach(async () => {
    dateControl = new FormControl<DateTime>(DateTime.now(), {nonNullable: true});
    await TestBed.configureTestingModule({
      imports: [
          DateFormFieldComponent,
          MatFormFieldModule,
          MatInputModule,
          TranslateModule.forRoot({})
      ],
      providers: [
          provideLuxonDateAdapter(),
          provideNoopAnimations(),
          TranslatePipe
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DateFormFieldComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('formControl', dateControl);
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should contain one mat-form-field', async () => {
    const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
    expect(harnesses.length).toEqual(1);
  });

  it('should be a date picker form field', async () => {
    const harness = await loader.getHarnessOrNull(MatDatepickerInputHarness);
    expect(harness).not.toBeNull();
  });

  it('should contain date picker toggle', async () => {
    const harness = await loader.getHarnessOrNull(MatDatepickerToggleHarness);
    expect(harness).not.toBeNull();
  })

  describe('date time base form fields tests', ()=> {
    let harness: MatFormFieldHarness;

    beforeEach(async () => {
      harness = await loader.getHarness(MatFormFieldHarness);
    });

    it('should show label when label value is defined', async () => {
      fixture.componentRef.setInput('label', 'test');
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeTrue();
    });

   it('should NOT show label by default', async () => {
      const hasLabel = await harness.hasLabel();
      expect(hasLabel).toBeFalse();
    });

    it('should show correct label', async () => {
      const val = 'test'
      fixture.componentRef.setInput('label', val);
      const label = await harness.getLabel();
      expect(label).toBe(val);
    });

    it('should have outline appearance by default', async () => {
      const appearance = await harness.getAppearance();
      expect(appearance).toBe('outline');
    });

    it('should use the correct appearance', async () => {
      const val = 'fill'
      fixture.componentRef.setInput('appearance', val);
      const appearance = await harness.getAppearance();
      expect(appearance).toBe(val);
    });
  });

  describe('date picker tests', () => {
    let harness: MatDatepickerInputHarness;
    beforeEach(async () => {
        harness = await loader.getHarness(MatDatepickerInputHarness);
    });

    it('should have a calendar', async () =>  {
      const hasCalender = await harness.hasCalendar();
      expect(hasCalender).toBeTrue();
    });

    it('should display the correct date', async () => {
       const now = DateTime.now();
       const formatted = now.toLocaleString(DateTime.DATE_SHORT);

       const dt = await harness.getValue();
       expect(dt).toBe(formatted);
    });
  });
});
