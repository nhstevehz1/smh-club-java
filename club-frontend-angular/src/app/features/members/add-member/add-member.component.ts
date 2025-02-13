import {Component} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldAppearance, MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {MatSelectModule} from "@angular/material/select";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {AddressEditorComponent} from "../../addresses/address-editor/address-editor.component";
import {MemberEditorComponent} from "../member-editor/member-editor.component";
import {PhoneEditorComponent} from "../../phones/phone-editor/phone-editor.component";
import {EmailEditorComponent} from "../../emails/email-editor/email-editor.component";

@Component({
  selector: 'app-create-member',
    imports: [
        MatFormFieldModule,
        MatCardModule,
        MatInputModule,
        MatDatepickerModule,
        MatSelectModule,
        MatIconModule,
        MatButtonModule,
        ReactiveFormsModule,
        AddressEditorComponent,
        MemberEditorComponent,
        PhoneEditorComponent,
        EmailEditorComponent
    ],
  providers: [
      provideLuxonDateAdapter()
  ],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent {

    memberForm: FormGroup;

    fieldAppearance: MatFormFieldAppearance = 'fill';

    constructor(private formBuilder: FormBuilder){
        this.memberForm = this.formBuilder.group({});
    }

    doneHandler(): void {
        if (!this.memberForm.valid) {
            return;
        }
    }

}
