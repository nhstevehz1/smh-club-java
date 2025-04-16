import {Component} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';


import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {EditAction, EditEvent, EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';

import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {AuthService} from '@app/core/auth/services/auth.service';
import {TableModelCreate, TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {MockTableApiService} from '@app/shared/components/base-table-component/testing/services/mock-table-api.service';
import {MockTableService} from '@app/shared/components/base-table-component/testing/services/mock-table.service';

import {
  MockTableEditDialogService
} from '@app/shared/components/base-table-component/testing/services/mock-table-edit-dialog.service';
import {Observable} from 'rxjs';
import {MockTableEditorComponent} from '@app/shared/components/base-table-component/testing/mock-editor/mock-table-editor.component';
import {BaseTableTest} from '@app/shared/components/base-table-component/testing/test-support';

@Component({
  selector: 'app-mock-base-table',
  imports: [
    SortablePageableTableComponent,
  ],
  templateUrl: './mock-base-table.component.html'
})
export class MockBaseTableComponent
  extends BaseTableComponent<TableModel, TableModel, MockTableEditorComponent> {

  constructor(auth: AuthService,
              testSvc: MockTableApiService,
              tableSvc: MockTableService,
              dialogSvc: MockTableEditDialogService) {
    super(auth, testSvc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<TableModel>): void {
    this.openEditDialog(this.dialogSvc.generateDialogInput('title', event.data, EditAction.Edit)).subscribe({
      next: model => console.debug(model),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<TableModel>): void {
    this.openEditDialog(this.dialogSvc.generateDialogInput('title', event.data, EditAction.Delete)).subscribe({
      next: model => console.debug(model),
      error: err => this.errors.set(err)
    });
  }

  override processRequestError(err: HttpErrorResponse) {
    this.errors.set(err.statusText);
  }

  openEditDialogEx(dialogInput: EditDialogInput<TableModel, MockTableEditorComponent>): Observable<EditDialogResult<TableModel>> {
    return super.openEditDialog(dialogInput);
  }

  updateItemEx(item: TableModel): void {
    super.updateItem(item);
  }

  deleteItemEx(id: number): void {
    super.deleteItem(id);
  }
}
