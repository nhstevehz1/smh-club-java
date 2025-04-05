import {Component, Inject} from '@angular/core';
import {BaseEditDialogComponent} from '@app/shared/components/base-edit-dialog/base-edit-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';
import {
  MockDialogEditorComponent
} from '@app/shared/components/base-edit-dialog/testing/mock-dialog-editor/mock-dialog-editor.component';
import {EditDialogModel} from '@app/shared/components/base-edit-dialog/testing/test-support';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import {EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {TranslatePipe} from '@ngx-translate/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';
import {EditorOutletDirective} from '@app/shared/components/base-editor/directives/editor-outlet.directive';

@Component({
  selector: 'app-mock-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatTooltip,
    TranslatePipe,
    EditorOutletDirective
  ],
  //templateUrl: './mock-dialog-component.html'
  templateUrl: '../../base-edit-dialog.component.html',
})
export class MockDialogComponent extends BaseEditDialogComponent<EditDialogModel, MockDialogEditorComponent> {

  constructor(dialogRef: MatDialogRef<MockDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: EditDialogInput<EditDialogModel, MockDialogEditorComponent>) {
    super(dialogRef, data);
  }
}
