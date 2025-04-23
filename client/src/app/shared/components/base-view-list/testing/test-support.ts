import {Injectable} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {BaseCrudService} from '@app/shared/services/api-service/base-crud.service';
import {HttpClient} from '@angular/common/http';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {EditAction, EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {FormGroup, FormControl} from '@angular/forms';
import {EditDialogService} from '@app/shared/services/dialog-edit-service/edit-dialog-service';
import {Observable, of} from 'rxjs';

export interface BaseViewListModel {
  id: number,
  field: string
}

export class BaseViewListTest {
  public static generateModel(prefix: number): BaseViewListModel {
    return {
      id: prefix,
      field: prefix + 'field'
    }
  }

  public static generateModelList(size: number) {
    const list: BaseViewListModel[] = [];
    for(let ii = 0; ii < size; ii++) {
      list.push(this.generateModel(ii));
    }
    return list;
  }

  public static generateDialogResult(): EditDialogResult<BaseViewListModel> {
    return {
      context: this.generateModel(0),
      action: EditAction.Cancel
    }
  }
}

@Injectable()
export class MockViewListApiService extends BaseCrudService<BaseViewListModel>{
  constructor(http: HttpClient) {
    super('', http);
  }
}

@Injectable()
export class MockViewListDialogService implements  EditDialogService<BaseViewListModel, MockBaseViewListEditor> {//BaseEditDialogService<BaseViewListModel, MockBaseViewListEditor> {

  generateDialogInput(title: string, context: BaseViewListModel, action: EditAction): EditDialogInput<BaseViewListModel, MockBaseViewListEditor> {
    return {
      action: action,
      title: title,
      context: context,
      editorConfig: {
        form: this.generateForm(),
        component: MockBaseViewListEditor
      }
    };
  }

  generateForm(): FormModelGroup<BaseViewListModel> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      field: new FormControl('', {nonNullable: true})
    });
  }

  openDialog(dialogData: EditDialogInput<BaseViewListModel, MockBaseViewListEditor>): Observable<EditDialogResult<BaseViewListModel>> {
    return of({
      context: dialogData.context,
      action: dialogData.action
    });
  }
}

export class MockBaseViewListEditor extends BaseEditorComponent<BaseViewListModel> {
}
