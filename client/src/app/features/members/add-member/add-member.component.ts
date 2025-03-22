import {Component, computed, signal, WritableSignal} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {FormArray, ReactiveFormsModule} from "@angular/forms";
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
import {AddressService} from "../../addresses/services/address.service";
import {EmailService} from "../../emails/services/email.service";
import {PhoneService} from "../../phones/services/phone.service";

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
      MembersService,
      AddressService,
      EmailService,
      PhoneService
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
        = signal<MatFormFieldAppearance>('outline');

    errorMessage = signal<string | null>(null);

    submitted = signal(false);


    constructor(private memberSvc: MembersService,
                private addressSvc: AddressService,
                private emailSvc: EmailService,
                private phoneSvc: PhoneService,
                private router: Router) {


        const addressForm = this.addressSvc.generateCreateForm();
        const emailForm = this.emailSvc.generateCreateForm();
        const phoneForm = this.phoneSvc.generateCreateForm();
        const createForm = this.memberSvc.generateCreateForm(addressForm, emailForm, phoneForm);
        this.createFormSignal = signal(createForm);
    }

    onSave(): void {
        if (this.createFormSignal().valid) {
            this.memberSvc.createMember(this.createFormSignal().value as MemberCreate).subscribe({
              next: () =>  {
                  this.errorMessage.set(null)
                  this.submitted.set(true);
              },
              error: (err: any) => {
                  let errMsg: string;
                  if (err.error instanceof Error) {
                      // A client-side or network error occurred.
                      console.debug(`Client of network error: ${err}`);
                      errMsg = 'An error has occurred.  Contact your administrator.';
                  } else {
                      // The backend returned an unsuccessful response code.
                      console.debug(`Server error, ${err}`);
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
        this.router.navigate(['p/members']).then();
    }

    onAddAddress(): void {
        this.getAddresses().push(this.addressSvc.generateCreateForm());
    }

    onDeleteAddress(idx: number): void {
        this.getAddresses().removeAt(idx);
    }

    onAddEmail(): void {
        this.getEmails().push(this.emailSvc.generateCreateForm());
    }

    onDeleteEmail(idx: number): void {
        this.getEmails().removeAt(idx);
    }

    onAddPhone(): void {
        this.getPhones().push(this.phoneSvc.generateCreateForm());
    }

    onDeletePhone(idx: number): void {
        this.getPhones().removeAt(idx);
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
}
