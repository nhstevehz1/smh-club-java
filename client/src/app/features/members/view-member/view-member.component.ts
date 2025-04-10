import {Component, signal, OnInit, input, computed, WritableSignal} from '@angular/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {MemberService} from '@app/features/members/services/member.service';
import {Member} from '@app/features/members/models';
import {Address} from '@app/features/addresses/models';
import {Phone} from '@app/features/phones/models';
import {Email} from '@app/features/emails/models';
import {Renewal} from '@app/features/renewals/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {DateTimeToLocalPipe, DateTimeToFormatPipe} from '@app/shared/pipes';
import {DateTime} from 'luxon';
import {TranslateService, LangChangeEvent} from '@ngx-translate/core';
import {single} from 'rxjs';

@Component({
  selector: 'app-view-member',
  imports: [
    DataDisplayComponent,
    DateTimeToLocalPipe,
    DateTimeToFormatPipe
  ],
  providers: [MemberService, AuthService],
  templateUrl: './view-member.component.html',
  styleUrl: './view-member.component.scss'
})
export class ViewMemberComponent implements OnInit {

  id = input.required<number>();

  member = signal<Member | undefined>(undefined);
  number = computed(() => this.member()?.member_number);
  birthDate = computed(() => this.member()?.birth_date);
  joinedDate = computed(() => this.member()?.joined_date);
  lang: WritableSignal<string>;

  fullName = computed<string>(() => {
    const member = this.member();
    const middle = member?.middle_name  ? ` ${member.middle_name}` : '';
    const suffix = member?.suffix  ? ` ${member.suffix}` : '';
    return `${member?.first_name}${middle} ${member?.last_name}${suffix}`;
  });

  addresses = signal<Address[]>([]);
  phones = signal<Phone[]>([]);
  emails = signal<Email[]>([]);
  renewals = signal<Renewal[]>([]);

  constructor(private memberSvc: MemberService,
              translate: TranslateService) {

    this.lang = signal(translate.currentLang);
    translate.onLangChange.subscribe({
      next: (lce: LangChangeEvent) => this.lang.update(() => lce.lang)
    })
  }

  ngOnInit(): void {
    this.memberSvc.get(this.id()).subscribe({
      next: member => {
        console.debug('svc', typeof member.birth_date);
        this.member.set(member);
      },
      error: err => console.debug(err) // TODO: display error
    });
  }

  protected readonly DateTime = DateTime;
}
