import {Component, computed, signal, WritableSignal, Inject} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatDialogRef, MatDialogModule, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';
import {TranslatePipe} from '@ngx-translate/core';
import {EditDialogInput} from '@app/shared/components/base-edit-dialog/models/edit-dialog-input';
import {EditAction} from '@app/shared/components/base-edit-dialog/models/edit-action';
import {Editor} from '@app/shared/components/base-editor/editor';
import {EditorOutletDirective} from '@app/shared/components/base-editor/directives/editor-outlet.directive';
import {EditDialog} from '@app/shared/components/base-edit-dialog/edit-dialog';

@Component({
  selector: 'app-edit-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatTooltip,
    TranslatePipe,
    EditorOutletDirective
  ],
  templateUrl: './base-edit-dialog.component.html'
})
export class BaseEditDialogComponent<T, C extends Editor<T>> implements EditDialog<T, C> {

  readonly editForm = computed(() => {
    const form = this.dialogInput().editorConfig.form;
    if(this.dialogInput().action == EditAction.Edit) {
      form.patchValue(this.dialogInput().context!);
    } else {
      form.reset();
    }
    return form;
  });


  readonly title = computed(() => this.dialogInput().title);
  readonly isDeleteAction = computed(() => this.dialogInput().action == EditAction.Delete);
  readonly editorConfig = computed(() => this.dialogInput().editorConfig);

  readonly dialogInput: WritableSignal<EditDialogInput<T, C>>;

  constructor(protected dialogRef: MatDialogRef<BaseEditDialogComponent<T, C>, EditDialogInput<T, C>>,
              @Inject(MAT_DIALOG_DATA) protected data: EditDialogInput<T, C>){
    this.dialogInput = signal({...data});
  }

  onCancel(): void {
    this.dialogInput().action = EditAction.Cancel;
    this.closeDialog(this.dialogInput());
  }

  onSave(): void {
    this.dialogInput().context = this.editForm().value as T;
    this.closeDialog(this.dialogInput());
  }

  onDelete(): void {
    this.closeDialog(this.dialogInput());
  }

  private closeDialog(result: EditDialogInput<T, C>): void {
    this.dialogRef.close(result);
  }
}
