import {Component, Inject, Optional, signal, WritableSignal} from '@angular/core';
import {FormModelGroup} from '../base-editor/form-model-group';
import {EditAction, EditDialogData} from '../../models/edit-event';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-dialog',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatButton,
    MatDialogActions,
    MatIcon,
    MatTooltip,
    ReactiveFormsModule
  ],
  templateUrl: './edit-dialog.component.html',
  styleUrl: './edit-dialog.component.scss'
})
export class EditDialogComponent<C, D> {

  editForm: WritableSignal<FormModelGroup<D> | undefined>;
  isDeleteAction: WritableSignal<boolean>;

  private readonly eventData: EditDialogData<D>;

  constructor(public dialogRef: MatDialogRef<C, EditDialogData<D>>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: EditDialogData<D>){

    this.eventData = {...data};
    this.isDeleteAction = signal(this.eventData.action == EditAction.Delete);
    this.editForm = signal(this.eventData.form);
  }

  onCancel(): void {
    this.closeDialog(EditAction.Cancel);
  }

  onSave(): void {
    this.closeDialog(this.eventData.action);
  }

  onDelete(): void {
    this.closeDialog(EditAction.Delete);
  }

  private closeDialog(action: EditAction): void {
    this.eventData.action = action;
    this.dialogRef.close(this.eventData);
  }
}
