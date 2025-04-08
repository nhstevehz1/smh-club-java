import { Component } from '@angular/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {MemberService} from '@app/features/members/services/member.service';

@Component({
  selector: 'app-view-member',
  imports: [],
  providers: [MemberService, AuthService],
  templateUrl: './view-member.component.html',
  styleUrl: './view-member.component.scss'
})
export class ViewMemberComponent {

}
