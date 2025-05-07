import {Component, input, output, model} from '@angular/core';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDivider} from '@angular/material/divider';
import {Updatable} from '@app/shared/models/updatable';
import {MatTooltip} from '@angular/material/tooltip';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-view-model-list',
  imports: [
    MatIconButton,
    MatIcon,
    MatDivider,
    MatTooltip,
    TranslatePipe
  ],
  templateUrl: './view-model-list.component.html',
  styleUrl: './view-model-list.component.scss'
})
export class ViewModelListComponent<T extends Updatable> {
  list = model.required<T[]>();
  allowAdd = input(false);
  addTooltip = input('modelView.add')
  addClicked = output<void>();

  onAddItem(): void {
    this.addClicked.emit();
  }
}
