import {Component, input, signal} from '@angular/core';
import {
    SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {FormControl} from "@angular/forms";
import {PhoneType, PhoneTypeOption} from "../models/phone-type";
import {MatFormFieldAppearance} from "@angular/material/form-field";

@Component({
  selector: 'app-phone-type-form-field',
    imports: [
        SelectFormFieldComponent
    ],
  templateUrl: './phone-type-form-field.component.html',
  styleUrl: './phone-type-form-field.component.scss'
})
export class PhoneTypeFormFieldComponent {

    formControlSignal
        = input.required<FormControl<PhoneType>>({alias: 'formControl'});

    appearanceSignal
        = input<MatFormFieldAppearance>(undefined, {alias: 'appearance'});

    optionsSignal
        = signal<Array<PhoneTypeOption>> ([])

    labelSignal =
        input<string>(undefined, {alias: 'label'});

    constructor() {
        this.optionsSignal.set([
            {label: 'phones.type.home', value: PhoneType.Home },
            {label: 'phones.type.mobile', value: PhoneType.Mobile},
            {label: 'phones.type.work', value: PhoneType.Work}
        ]);
    }
}
