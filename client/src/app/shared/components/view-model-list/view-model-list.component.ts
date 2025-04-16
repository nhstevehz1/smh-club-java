import {Component, input, output, model} from '@angular/core';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/divider';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';
import {T} from '@angular/cdk/keycodes';

@Component({
  selector: 'app-view-model-list',
  imports: [
    MatIconButton,
    MatIcon,
    MatDivider,
    ViewModelComponent
  ],
  templateUrl: './view-model-list.component.html',
  styleUrl: './view-model-list.component.scss'
})
export class ViewModelListComponent<T> {
  list = model<T[]>();
  allowAdd = input(false);
  allowItemEdit = input(false);
  allowItemDelete = input(false);

  addClicked = output<void>();
  editItemClicked = output<T>();
  deleteItemClicked = output<T>();

  onAddItem(): void {
    this.addClicked.emit();
  }

  onEdItItem(item: T) {
    this.editItemClicked.emit(item);
  }

  onDeleteItem(item: T): void {
    this.deleteItemClicked.emit(item);
  }
}
