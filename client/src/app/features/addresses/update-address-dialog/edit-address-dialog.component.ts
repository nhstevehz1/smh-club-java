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
import {EditAction, EditDialogData} from '../../../shared/components/edit-dialog/models/edit-event';


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
  editForm: WritableSignal<FormModelGroup<Address>>;
  addressForm = computed(() => this.editForm());
  isDeleteAction: WritableSignal<boolean>;

  private readonly eventData: EditDialogData<Address>;

  constructor(public dialogRef: MatDialogRef<EditAddressDialogComponent, EditDialogData<Address>>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: EditDialogData<Address>) {

    this.eventData = {...data};

    if(this.eventData.action == EditAction.Edit) {
      data.form.patchValue(this.eventData.context!);
    }

    this.editForm = signal(data.form);
    this.isDeleteAction = signal(this.eventData.action == EditAction.Delete);
  }

  onCancel(): void {
    this.eventData.action = EditAction.Cancel;
    this.dialogRef.close(this.eventData);
  }

  onSave(): void {
    this.eventData.context = this.editForm().value as Address;
    this.dialogRef.close(this.eventData);
  }

  onDelete(): void {
    this.dialogRef.close()
  }
}
