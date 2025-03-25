import {Component, computed, input} from '@angular/core';
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {Email} from "../models/email";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {
  InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {EmailTypeFormFieldComponent} from "../email-type-form-field/email-type-form-field.component";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";
import {EmailService} from '../services/email.service';

@Component({
  selector: 'app-email-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    InputFormFieldComponent,
    EmailTypeFormFieldComponent
  ],
  providers: [EmailService],
  templateUrl: './email-editor.component.html',
  styleUrl: './email-editor.component.scss'
})
export class EmailEditorComponent extends BaseEditorComponent<Email> {

  email = computed(() => this.editorForm()!.controls.email);
  emailErrors = input<FormControlError[]>();

  emailType = computed(() => this.editorForm()!.controls.email_type);
  emailTypeErrors = input<FormControlError[]>();

  constructor() {
    super();
  }
}
