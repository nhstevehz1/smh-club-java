import {Component, computed, inject, input} from '@angular/core';
import {ControlContainer, ReactiveFormsModule} from '@angular/forms';

import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {Member} from '@app/features/members/models/member';
import {
  InputFormFieldComponent
} from '@app/shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {
  DateFormFieldComponent
} from '@app/shared/components/editor-form-fields/date-form-field/date-form-field.component';
import {FormControlError, InputType} from '@app/shared/components/editor-form-fields/models';

@Component({
  selector: 'app-member-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    InputFormFieldComponent,
    DateFormFieldComponent
  ],
    viewProviders: [
        {provide: ControlContainer, useFactory: (): ControlContainer => inject(ControlContainer, {skipSelf: true})}
    ],
  templateUrl: './member-editor.component.html',
  styleUrl: './member-editor.component.scss'
})
export class MemberEditorComponent extends BaseEditorComponent<Member> {

    memberNumber= computed(() => this.editorForm()!.controls.member_number);
    memberNumberErrors = input<FormControlError[]>();

    firstName= computed(() => this.editorForm()!.controls.first_name);
    firstNameErrors= input<FormControlError[]>();

    middleName= computed(() => this.editorForm()!.controls.middle_name);
    middleNameErrors= input<FormControlError[]>();

    lastName= computed(() => this.editorForm()!.controls.last_name);
    lastNameErrors= input<FormControlError[]>();

    suffix= computed(() => this.editorForm()!.controls.suffix);
    suffixErrors= input<FormControlError[]>();

    birthDate= computed(() => this.editorForm()!.controls.birth_date);
    birthDateErrors= input<FormControlError[]>();

    joinedDate= computed(() => this.editorForm()!.controls.joined_date);
    joinedDateErrors= input<FormControlError[]>();

    constructor() {
        super();
    }

  protected readonly InputType = InputType;
}
