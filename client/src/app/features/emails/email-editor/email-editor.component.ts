import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

import {BaseEditorComponent} from '@app/shared/components/base-editor';
import {FormControlError, InputFormFieldComponent} from '@app/shared/components/editor-form-fields';

import {
  EmailTypeFormFieldComponent, Email, EmailService
} from '@app/features/emails';


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
