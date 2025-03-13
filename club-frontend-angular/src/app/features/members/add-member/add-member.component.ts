import {Component, computed, signal, WritableSignal} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {
    FormArray,
    FormBuilder,
    FormGroup,
    NonNullableFormBuilder,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
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
import {MemberCreate} from "../models/member";
import {Address, AddressCreate} from "../../addresses/models/address";
import {Email, EmailCreate} from "../../emails/models/email";
import {Phone, PhoneCreate} from "../../phones/models/phone";
import {MatTooltip} from "@angular/material/tooltip";
import {MatDivider} from "@angular/material/divider";
import {MembersService} from "../services/members.service";
import {Router} from "@angular/router";
import {OkCancelComponent} from "../../../shared/components/ok-cancel/ok-cancel.component";
import {TranslatePipe} from "@ngx-translate/core";
import {DateTime} from "luxon";

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
        MatDivider,
        OkCancelComponent,
        TranslatePipe
    ],
  providers: [
      provideLuxonDateAdapter(),
      MembersService
  ],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent {

    createFormSignal: WritableSignal<FormModelGroup<MemberCreate>>;

    memberFormComputed = computed(() =>
        this.createFormSignal() as unknown as FormModelGroup<MemberCreate>);

    addressFormsComputed = computed(() =>
        this.createFormSignal().controls.addresses as unknown as FormArray<FormModelGroup<AddressCreate>>);

    emailFormsComputed = computed(() =>
        this.createFormSignal().controls.emails as unknown as FormArray<FormModelGroup<EmailCreate>>);

    phoneFormsComputed = computed(() =>
        this.createFormSignal().controls.phones as unknown as FormArray<FormModelGroup<PhoneCreate>>);
    
    fieldAppearance
        = signal<MatFormFieldAppearance>('fill');
    
    errorMessage = signal<string | null>(null);
    
    submitted = signal(false);


    constructor(private formBuilder: FormBuilder,
                private fb: NonNullableFormBuilder,
                private svc: MembersService,
                private router: Router) {


        const addresses
            = this.fb.array<FormModelGroup<AddressCreate>>([this.createAddressGroup()])

        const phones
            = this.fb.array<FormModelGroup<PhoneCreate>>([this.createPhoneGroup()]);

        const emails
            = this.fb.array<FormModelGroup<EmailCreate>>([this.createEmailGroup()])

        const create = this.createFormGroup(addresses, phones, emails);
        this.createFormSignal = signal(create);
    }

    onSave(): void {
        if (this.createFormSignal().valid) {
            this.svc.createMember(<MemberCreate>this.createFormSignal().value).subscribe({
              next: () =>  {
                  this.errorMessage.set(null)
                  this.submitted.set(true);
              },
              error: (err: any) => {
                  let errMsg: string;
                  if (err.error instanceof Error) {
                      // A client-side or network error occurred.
                      console.log(`Client of network error: ${err}`);
                      errMsg = 'An error has occurred.  Contact your administrator.';
                  } else {
                      // The backend returned an unsuccessful response code.
                      console.log(`Server error, ${err}`);
                      errMsg = `Error code: ${err.status}, server error.  Contact your administrator.`;
                  }
                  this.errorMessage.set(errMsg);
              }
            });
        } else {
            this.errorMessage.set('Invalid data');
            return;
        }
    }

    onOkOrCancel(): void {
        this.router.navigate(['p/members']).then(() => {});
    }

    onAddAddress(): void {
        this.getAddresses().push(this.createAddressGroup());
    }

    onDeleteAddress(idx: number): void {
        this.getAddresses().removeAt(idx);
    }

    onAddEmail(): void {
        this.getEmails().push(this.createEmailGroup());
        //this.emailForms.update(value => this.getEmails());
    }

    onDeleteEmail(idx: number): void {
        this.getEmails().removeAt(idx);
        //this.emailForms.update(value => this.getEmails());
    }

    onAddPhone(): void {
        this.getPhones().push(this.createPhoneGroup());
        //this.phoneForms.update(value => this.getPhones());
    }

    onDeletePhone(idx: number): void {
        this.getPhones().removeAt(idx);
        //this.phoneForms.update(value => this.getPhones());
    }

    private getAddresses(): FormArray<FormModelGroup<Address>> {
        return this.createFormSignal().get('addresses') as FormArray<FormModelGroup<Address>>;
    }

    private getEmails(): FormArray<FormModelGroup<Email>> {
        return this.createFormSignal().get('emails') as FormArray<FormModelGroup<Email>>;
    }

    private getPhones(): FormArray<FormModelGroup<Phone>> {
        return this.createFormSignal().get('phones') as FormArray<FormModelGroup<Phone>>;
    }

    private createFormGroup(addresses: FormArray<FormGroup>,
                            phones: FormArray<FormGroup>, emails: FormArray<FormGroup>): FormGroup {
        return this.fb.group({
            id: [0],
            member_number: [0],
            first_name: ['', Validators.required],
            middle_name: [''],
            last_name: ['', Validators.required],
            suffix: [''],
            birth_date: [DateTime.now, Validators.required],
            joined_date: [DateTime.now, Validators.required],
            addresses: addresses,
            phones: phones,
            emails: emails
        });
    }

    private createAddressGroup(): FormGroup {
        return this.formBuilder.group({
            id: [0],
            member_id: [0],
            address1: [null, [Validators.required]],
            address2: [null],
            city: [null, [Validators.required]],
            state: [null, [Validators.required, Validators.minLength(2)]],
            zip: [null, [Validators.required, Validators.minLength(5)]],
            address_type: [AddressType.Home, [Validators.required]],
        });
    }

    private createEmailGroup(): FormGroup {
        return this.fb.group({
            id: [0],
            member_id: [0],
            email: ['', [Validators.required, Validators.email]],
            email_type: [EmailType.Home, [Validators.required]]
        });
    }

    private createPhoneGroup(): FormGroup {
        return this.fb.group({
            id: [0],
            member_id: [0],
            country_code: ['1', [Validators.required]],
            phone_number: ['', [Validators.required]],
            phone_type: [PhoneType.Home, [Validators.required]],
        });
    }
}
