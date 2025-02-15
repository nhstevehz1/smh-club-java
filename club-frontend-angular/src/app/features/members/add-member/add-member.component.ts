import {Component} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
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
import {AddressType} from "../../addresses/models/address-type";
import {EmailType} from "../../emails/models/email-type";
import {PhoneType} from "../../phones/models/phone-type";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Member, MemberCreate} from "../models/member";
import {Address} from "../../addresses/models/address";
import {Email} from "../../emails/models/email";
import {Phone} from "../../phones/models/phone";
import {MatTooltip} from "@angular/material/tooltip";
import {MatDivider} from "@angular/material/divider";
import {MembersService} from "../services/members.service";
import {Router} from "@angular/router";
import {catchError} from "rxjs/operators";

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
        EmailEditorComponent,
        MatTooltip,
        MatDivider
    ],
  providers: [
      provideLuxonDateAdapter()
  ],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent {

    createForm: FormModelGroup<MemberCreate>;

    fieldAppearance: MatFormFieldAppearance = 'fill';

    constructor(private formBuilder: FormBuilder,
                private svc: MembersService,
                private router: Router) {

        this.createForm = this.createMemberGroup();
        this.addressForms.push(this.createAddressGroup());
        this.emailForms.push(this.createEmailGroup());
        this.phoneForms.push(this.createPhoneGroup());
    }

    public get memberForm(): FormModelGroup<Member> {
        return this.createForm.get("member") as FormModelGroup<Member>;
    }

    public get addressForms(): FormArray<FormModelGroup<Address>> {
        return this.createForm.get('addresses') as FormArray<FormModelGroup<Address>>;
    }

    public get emailForms(): FormArray<FormModelGroup<Email>> {
        return this.createForm.get('emails') as FormArray<FormModelGroup<Email>>;
    }

    public get phoneForms(): FormArray<FormModelGroup<Phone>> {
        return this.createForm.get("phones") as FormArray<FormModelGroup<Phone>>;
    }

    doneHandler(): void {
        if (this.memberForm.valid) {
            this.svc.createMember(<MemberCreate>this.createForm.value).subscribe({
              next: () =>  this.router.navigate(['p/members']).then(() => {}),
              error: (err: any) => {
                  if (err.error instanceof Error) {
                      // A client-side or network error occurred.
                      console.error('An error occurred:', err.error.message);
                  } else {
                      // The backend returned an unsuccessful response code.
                      console.error(`Backend returned code ${err.status}, body was: ${err.error}`);
                  }
              }
            });
        } else {
            return;
        }
    }

    onAddAddress(): void {
        this.addressForms.push(this.createAddressGroup());
    }

    onDeleteAddress(idx: number): void {
        this.addressForms.removeAt(idx);
    }

    onAddEmail(): void {
        this.emailForms.push(this.createEmailGroup());
    }

    onDeleteEmail(idx: number): void {
        this.emailForms.removeAt(idx);
    }

    onAddPhone(): void {
        this.phoneForms.push(this.createPhoneGroup());
    }

    onDeletePhone(idx: number): void {
        this.phoneForms.removeAt(idx);
    }

    private createMemberGroup(): FormGroup {
        return this.formBuilder.group({
         member: this.formBuilder.group({
             id: [0],
            first_name: [null, [Validators.required ]],
            middle_name: [null],
            last_name: [null, [Validators.required]],
             suffix: [null],
            birth_date: [null, [Validators.required]],
            joined_date: [null, [Validators.required]]}),

        addresses: this.formBuilder.array([]),
        phones: this.formBuilder.array([]),
        emails: this.formBuilder.array([])
        });
    }

    private createAddressGroup(): FormGroup {
        return this.formBuilder.group({
            address1: [null, [Validators.required]],
            address2: [null],
            city: [null, [Validators.required]],
            state: [null, [Validators.required, Validators.minLength(2)]],
            zip: [null, [Validators.required, Validators.minLength(5)]],
            address_type: [AddressType.Home, [Validators.required]],
        });
    }

    private createEmailGroup(): FormGroup {
        return this.formBuilder.group({
            email: [null, [Validators.required, Validators.email]],
            email_type: [EmailType.Home, [Validators.required]]
        });
    }

    private createPhoneGroup(): FormGroup {
        return this.formBuilder.group({
            country_code: ['1', [Validators.required]],
            phone_number: [null, [Validators.required]],
            phone_type: [PhoneType.Home, [Validators.required]],
        });
    }
}
