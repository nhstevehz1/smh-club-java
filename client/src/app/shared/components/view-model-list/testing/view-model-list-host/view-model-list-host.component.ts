import {Component, model} from '@angular/core';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';
import {ViewListModel} from '@app/shared/components/view-model-list/testing/test-support';

@Component({
  selector: 'app-view-model-list-host',
  imports: [
    ViewModelListComponent
  ],
  templateUrl: './view-model-list-host.component.html',
  styleUrl: './view-model-list-host.component.scss'
})
export class ViewModelListHostComponent {
  models = model<ViewListModel[]>([]);
}
