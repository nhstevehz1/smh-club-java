import {Component, input, computed, WritableSignal, signal} from '@angular/core';
import {DateTime} from 'luxon';
import {TranslateService, LangChangeEvent} from '@ngx-translate/core';

import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {DateTimeToLocalPipe, DateTimeToFormatPipe} from '@app/shared/pipes';
import {Renewal} from '@app/features/renewals/models';

@Component({
  selector: 'app-view-renewal',
  imports: [
    DataDisplayComponent,
    DateTimeToLocalPipe,
    DateTimeToFormatPipe
  ],
  templateUrl: './view-renewal.component.html',
  styleUrl: './view-renewal.component.scss'
})
export class ViewRenewalComponent {
  renewal = input.required<Renewal>();
  year = computed(() => this.renewal().renewal_year);
  date = computed(() => this.renewal().renewal_date);

  lang: WritableSignal<string>;

  protected readonly DateTime = DateTime;

  constructor(translate: TranslateService) {
    this.lang = signal(translate.currentLang);

    translate.onLangChange.subscribe({
      next: (lce: LangChangeEvent) => this.lang.update(() => lce.lang)
    });
  }
}
