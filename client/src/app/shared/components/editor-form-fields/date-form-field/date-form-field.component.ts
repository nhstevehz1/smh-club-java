import {Component} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';

import {TranslatePipe} from '@ngx-translate/core';
import {DateTime} from 'luxon';

import {BaseInputComponent} from '@app/shared/components/editor-form-fields';

@Component({
  selector: 'app-date-form-field',
  imports: [
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslatePipe,
  ],
  templateUrl: './date-form-field.component.html',
  styleUrl: './date-form-field.component.scss'
})
export class DateFormFieldComponent extends BaseInputComponent<DateTime>{

}
