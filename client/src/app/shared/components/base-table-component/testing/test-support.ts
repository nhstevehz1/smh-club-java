import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {TestHelpers} from '@app/shared/testing';
import {PagedData} from '@app/shared/services/api-service/models';
import {EditDialogInput, EditAction} from '@app/shared/components/base-edit-dialog/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {FormGroup, FormControl} from '@angular/forms';
import {MockTableEditorComponent} from '@app/shared/components/base-table-component/testing/mock-editor/mock-table-editor.component';
import {Updatable} from '@app/shared/models/updatable';


export interface TableModelCreate {
  tableField: string,
}

export interface TableModel extends TableModelCreate, Updatable {}

export class BaseTableTest {

  static generatePagedData(page: number, size: number, total: number): PagedData<TableModel> {
    const content = this.generateModelList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateModelList(size: number): TableModel[] {
    const list: TableModel[] = [];
    for(let ii = 0; ii < size; ii++) {
      list.push({id: ii, tableField: `${ii}test`});
    }
    return list;
  }

  static generateColumnDefs(): ColumnDef<TableModel>[] {
    return [{
      columnName: 'test',
      displayName: 'test',
      isSortable: false,
      cell: (element: TableModel) => `${element.tableField}`
    }]
  }

  static generateModel(): TableModel {
    return {
      id: 0,
      tableField: 'test'
    }
  }

  static generateForm(): FormModelGroup<TableModel> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      tableField: new FormControl('', {nonNullable: true})
    });
  }

  static generateDialogInput(model: TableModel, action: EditAction): EditDialogInput<TableModel, MockTableEditorComponent> {
    return {
      title: 'test title',
      context: model,
      action: action,
      editorConfig: {
        component: MockTableEditorComponent,
        form: this.generateForm()
      }
    }
  }
}
