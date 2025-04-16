import {Component, model, input} from '@angular/core';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';
import {ViewListModel} from '@app/shared/components/view-model-list/testing/test-support';

@Component({
  selector: 'app-view-model-list-host',
  imports: [
    ViewModelListComponent
  ],
  templateUrl: './view-model-list-host.component.html'
})
export class ViewModelListHostComponent {
  models = model<ViewListModel[]>([]);
  allowAdd = input(false);
  allowEdit = input(false);
  allowDelete = input(false);

  onAdd(): void {
    console.trace('view model list, onAdd');
  }

  onEditItem(item: ViewListModel) {
    console.trace('view model list, onEditItem', item);
  }

  onDeleteItem(item: ViewListModel) {
    console.trace('view model list, onDeleteItem', item);
  }
}
