import {Component, input, output, model} from '@angular/core';
import {Updatable} from '@app/shared/models/updatable';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-view-model',
  imports: [
    MatIconButton,
    MatIcon,
    MatTooltip
  ],
  templateUrl: './view-model.component.html',
  styleUrl: './view-model.component.scss'
})
export class ViewModelComponent<T extends Updatable> {
  item = model.required<T>();

  allowEdit = input(false);
  allowDelete = input(false);

  editClick = output<T>();
  deleteClick = output<T>();

  onEdit(): void {
    this.editClick.emit(this.item());
  }

  onDelete(): void {
    this.deleteClick.emit(this.item());
  }
}
