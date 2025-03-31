import {Component, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

import {TranslatePipe} from '@ngx-translate/core';

import {BaseInputComponent, InputType} from '@app/shared/components/editor-form-fields';

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
export class InputFormFieldComponent extends BaseInputComponent<number | string> {

    prefixText
        = input<string>();

    inputType =
        input<InputType>(InputType.Text);

}
