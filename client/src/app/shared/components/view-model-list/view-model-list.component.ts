import {Component, model, input, output, OnInit, Signal} from '@angular/core';
import {T} from '@angular/cdk/keycodes';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {FilterService} from '@app/shared/components/view-model-list/models/filter-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
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

  filterId = input.required<number>();

  list: Signal<T[]>;
  showAddButton = input(false);
  addClicked = output<void>();

  onAddItem(): void {
    this.addClicked.emit();
  }

  constructor(private filterSvc: FilterService<T>) {
    this.list =
      toSignal(this.filterSvc.getAllByFilterId(this.filterId()), {initialValue: []});
  }
}
