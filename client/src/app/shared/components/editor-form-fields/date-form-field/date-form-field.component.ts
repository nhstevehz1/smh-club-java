import {Component} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {DateTime} from "luxon";
import {BaseInputComponent} from "../base-input-component";
import {MatInputModule} from "@angular/material/input";

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
