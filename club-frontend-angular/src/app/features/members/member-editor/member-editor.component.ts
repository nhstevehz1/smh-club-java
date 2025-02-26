import {Component, inject, OnInit, signal} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {ControlContainer, FormControl, ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatDivider} from "@angular/material/divider";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {Member} from "../models/member";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-member-editor',
    imports: [
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        MatDatepicker,
        MatDatepickerInput,
        MatDatepickerToggle,
        MatDivider,
        NgClass
    ],
    viewProviders: [
        {provide: ControlContainer, useFactory: (): ControlContainer => inject(ControlContainer, {skipSelf: true})}
    ],
  templateUrl: './member-editor.component.html',
  styleUrl: './member-editor.component.scss'
})
export class MemberEditorComponent extends BaseEditorComponent<Member> implements OnInit {

    readonly firstNameError = signal(false);
    readonly middleNameError = signal(false);
    readonly lastNameError = signal(false);
    readonly suffixError = signal(false);
    readonly birthDateError = signal(false);
    readonly joinedDateError = signal(false);

    constructor() {
        super();
    }

    public get firstName(): FormControl {
        return this.editorForm.controls.first_name;
    }

    public get middleName(): FormControl {
        return this.editorForm.controls.middle_name;
    }

    public get lastName(): FormControl {
        return this.editorForm.controls.last_name;
    }

    public get suffix(): FormControl {
        return this.editorForm.controls.suffix;
    }

    public get birthDate(): FormControl {
        return this.editorForm.controls.birth_date;
    }

    public get joinedDate(): FormControl {
        return this.editorForm.controls.joined_date;
    }

    ngOnInit() {
        this.setErrorSignal(this.firstNameError, this.firstName);
        this.setErrorSignal(this.middleNameError, this.middleName);
        this.setErrorSignal(this.lastNameError, this.lastName);
        this.setErrorSignal(this.suffixError, this.suffix);
        this.setErrorSignal(this.birthDateError, this.birthDate);
        this.setErrorSignal(this.joinedDateError, this.joinedDate);
    }
}
