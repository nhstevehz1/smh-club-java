import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RenewalEditorComponent} from './renewal-editor.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {provideLuxonDateAdapter} from '@angular/material-luxon-adapter';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {Renewal} from '@app/features/renewals/models';
import {FormGroup, FormControl} from '@angular/forms';
import {DateTime} from 'luxon';
import {TranslateModule} from '@ngx-translate/core';
import {MatFormFieldAppearance} from '@angular/material/form-field';
import {MatFormFieldHarness} from '@angular/material/form-field/testing';
import {TestHelpers} from '@app/shared/testing';

describe('RenewalEditorComponent', () => {
  let component: RenewalEditorComponent;
  let fixture: ComponentFixture<RenewalEditorComponent>;
  let loader: HarnessLoader;

  const formGroup: FormModelGroup<Renewal> = new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    member_id: new FormControl(0, {nonNullable: true}),
    renewal_date: new FormControl(DateTime.now(), {nonNullable: true}),
    renewal_year: new FormControl(DateTime.now().year, {nonNullable: true})
  })

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RenewalEditorComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideLuxonDateAdapter(),
        provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenewalEditorComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('editorForm', formGroup);
    fixture.detectChanges();
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

    it('should contain the correct number of renewal form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(2);
    });

    describe('renewal date tests', () => {
      beforeEach(async () => {
        harness =
          await loader.getHarnessOrNull(MatFormFieldHarness.with(
            {floatingLabelText: 'renewals.editor.renewalDate.label'}
          ));
      });

      it('should contain renewal date field', () => {
        expect(harness).toBeTruthy();
      });

      it('should contain the correct date', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        const shortDate = formGroup.controls.renewal_date.value.toLocaleString(DateTime.DATE_SHORT);
        expect(value).toBe(shortDate);
      });

      it('renewal date should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('renewal date should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('renewal year tests', () => {
      beforeEach(async () => {
        harness =
          await loader.getHarnessOrNull(MatFormFieldHarness.with(
            {floatingLabelText: 'renewals.editor.renewalYear.label'}
          ));
      });

      it('should contain renewal year field', () => {
        expect(harness).toBeTruthy();
      });

      it('should contain the correct year', async () => {
        const value = await TestHelpers.getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.renewal_year.value.toString());
      });

      it('renewal year should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('renewal year should use fill appearance', async() => {
        fixture.componentRef.setInput('fieldAppearance', fill);

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });
  });
});
