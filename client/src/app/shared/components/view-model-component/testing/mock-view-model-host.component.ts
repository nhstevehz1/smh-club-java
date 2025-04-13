import {Component, input, signal} from '@angular/core';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';
import {ViewModelTest, ViewModel} from '@app/shared/components/view-model-component/testing/view-model';

@Component({
  selector: 'app-mock-view-model-host',
  imports: [
    ViewModelComponent
  ],
  template: `<app-view-model
    [item]="model()"
    [showAllButtons]="showButtons()"
    [showEditButton]="showEdit()"
    [showDeleteButton]="showDelete()"
    (deleteClick)="onDelete(model())"
    (editClick)="onEdit(model())">
      <span>this is a test</span>
  </app-view-model>`
})
export class MockViewModelHostComponent {
 model = signal(ViewModelTest.generateModel(1));
 showButtons = input(false);
 showDelete = input(false);
 showEdit = input(false);

  onDelete(model: ViewModel) {
    console.debug('delete clicked', model);
  }

  onEdit(model: ViewModel) {
    console.debug('edit clicked', model);
  }
}
