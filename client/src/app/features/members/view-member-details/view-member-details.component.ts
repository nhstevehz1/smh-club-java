import {Component, input, signal, OnInit} from '@angular/core';
import {Member} from '@app/features/members/models';
import {ViewMemberComponent} from '@app/features/members/view-member/view-member.component';
import {ViewAddressListComponent} from '@app/features/addresses/view-address-list/view-address-list.component';
import {ViewEmailListComponent} from '@app/features/emails/view-email-list/view-email-list.component';
import {ViewPhoneListComponent} from '@app/features/phones/view-phone-list/view-phone-list.component';
import {ViewRenewalListComponent} from '@app/features/renewals/view-renewal-list/view-renewal-list.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MatDivider} from '@angular/material/divider';
import {MatExpansionModule} from '@angular/material/expansion';
import {TranslatePipe} from '@ngx-translate/core';
import {MatIconModule} from '@angular/material/icon';
import {Location} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-view-member-details',
  imports: [
    ViewMemberComponent,
    ViewAddressListComponent,
    ViewEmailListComponent,
    ViewPhoneListComponent,
    ViewRenewalListComponent,
    MatDivider,
    MatExpansionModule,
    MatIconModule,
    MatButtonModule,
    TranslatePipe,
    MatTooltip
  ],
  providers: [MemberService],
  templateUrl: './view-member-details.component.html',
  styleUrl: './view-member-details.component.scss'
})
export class ViewMemberDetailsComponent implements OnInit {
  id = input.required<number>();
  member = signal<Member | undefined>(undefined);

  constructor(private apiSvc: MemberService,
              private location: Location) {}

  ngOnInit(): void {
    this.apiSvc.get(this.id()).subscribe({
      next: member => this.member.update(() => member)
    });
  }

  onBack(): void {
    this.location.back();
  }
}
