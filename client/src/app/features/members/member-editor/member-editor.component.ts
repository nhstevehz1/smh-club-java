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
        = computed(() => this.editorForm().controls.member_number);

    firstName= computed(() => this.editorForm().controls.first_name);
    firstNameErrors= input<FormControlError[]>();

    middleName= computed(() => this.editorForm().controls.middle_name);
    middleNameErrors= input<FormControlError[]>();

    lastNameSignal= computed(() => this.editorForm().controls.last_name);
    lastNameErrors= input<FormControlError[]>();

    suffix= computed(() => this.editorForm().controls.suffix);
    suffixErrors= input<FormControlError[]>();

    birthDate= computed(() => this.editorForm().controls.birth_date);
    birthDateErrors= input<FormControlError[]>();

    joinedDate= computed(() => this.editorForm().controls.joined_date);
    joinedDateErrors= input<FormControlError[]>();

    constructor() {
        super();
    }

}
