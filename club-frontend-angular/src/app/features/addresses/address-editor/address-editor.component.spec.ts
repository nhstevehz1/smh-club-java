import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressEditorComponent} from './address-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {FormControl, FormGroup, ValidationErrors} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {MatInputHarness} from "@angular/material/input/testing";
import {MatSelectHarness} from "@angular/material/select/testing";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";

describe('AddressEditorComponent', () => {
  let component: AddressEditorComponent;
  let fixture: ComponentFixture<AddressEditorComponent>;
  let loader: HarnessLoader;
  let formGroup = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    address1: new FormControl<string>('address1', {nonNullable: true}),
    address2: new FormControl<string>('address2', {nonNullable: true}),
    city: new FormControl<string>('city', {nonNullable: true}),
    state: new FormControl<string>('state', {nonNullable: true}),
    zip: new FormControl<string>('zip', {nonNullable: true}),
    address_type: new FormControl<AddressType | string>(AddressType.Home, {nonNullable: true})
  });

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [AddressEditorComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressEditorComponent);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('form field tests', () => {
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

      expect(harnesses.length).toEqual(6);
    });

    describe('address1 field tests', () =>  {

      beforeEach( async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Street or PO Box'}));
      });

      it('should contain address1 field', async () => {
        expect(harness).not.toBeNull();
      });

      it('address1 form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.address1.value);
      });

      it('address1 should use outline appearance', async () => {
        component.fieldAppearance = outline;
        let appearance = await harness?.getAppearance();

        expect(appearance).toBe(outline);
      });

      it('address1 should use fill appearance', async () => {
        component.fieldAppearance = fill;

        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('address1 should show error', async () => {
        formGroup.controls.address1.markAsTouched()
        formGroup.controls.address1.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('address1 should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('address2 field tests', () =>  {

      beforeEach( async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: '(address continued)'}));
      });

      it('should return correct address2 value', () => {
        const result = component.address2;
        expect(result.value).toBe(formGroup.controls.address2.value);
      });

      it('should contain address2 field', async () => {
        expect(harness).not.toBeNull();
      });

      it('address2 form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.address2.value);
      });

      it('address2 should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('address2 should use fill appearance', async () => {
        component.fieldAppearance = fill;
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('address2 should show error', async () => {
        formGroup.controls.address2.markAsTouched()
        formGroup.controls.address2.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('address2 should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('city field tests', () =>  {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'City/Town'}));
      });

      it('should return city value', () => {
        const result = component.city;
        expect(result.value).toBe(formGroup.controls.city.value);
      });

      it('should contain city field', async () => {
        expect(harness).not.toBeNull();
      });

      it('city form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.city.value);
      });

      it('city should use outline appearance', async () => {
        component.fieldAppearance = outline;
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('city should use fill appearance', async () => {
        component.fieldAppearance = fill;
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('city should show error', async () => {
        formGroup.controls.city.markAsTouched()
        formGroup.controls.city.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('city should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('state field tests', () =>  {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'State/Territory'}));
      });

      it('should return state value', () => {
        const result = component.state;

        expect(result.value).toBe(formGroup.controls.state.value);
      });

      it('should contain state field', async () => {
        expect(harness).not.toBeNull();
      });

      it('state form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.state.value);
      });

      it('state should use outline appearance', async () => {
        component.fieldAppearance = outline;
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('state should use fill appearance', async () => {
        component.fieldAppearance = fill;
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('state should show error', async () => {
        formGroup.controls.state.markAsTouched()
        formGroup.controls.state.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('state should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('zip field tests', () =>  {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Postal Code'}));
      });

      it('should return zip value', () => {
        const result = component.zip;

        expect(result.value).toBe(formGroup.controls.zip.value);
      });

      it('should contain zip field', async () => {
        expect(harness).not.toBeNull();
      });

      it('zip form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.zip.value);
      });

      it('zip should use outline appearance', async () => {
        component.fieldAppearance = outline;
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('zip should use fill appearance', async () => {
        component.fieldAppearance = fill;
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('zip should show error', async () => {
        formGroup.controls.zip.markAsTouched()
        formGroup.controls.zip.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('zip should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });

    describe('address type field tests', ()=>  {
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Address type'}));
      });

      it('should return AddressType value', () => {
        const result = component.addressType;

        expect(result.value).toBe(formGroup.controls.address_type.value);
      });

      it('should contain AddressType field', async () => {
        expect(harness).not.toBeNull();
      });

      it('AddressType form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.address_type.value);
      });

      it('AddressType should use outline appearance', async () => {
        component.fieldAppearance = outline;

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('AddressType should use fill appearance', async () => {
        component.fieldAppearance = fill;
        const harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({floatingLabelText: 'Address type'}));

        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });

      it('address type should show error', async () => {
        formGroup.controls.address_type.markAsTouched()
        formGroup.controls.address_type.setErrors(errors);

        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(1);
      });

      it('address type should NOT show error', async () => {
        const errHarnesses = await harness?.getErrors();

        expect(errHarnesses).toBeTruthy();
        expect(errHarnesses?.length).toEqual(0)
      });
    });
  });
  
  describe('remove button and title tests', () => {
    let buttonHarness: MatButtonHarness | null;
    
    beforeEach(async () => {
      component.editorForm = formGroup;
    });
    
    it('should NOT show remove button when showRemoveButton is set to false', async () => {
      component.showRemoveButton = false;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeFalsy();
    });

    it('should show remove button when showRemoveButton is set to true', async () => {
      component.showRemoveButton = true;
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeTruthy();
    });

    it('should call on remove when remove button is clicked', async () => {
      component.showRemoveButton = true;
      const spy = spyOn(component, 'onRemove').and.stub();

      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      await buttonHarness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when title is defined', async () => {
      component.title = 'test';

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeTruthy()
    });

    it('should display title when title is undefined', async () => {
      component.title = undefined;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeFalsy()
    });

    it('should display correct title', async () => {
      const title = 'title';
      component.title = title;

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element.nativeElement.textContent).toBe(title);
    });
  });
});

export async function getFormFieldValue(harness: MatFormFieldHarness | null ): Promise<string> {

  const control = await harness?.getControl();

  if(control instanceof MatInputHarness) {

    const input: MatInputHarness = (control as MatInputHarness);
    return input.getValue();
  } else {

    const select: MatSelectHarness = (control as MatSelectHarness);
    return select.getValueText()
  }
}
