import {Component, computed, inject, input} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {ControlContainer, ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {MemberCreate} from "../models/member";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {EditorHeaderComponent} from "../../../shared/components/editor-header/editor-header.component";
import {
    InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {
    DateFormFieldComponent
} from "../../../shared/components/editor-form-fields/date-form-field/date-form-field.component";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";

@Component({
  selector: 'app-member-editor',
    imports: [
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        EditorHeaderComponent,
        InputFormFieldComponent,
        DateFormFieldComponent
    ],
    viewProviders: [
        {provide: ControlContainer, useFactory: (): ControlContainer => inject(ControlContainer, {skipSelf: true})}
    ],
  templateUrl: './member-editor.component.html',
  styleUrl: './member-editor.component.scss'
})
export class MemberEditorComponent extends BaseEditorComponent<MemberCreate> {

    memberNumberSignal
        = computed(() => this.editorFormSignal().controls.member_number);

    firstNameSignal
        = computed(() => this.editorFormSignal().controls.first_name);
    firstNameErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'firstNameErrors'});

    middleNameSignal
        = computed(() => this.editorFormSignal().controls.middle_name);
    middleNameErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'middleNameErrors'});

    lastNameSignal
        = computed(() => this.editorFormSignal().controls.last_name);
    lastNameErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'lastNameErrors'});

    suffixSignal
        = computed(() => this.editorFormSignal().controls.suffix);
    suffixErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'suffixErrors'});

    birthDateSignal
        = computed(() => this.editorFormSignal().controls.birth_date);
    birthDateErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'birthDateErrors'});

    joinedDateSignal
        = computed(() => this.editorFormSignal().controls.joined_date);
    joinedDateErrorsSignal
        = input<Array<FormControlError>>(undefined, {alias: 'joinedDateErrors'});

    constructor() {
        super();
    }

}
