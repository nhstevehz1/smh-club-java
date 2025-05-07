import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMemberDetailsComponent } from './view-member-details.component';
import {TranslateModule} from '@ngx-translate/core';
import {MemberService} from '@app/features/members/services/member.service';
import {Location} from '@angular/common';
import {Member} from '@app/features/members/models';
import {asyncData} from '@app/shared/testing';
import {MemberTest} from '@app/features/members/testing/test-support';
import {async} from 'rxjs';

describe('ViewMemberDetailsComponent', () => {
  let component: ViewMemberDetailsComponent;
  let fixture: ComponentFixture<ViewMemberDetailsComponent>;

  let svcMock: jasmine.SpyObj<MemberService>;
  let locationMock: jasmine.SpyObj<Location>;

  let member: Member;

  beforeEach(async () => {
    svcMock = jasmine.createSpyObj('MemberService', ['get']);
    locationMock = jasmine.createSpyObj('Location', ['back']);

    await TestBed.configureTestingModule({
      imports: [
        ViewMemberDetailsComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: MemberService, useValue: {}},
        {provide: Location, useValue: {}}
      ]
    }).overrideProvider(MemberService, {useValue: svcMock})
      .overrideProvider(Location, {useValue: svcMock})
    .compileComponents();

    fixture = TestBed.createComponent(ViewMemberDetailsComponent);
    component = fixture.componentInstance;
  });

  describe('test init', () => {
    beforeEach(() => {
      fixture.componentRef.setInput('id', 0);
      member = MemberTest.generateMember(0);
    });

    it('should create', async () => {
      svcMock.get.and.returnValue(asyncData(member))
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component).toBeTruthy();
    });

    it('should call MemberService.get on init', async () => {
      const spy = svcMock.get.and.returnValue(asyncData(member));
      fixture.detectChanges();
      await fixture.whenStable();
      expect(spy).toHaveBeenCalledWith(0);
    })
  });
});
