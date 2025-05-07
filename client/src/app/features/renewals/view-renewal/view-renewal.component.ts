import {Component, computed, WritableSignal, signal, model, input, output} from '@angular/core';
import {DateTime} from 'luxon';
import {TranslateService, LangChangeEvent, TranslatePipe} from '@ngx-translate/core';

import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {DateTimeToLocalPipe, DateTimeToFormatPipe} from '@app/shared/pipes';
import {Renewal} from '@app/features/renewals/models';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';

@Component({
  selector: 'app-view-renewal',
  imports: [
    DataDisplayComponent,
    DateTimeToLocalPipe,
    DateTimeToFormatPipe,
    ViewModelComponent,
    TranslatePipe
  ],
  templateUrl: './view-renewal.component.html',
  styleUrl: './view-renewal.component.scss'
})
export class ViewRenewalComponent {
  allowEdit = input(false);
  allowDelete = input(false);

  renewal = model.required<Renewal>();
  year = computed(() => this.renewal().renewal_year);
  date = computed(() => this.renewal().renewal_date);

  lang: WritableSignal<string>;

  editClick = output<Renewal>();
  deleteClick = output<Renewal>();

  protected readonly DateTime = DateTime;

  constructor(translate: TranslateService) {
    this.lang = signal(translate.currentLang);

    translate.onLangChange.subscribe({
      next: (lce: LangChangeEvent) => this.lang.update(() => lce.lang)
    });
  }

  onEdit(): void {
    this.editClick.emit(this.renewal());
  }

  onDelete(): void {
    this.deleteClick.emit(this.renewal());
  }
}
