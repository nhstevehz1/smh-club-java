import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMemberComponent } from './view-member.component';
import {TranslateModule} from '@ngx-translate/core';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTest} from '@app/features/members/testing/test-support';
import {asyncData} from '@app/shared/testing';
import {Member} from '@app/features/members/models';

describe('ViewMemberComponent', () => {
  let component: ViewMemberComponent;
  let fixture: ComponentFixture<ViewMemberComponent>;

  let apiSvcMock: jasmine.SpyObj<MemberService>;

  beforeEach(async () => {
    apiSvcMock = jasmine.createSpyObj('MemberService', ['get']);

    await TestBed.configureTestingModule({
      imports: [
        ViewMemberComponent,
        TranslateModule.forRoot({})
      ], providers: [
        {provide: MemberService, useValue: {}}
      ]
    }).overrideProvider(MemberService, {useValue: apiSvcMock})
    .compileComponents();

    fixture = TestBed.createComponent(ViewMemberComponent);
    component = fixture.componentInstance;
  });

  fit('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('interactions on init', () => {
    let member: Member;

    beforeEach(() => {
      member = MemberTest.generateMember(1);
      fixture.componentRef.setInput('id', member.id);
    });

    fit('should call MemberService.get', async ()=> {
      const spy = apiSvcMock.get.and.returnValue(asyncData(member));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalledWith(member.id);
    });

    fit('should set member', async () => {
      apiSvcMock.get.and.returnValue(asyncData(member));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.member()).toEqual(member);
    });

    fit('expect fullName to be correct', async ()=> {
      apiSvcMock.get.and.returnValue(asyncData(member));
      const fullName = `${member.first_name} ${member.middle_name} ${member.last_name} ${member.suffix}`

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.fullName()).toEqual(fullName);
    });
  });
});
