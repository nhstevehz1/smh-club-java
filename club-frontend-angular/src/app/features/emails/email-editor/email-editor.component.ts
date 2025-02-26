import {AfterViewInit, Component, OnInit, Signal, signal, ViewChild, ViewChildren, WritableSignal} from '@angular/core';
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {EmailType} from "../models/email-type";
import {MatFormField, MatInput, MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatDivider} from "@angular/material/divider";
import {Email} from "../models/email";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {NgClass} from "@angular/common";
import {combineLatest, filter, merge, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-email-editor',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatDivider,
    MatIcon,
    MatIconButton,
    NgClass
  ],
  templateUrl: './email-editor.component.html',
  styleUrl: './email-editor.component.scss'
})
export class EmailEditorComponent extends BaseEditorComponent<Email> implements OnInit {

  emailTypes = Object.values(EmailType);

  readonly emailError = signal(false);
  readonly emailTypeError = signal(false);

  public get email(): FormControl {
    return this.editorForm.controls.email;
  }

  public get emailType(): FormControl {
    return this.editorForm.controls.email_type;
  }

  constructor() {
    super();
  }

  ngOnInit() {
    this.setErrorSignal(this.emailError, this.email);
    this.setErrorSignal(this.emailTypeError, this.emailType);
  }

  getEmailError(): string {
    if(this.email!.hasError('required')) {
      return 'Email required';
    }

    if(this.email!.hasError('email')) {
      return 'Email invalid';
    }

    return 'invalid';
  }

}
