import {Component, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';

import {TranslatePipe} from '@ngx-translate/core';

import {BaseInputComponent, SelectOption} from '@app/shared/components/editor-form-fields';

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
