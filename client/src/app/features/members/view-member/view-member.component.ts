import {Component, signal, computed, WritableSignal, model} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatDividerModule} from '@angular/material/divider';
import {MatExpansionModule} from '@angular/material/expansion';
import {DateTime} from 'luxon';
import {TranslateService, LangChangeEvent} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {MemberService} from '@app/features/members/services/member.service';
import {Member} from '@app/features/members/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {DateTimeToLocalPipe, DateTimeToFormatPipe} from '@app/shared/pipes';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';

@Component({
  selector: 'app-view-member',
  imports: [
    DataDisplayComponent,
    DateTimeToLocalPipe,
    DateTimeToFormatPipe,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatDividerModule,
    MatExpansionModule,
    ViewModelComponent
  ],
  providers: [MemberService, AuthService],
  templateUrl: './view-member.component.html',
  styleUrl: './view-member.component.scss'
})
export class ViewMemberComponent {
  member = model.required<Member>();

  number = computed(() => this.member()?.member_number);
  birthDate = computed(() => this.member().birth_date);
  joinedDate = computed(() => this.member().joined_date);

  fullName = computed<string>(() => {
    const member = this.member();
    const middle = member.middle_name  ? ` ${member.middle_name}` : '';
    const suffix = member?.suffix  ? ` ${member.suffix}` : '';
    return `${member.first_name}${middle} ${member?.last_name}${suffix}`;
  });

  lang: WritableSignal<string>;

  protected readonly DateTime = DateTime;

  constructor(translate: TranslateService) {
    this.lang = signal(translate.currentLang);
    translate.onLangChange.subscribe({
      next: (lce: LangChangeEvent) => this.lang.update(() => lce.lang)
    });
  }
}
