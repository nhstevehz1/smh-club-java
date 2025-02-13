import {Component, inject, Input, OnInit} from '@angular/core';
import {MatError, MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {ControlContainer, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatDivider} from "@angular/material/divider";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";

@Component({
  selector: 'app-member-editor',
    imports: [
        ReactiveFormsModule,
        MatInputModule,
        MatSelectModule,
        MatDatepicker,
        MatDatepickerInput,
        MatDatepickerToggle,
        MatDivider
    ],
    viewProviders: [
        {provide: ControlContainer, useFactory: (): ControlContainer => inject(ControlContainer, {skipSelf: true})}
    ],
  templateUrl: './member-editor.component.html',
  styleUrl: './member-editor.component.scss'
})
export class MemberEditorComponent extends BaseEditorComponent implements OnInit{

    constructor(private formBuilder: FormBuilder) {
        super();
    }

    public get firstName(): FormControl {
        return this.formGroup.get('first_name') as FormControl;
    }

    public get middleName(): FormControl {
        return this.formGroup.get('middle_name') as FormControl;
    }

    public get lastName(): FormControl {
        return this.formGroup.get('last_name') as FormControl;
    }

    public get suffix(): FormControl {
        return this.formGroup.get('suffix') as FormControl;
    }

    public get birthDate(): FormControl {
        return this.formGroup.get('birth_date') as FormControl;
    }

    public get joinedDate(): FormControl {
        return this.formGroup.get('joined_date') as FormControl;
    }

    protected createGroup(): FormGroup {
        return this.formBuilder.group({
            first_name: [null, [Validators.required ]],
            middle_name: [null],
            last_name: [null, [Validators.required]],
            birth_date: [null, [Validators.required]],
            joined_date: [null, [Validators.required]],
            suffix: [null],
            addresses: this.formBuilder.array([]),
            phones: this.formBuilder.array([]),
            emails: this.formBuilder.array([])
        });
    }
}
