import {Component, computed, input} from '@angular/core';
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {EmailBase} from "../models/email";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {EditorHeaderComponent} from "../../../shared/components/editor-header/editor-header.component";
import {
    InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {EmailTypeFormFieldComponent} from "../email-type-form-field/email-type-form-field.component";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";

@Component({
  selector: 'app-email-editor',
    imports: [
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        EditorHeaderComponent,
        InputFormFieldComponent,
        EmailTypeFormFieldComponent
    ],
  templateUrl: './email-editor.component.html',
  styleUrl: './email-editor.component.scss'
})
export class EmailEditorComponent extends BaseEditorComponent<EmailBase> {

  email = computed(() => this.editorForm().controls.email);
  emailErrors = input<FormControlError[]>();

  emailType = computed(() => this.editorForm().controls.email_type);
  emailTypeErrors = input<FormControlError[]>();

  constructor() {
    super();
  }
}
