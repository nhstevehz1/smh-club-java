import { Component } from '@angular/core';

import {AuthService} from '@app/core/auth';

import {MemberService} from '@app/features/members';

@Component({
  selector: 'app-view-member',
  imports: [],
  providers: [MemberService, AuthService],
  templateUrl: './view-member.component.html',
  styleUrl: './view-member.component.scss'
})
export class ViewMemberComponent {

  constructor(svc: MemberService,
              auth: AuthService){}


}
