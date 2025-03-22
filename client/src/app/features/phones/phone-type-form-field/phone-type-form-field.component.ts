import {Component, input, signal} from '@angular/core';
import {
    SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {FormControl} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";
import {SelectOption} from '../../../shared/components/editor-form-fields/models/select-option';

@Component({
  selector: 'app-phone-type-form-field',
    imports: [
        SelectFormFieldComponent
    ],
  templateUrl: './phone-type-form-field.component.html',
  styleUrl: './phone-type-form-field.component.scss'
})
export class PhoneTypeFormFieldComponent {

    formControl
        = input.required<FormControl<PhoneType>>();

    appearance
        = input<MatFormFieldAppearance>();

    label = input<string>();

    controlErrors
        = input<FormControlError[]>();

    options = signal<SelectOption<PhoneType>[]>([]);

    constructor() {
        this.options.set([
            {label: 'phones.type.home', value: PhoneType.Home },
            {label: 'phones.type.mobile', value: PhoneType.Mobile},
            {label: 'phones.type.work', value: PhoneType.Work}
        ]);
    }
}
