import {Component} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';


import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog/models';

import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {AuthService} from '@app/core/auth/services/auth.service';
import {TestCreate, TestModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {TestService} from '@app/shared/components/base-table-component/testing/services/test.service';
import {TestTableService} from '@app/shared/components/base-table-component/testing/services/test-table.service';

import {
  TestEditDialogService
} from '@app/shared/components/base-table-component/testing/services/test-edit-dialog.service';

@Component({
  selector: 'app-list-test',
  imports: [
    SortablePageableTableComponent,
  ],
  templateUrl: './list-test.component.html',
  styleUrl: './list-test.component.scss'
})
export class ListTestComponent
  extends BaseTableComponent<TestCreate, TestModel, TestModel> {

  constructor(auth: AuthService,
              testSvc: TestService,
              tableSvc: TestTableService,
              dialogSvc: TestEditDialogService) {
    super(auth, testSvc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<TestModel>): void {
    this.openEditDialog(this.dialogSvc.generateDialogInput('title', event.data, EditAction.Edit)).subscribe({
      next: model => console.log(model),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<TestModel>): void {
    this.openEditDialog(this.dialogSvc.generateDialogInput('title', event.data, EditAction.Delete)).subscribe({
      next: model => console.log(model),
      error: err => this.errors.set(err)
    });
  }

  override processRequestError(err: HttpErrorResponse) {
    this.errors.set(err.statusText);
  }
}
