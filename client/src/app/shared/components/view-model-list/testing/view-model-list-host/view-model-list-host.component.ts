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
  list = model<ViewListModel[]>([]);
  allowAdd = input(false);

  onAdd(): void {
    console.trace('view model list, onAdd');
  }
}
