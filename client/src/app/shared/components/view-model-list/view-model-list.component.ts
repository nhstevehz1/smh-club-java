import {Component, input, output, model} from '@angular/core';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/divider';

@Component({
  selector: 'app-view-model-list',
  imports: [
    MatIconButton,
    MatIcon,
    MatDivider
  ],
  templateUrl: './view-model-list.component.html',
  styleUrl: './view-model-list.component.scss'
})
export class ViewModelListComponent<T> {
  list = model<T[]>();
  showAddButton = input(false);
  addClicked = output<void>();

  onAddItem(): void {
    this.addClicked.emit();
  }
}
