import {Component, input} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {TranslatePipe} from "@ngx-translate/core";
import {InputType} from "../models/input-type";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {InputComponentBase} from "../models/input-component";

@Component({
  selector: 'app-input-form-field',
    imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        TranslatePipe,
    ],
  templateUrl: './input-form-field.component.html',
  styleUrl: './input-form-field.component.scss'
})
export class InputFormFieldComponent extends InputComponentBase<number | string> {

    prefixText
        = input<string>();

    inputType =
        input<InputType>(InputType.Text);

}
