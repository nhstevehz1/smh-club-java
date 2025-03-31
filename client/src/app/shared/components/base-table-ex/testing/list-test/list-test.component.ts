import {Component} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {AuthService} from '@app/core/auth';
import {BaseTableExComponent} from '@app/shared/components/base-table-ex';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog';

import {
  TestEditDialogService, TestService, TestTableService,
  TestCreate, TestModel
} from '@app/shared/components/base-table-ex/testing';




@Component({
  selector: 'app-list-test',
  imports: [
    SortablePageableTableComponent,
  ],
  templateUrl: './list-test.component.html',
  styleUrl: './list-test.component.scss'
})
export class ListTestComponent
  extends BaseTableExComponent<TestCreate, TestModel, TestModel> {

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
