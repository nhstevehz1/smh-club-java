import {MatDialogRef} from '@angular/material/dialog';
import {EditDialogComponent} from '../edit-dialog.component';
import {first, Observable} from 'rxjs';
import {TemplateRef} from '@angular/core';
import {BaseEditorComponent} from '../../base-editor/base-editor.component';

type DialogRef<T> = MatDialogRef<EditDialogComponent<T>>

export class EditDialogService<T> {

  dialogOpened$: Observable<void>;

  constructor(private dialogRef: DialogRef<T>) {
    this.dialogOpened$ = this.dialogRef.afterOpened().pipe(first());
  }

  close() {
    this.dialogRef.close();
  }


  setTitleText(title: string): void {

  }

  setTemplate(template: TemplateRef<BaseEditorComponent<T>>): void {

  }
}
