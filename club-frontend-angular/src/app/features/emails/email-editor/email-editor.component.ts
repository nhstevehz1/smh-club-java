import {Component, OnInit} from '@angular/core';
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {EmailType} from "../models/email-type";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-email-editor',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatDivider
  ],
  templateUrl: './email-editor.component.html',
  styleUrl: './email-editor.component.scss'
})
export class EmailEditorComponent extends BaseEditorComponent implements OnInit {

  emailTypes = Object.values(EmailType);

  public get email(): FormControl {
    return this.formGroup.get('email') as FormControl;
  }

  public get emailType(): FormControl {
    return this.formGroup.get('email_type') as FormControl;
  }

  constructor(private formBuilder: FormBuilder) {
    super();
  }

  getEmailError(): string {
    if(this.email.hasError('required')) {
      return 'Email required';
    }

    if(this.email.hasError('email')) {
      return 'Email invalid';
    }

    return 'invalid';
  }

  protected createGroup(): FormGroup {
    return this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      email_type: [EmailType.Home, [Validators.required]]
    });
  }
}
