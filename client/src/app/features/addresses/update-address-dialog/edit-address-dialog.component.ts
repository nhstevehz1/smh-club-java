import {Component, computed, Inject, Optional, signal, WritableSignal} from '@angular/core';
import {AddressService} from '../services/address.service';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {Address, AddressCreate} from '../models/address';
import {FormModelGroup} from '../../../shared/components/base-editor/form-model-group';
import {AddressEditorComponent} from '../address-editor/address-editor.component';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';
import {ReactiveFormsModule} from '@angular/forms';
import {EditAction, EditDialogData} from '../../../shared/models/edit-event';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-update-address-dialog',
  imports: [
    AddressEditorComponent,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatTooltip,
    ReactiveFormsModule
  ],
  providers: [
    AddressService
  ],
  templateUrl: './edit-address-dialog.component.html',
  styleUrl: './edit-address-dialog.component.scss'
})
export class EditAddressDialogComponent {
  editForm: WritableSignal<FormModelGroup<AddressCreate>>;
  addressForm = computed(() => this.editForm());
  isDeleteAction: WritableSignal<boolean>;

  private readonly eventData: EditDialogData<Address>;

  constructor(public dialogRef: MatDialogRef<EditAddressDialogComponent, EditDialogData<Address>>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: EditDialogData<Address>,
              private svc: AddressService) {

    const form: FormModelGroup<AddressCreate> = this.svc.generateAddressForm();

    this.eventData = {...data};

    if(this.eventData.action == EditAction.Edit) {
      form.patchValue(this.eventData.data!);
    }

    this.editForm = signal(form);
    this.isDeleteAction = signal(this.eventData.action == EditAction.Delete);
  }

  onCancel(): void {
    this.eventData.action = EditAction.Cancel;
    this.dialogRef.close(this.eventData);
  }

  onSave(): void {
    if(this.eventData.action == EditAction.Edit) {
      this.updateAddress();
    } else {
      this.createAddress();
    }
  }

  onDelete(): void {
    this.deleteAddress();
  }

  private updateAddress(): void {
    const val = this.editForm().value;
    this.eventData.data!.address1 = val.address1!;
    this.eventData.data!.address2 = val.address2!
    this.eventData.data!.city = val.city!;
    this.eventData.data!.state = val.state!
    this.eventData.data!.postal_code = val.postal_code!
    this.eventData.data!.address_type = val.address_type!

    this.svc.updateAddress(this.eventData.data!).subscribe({
      next: value => this.dialogRef.close({data: value, action: EditAction.Edit}),
      error: (err: HttpErrorResponse) => console.debug(err)
    });
  }

  private createAddress(): void {
    this.svc.createAddress(this.editForm().value as AddressCreate).subscribe({
      next: value => this.dialogRef.close({data: value, action: EditAction.Create}),
      error: (err: HttpErrorResponse) => console.debug(err)
    });
  }

  private deleteAddress(): void {
    this.svc.deleteAddress(this.eventData.data.id).subscribe({
      next: () => this.dialogRef.close({data: this.eventData.data, action: EditAction.Delete}),
      error: (err: HttpErrorResponse) => console.debug(err)
    });
  }
}
