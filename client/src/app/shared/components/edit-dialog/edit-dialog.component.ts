import {Component, computed, Inject, Optional, Signal, signal, WritableSignal} from '@angular/core';
import {FormModelGroup} from '../base-editor/form-model-group';
import {EditAction, EditDialogData} from './models/edit-event';
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
import {NgComponentOutlet} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';
import {MatFormFieldAppearance} from '@angular/material/form-field';

@Component({
  selector: 'app-edit-dialog',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatButton,
    MatDialogActions,
    MatIcon,
    MatTooltip,
    ReactiveFormsModule,
    TranslatePipe,
    NgComponentOutlet
  ],
  templateUrl: './edit-dialog.component.html',
  styleUrl: './edit-dialog.component.scss'
})
export class EditDialogComponent<T> {
  editForm: Signal<FormModelGroup<T>>;
  title: WritableSignal<string>;
  component: Signal<any>;
  inputs: Signal<any>;
  context: WritableSignal<T>;
  isDeleteAction: WritableSignal<boolean>;

  private readonly dialogData: EditDialogData<T>;

  constructor(public dialogRef: MatDialogRef<EditDialogComponent<T>, EditDialogData<T>>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: EditDialogData<T>){

    this.dialogData = {...data};

    if (this.dialogData.action == EditAction.Edit) {
      this.dialogData.form.patchValue(this.dialogData.context!)
    }

    this.editForm = computed(() => this.dialogData.form);
    this.title = signal(this.dialogData.title);
    this.component = computed(() => this.dialogData.component);
    this.inputs = computed(() => {
      return {
        editorForm: this.editForm(),
        fieldAppearance: 'outline' as MatFormFieldAppearance
      }
    })
    this.context = signal(this.dialogData.context);
    this.isDeleteAction = signal(this.dialogData.action == EditAction.Delete);
  }

  onCancel(): void {
    this.dialogData.action = EditAction.Cancel;
    this.closeDialog(this.dialogData);
  }

  onSave(): void {
    this.dialogData.context = this.editForm().value as T;
    console.debug('onSave', this.dialogData.context);
    this.closeDialog(this.dialogData);
  }

  onDelete(): void {
    this.closeDialog(this.dialogData);
  }

  private closeDialog(result: EditDialogData<T>): void {
    this.dialogRef.close(result);
  }

  protected readonly EditAction = EditAction;
}
