import { Component } from '@angular/core';
import {BaseViewList} from '@app/shared/components/base-view-list/base-view-list';
import {
  BaseViewListModel,
  MockViewListApiService,
  MockViewListDialogService, MockBaseViewListEditor
} from '@app/shared/components/base-view-list/testing/test-support';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';

@Component({
  selector: 'app-mock-base-view-list',
  imports: [],
  template: '',
})
export class MockBaseViewListComponent extends BaseViewList<BaseViewListModel, MockBaseViewListEditor>{
 constructor(apiSvc: MockViewListApiService,
             dialogSvc: MockViewListDialogService) {
   super(apiSvc, dialogSvc);
 }

  public override processAction(title: string, context: BaseViewListModel, action: EditAction): void {
    super.processAction(title, context, action);
  }
}
