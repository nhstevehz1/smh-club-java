import { Component } from '@angular/core';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';

@Component({
  selector: 'app-test-editor',
  imports: [],
  templateUrl: './mock-table-editor.component.html',
  styleUrl: './mock-table-editor.component.scss'
})
export class MockTableEditorComponent extends BaseEditorComponent<TableModel>{}
