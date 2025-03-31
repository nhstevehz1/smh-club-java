import {Component, computed, Inject, Optional, Signal, signal, WritableSignal} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {NgComponentOutlet} from '@angular/common';

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
import {MatFormFieldAppearance} from '@angular/material/form-field';
import {TranslatePipe} from '@ngx-translate/core';

import {FormModelGroup} from '@app/shared/components/base-editor';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';

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

  private readonly dialogData: EditDialogInput<T>;

  constructor(public dialogRef: MatDialogRef<EditDialogComponent<T>, EditDialogInput<T>>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: EditDialogInput<T>){

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
    this.closeDialog(this.dialogData);
  }

  onDelete(): void {
    this.closeDialog(this.dialogData);
  }

  private closeDialog(result: EditDialogInput<T>): void {
    this.dialogRef.close(result);
  }
}
