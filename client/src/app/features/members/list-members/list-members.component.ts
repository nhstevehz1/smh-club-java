import {Component} from '@angular/core';
import {Router} from '@angular/router';

import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatTooltip} from '@angular/material/tooltip';

import {TranslatePipe, TranslateService} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {DateTimeToFormatPipe} from '@app/shared/pipes';

import {MemberCreate, Member} from '@app/features/members/models/member';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTableService} from '@app/features/members/services/member-table.service';
import {MemberEditDialogService} from '@app/features/members/services/member-edit-dialog.service';
import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditEvent, EditAction} from '@app/shared/components/base-edit-dialog/models';
import {MemberEditorComponent} from '@app/features/members/member-editor/member-editor.component';

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
export class ListMembersComponent extends BaseTableComponent<MemberCreate, Member, Member, MemberEditorComponent> {

  constructor(auth: AuthService,
              svc: MemberService,
              tableSvc: MemberTableService,
              dialogSvc: MemberEditDialogService,
              private router: Router) {

    super(auth, svc, tableSvc, dialogSvc);
  }

  onAddMemberClick(): void {

  }

  onViewClick(event: EditEvent<Member>): void {
    this.router.navigate(['p/members/view', event.data.id]).then();
  }
}
