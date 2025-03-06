import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressEditorComponent} from './address-editor.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {FormControl, FormGroup, NG_VALUE_ACCESSOR, ReactiveFormsModule} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Address} from "../models/address";
import {getFormFieldValue} from "../../../shared/test-helpers/test-helpers";
import {
  InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {forwardRef} from "@angular/core";
import {TranslateModule} from "@ngx-translate/core";
import {MatFormFieldAppearance} from "@angular/material/form-field";

describe('AddressEditorComponent', () => {
  let component: AddressEditorComponent;
  let fixture: ComponentFixture<AddressEditorComponent>;
  let loader: HarnessLoader;
  
  const formGroup: FormModelGroup<Address> = new FormGroup({
    id: new FormControl<number>(0, {nonNullable: true}),
    member_id: new FormControl<number>(0, {nonNullable: true}),
    address1: new FormControl<string>('address1', {nonNullable: true}),
    address2: new FormControl<string>('address2', {nonNullable: true}),
    city: new FormControl<string>('city', {nonNullable: true}),
    state: new FormControl<string>('state', {nonNullable: true}),
    zip: new FormControl<string>('zip', {nonNullable: true}),
    address_type: new FormControl<AddressType>(AddressType.Home, {nonNullable: true})
  });

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [
        AddressEditorComponent,
        ReactiveFormsModule,
        TranslateModule.forRoot({})
      ],
      providers: [
          provideNoopAnimations(),
        {
          provide: NG_VALUE_ACCESSOR,
          useExisting: forwardRef(() => AddressEditorComponent),
          multi: true,
        },
        {
          provide: NG_VALUE_ACCESSOR,
          useExisting: forwardRef(() => InputFormFieldComponent),
          multi: true,
        }
      ]
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
    const outline: MatFormFieldAppearance = 'outline';
    const fill: MatFormFieldAppearance = 'fill';
    let harness: MatFormFieldHarness | null;

    beforeEach(() => {
      formGroup.reset();
      fixture.componentRef.setInput('editorForm', formGroup);
    });

    it('should contain the correct number of form fields', async () => {
      const harnesses = await loader.getAllHarnesses(MatFormFieldHarness);
      expect(harnesses.length).toEqual(6);
    });

    describe('address1 field tests', () =>  {
      beforeEach( async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.address1.label'}));
      });

      it('should contain address1 field', async () => {
        expect(harness).not.toBeNull();
      });

      it('address1 form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.address1.value);
      });

      it('address1 should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('address1 should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
      
    });

    describe('address2 field tests', () =>  {
      
      beforeEach( async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.address2.label'}));
      });
      
      it('should contain address2 field', async () => {
        expect(harness).not.toBeNull();
      });

      it('address2 form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe(formGroup.controls.address2.value);
      });

      it('address2 should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('address2 should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
      
    });

    describe('city field tests', () =>  {
      
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.city.label'}));
      });
      
      it('should contain city field', async () => {
        expect(harness).not.toBeNull();
      });

      it('city form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.city.value);
      });

      it('city should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('city should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('state field tests', () =>  {
      
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.state.label'}));
      });
      
      it('should contain state field', async () => {
        expect(harness).not.toBeNull();
      });

      it('state form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.state.value);
      });

      it('state should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('state should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        let appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('postal code field tests', () =>  {
      
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.postalCode.label'}));
      });

      it('should contain zip field', async () => {
        expect(harness).not.toBeNull();
      });

      it('zip form field should contain the correct value', async () => {
        const value = await getFormFieldValue(harness);

        expect(value).toBe(formGroup.controls.zip.value);
      });

      it('zip should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('zip should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });

    describe('address type field tests', ()=>  {
      
      beforeEach(async () => {
        harness =
            await loader.getHarnessOrNull(MatFormFieldHarness.with({
              floatingLabelText: 'addresses.editor.addressType.label'}));
      });
      
      it('should contain AddressType field', async () => {
        expect(harness).not.toBeNull();
      });

      it('AddressType form field should contain correct value', async () => {
        const value = await getFormFieldValue(harness);
        expect(value).toBe('addresses.type.home');
      });

      it('AddressType should use outline appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', outline);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(outline);
      });

      it('AddressType should use fill appearance', async () => {
        fixture.componentRef.setInput('fieldAppearance', fill);
        const appearance = await harness?.getAppearance();
        expect(appearance).toBe(fill);
      });
    });
  });
  
  describe('address remove button and title tests', () => {
    let buttonHarness: MatButtonHarness | null;
    
    beforeEach(async () => {
      fixture.componentRef.setInput('editorForm', formGroup);
    });
    
    fit('should NOT show remove button when showRemoveButton is set to false', async () => {
      fixture.componentRef.setInput('showRemoveButton', false);
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeFalsy();
    });

    it('should show remove button when showRemoveButton is set to true', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      expect(buttonHarness).toBeTruthy();
    });

    it('should call on remove when remove button is clicked', async () => {
      fixture.componentRef.setInput('showRemoveButton', true);
      const spy = spyOn(component, 'onRemove').and.stub();

      buttonHarness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
      await buttonHarness?.click();

      expect(spy).toHaveBeenCalled();
    });

    it('should display title when title is defined', async () => {
      fixture.componentRef.setInput('title', 'test');

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeTruthy()
    });

    it('should NOT display title when title is undefined', async () => {
      fixture.componentRef.setInput('title', undefined);

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element).toBeFalsy()
    });

    it('should display correct title', async () => {
      const title = 'title';
      fixture.componentRef.setInput('title', title);

      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css(('.editor-title')));
      expect(element.nativeElement.textContent).toBe(title);
    });
  });
});
