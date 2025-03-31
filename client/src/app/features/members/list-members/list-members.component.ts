import {Component} from '@angular/core';
import {Router} from '@angular/router';

import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';

import {TranslatePipe, TranslateService} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component';
import {DateTimeToFormatPipe} from '@app/shared/pipes';
import {EditEvent} from '@app/shared/components/edit-dialog';

import {
  Member, MemberCreate, MemberEditDialogService, MemberService, MemberTableService
} from '@app/features/members';


@Component({
  selector: 'app-list-members',
  imports: [
    SortablePageableTableComponent,
    MatIconModule,
    MatButtonModule,
    MatTooltip,
    TranslatePipe,
  ],
    providers: [
      MemberService,
      MemberTableService,
      TranslateService,
      DateTimeToFormatPipe
    ],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent extends BaseTableComponent<MemberCreate, Member, Member> {

  constructor(auth: AuthService,
              svc: MemberService,
              tableSvc: MemberTableService,
              dialogSvc: MemberEditDialogService,
              private router: Router) {

    super(auth, svc, tableSvc, dialogSvc);
  }

  addMemberHandler(): void {
    this.router.navigate(['p/members/add']).then();
  }

  onViewClick(event: EditEvent<Member>): void {
    this.router.navigate(['p/member/view', event.data.id]).then();
  }
}
