import {Component, input} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {TranslatePipe} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {SelectOption} from "../models/select-option";
import {BaseInputComponent} from '../base-input-component';

@Component({
  selector: 'app-select-form-field',
    imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        TranslatePipe
    ],
  templateUrl: './select-form-field.component.html',
  styleUrl: './select-form-field.component.scss'
})
export class SelectFormFieldComponent<T> extends BaseInputComponent<T> {

    options
        = input.required<SelectOption<T>[]>();
}
