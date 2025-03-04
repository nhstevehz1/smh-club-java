import {Component, input} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {TranslatePipe} from "@ngx-translate/core";
import {InputComponentBase} from "../models/input-component";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {SelectOption} from "../models/select-option";

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
export class SelectFormFieldComponent<T> extends InputComponentBase<T> {

    optionsSignal
        = input.required<SelectOption<T>[]>({alias: 'options'});
}
