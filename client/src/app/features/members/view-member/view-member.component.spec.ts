import { ComponentFixture, TestBed } from '@angular/core/testing';
import {Location} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

import { ViewMemberComponent } from './view-member.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTest} from '@app/features/members/testing/test-support';
import {asyncData} from '@app/shared/testing';
import {Member} from '@app/features/members/models';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';

describe('ViewMemberComponent', () => {
  let component: ViewMemberComponent;
  let fixture: ComponentFixture<ViewMemberComponent>;
  let loader: HarnessLoader;

  let apiSvcMock: jasmine.SpyObj<MemberService>;
  let locationMock: jasmine.SpyObj<Location>;

  let member: Member;

  beforeEach(async () => {
    apiSvcMock = jasmine.createSpyObj('MemberService',
      ['get', 'getAddresses', 'getEmails', 'getPhones', 'getRenewals']);

    locationMock = jasmine.createSpyObj('Location', ['back']);

    await TestBed.configureTestingModule({
      imports: [
        ViewMemberComponent,
        TranslateModule.forRoot({})
      ], providers: [
        {provide: MemberService, useValue: {}},
        {provide: Location, useValue: {}}
      ]
    }).overrideProvider(MemberService, {useValue: apiSvcMock})
      .overrideProvider(Location, {useValue: locationMock})
    .compileComponents();

    fixture = TestBed.createComponent(ViewMemberComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);

    member = MemberTest.generateMember(1);
    fixture.componentRef.setInput('id', member.id);

    apiSvcMock.getAddresses.and.returnValue(asyncData([]));
    apiSvcMock.getEmails.and.returnValue(asyncData([]));
    apiSvcMock.getPhones.and.returnValue(asyncData([]));
    apiSvcMock.getRenewals.and.returnValue(asyncData([]));
  });

  fit('should create', async () => {
    apiSvcMock.get.and.returnValue(asyncData(member));

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  describe('interactions on init', () => {
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

  describe('component interactions', () => {
    beforeEach(async () => {
      apiSvcMock.get.and.returnValue(asyncData(member));
      fixture.detectChanges();
      await fixture.whenStable();
    });

    fit('should contain back button', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', text: 'arrow_back'}));
      expect(harness).toBeTruthy();
    });

    fit('back button click should call onBack', async () => {
      const spy = spyOn(component, 'onBack').and.stub();
      const harness =
        await loader.getHarness(MatButtonHarness.with({variant: 'icon', text: 'arrow_back'}));

      await harness.click();

      expect(spy).toHaveBeenCalled();
    });

    fit('onBack should call Location.back', async () => {
      const spy = locationMock.back.and.stub();
      component.onBack();
      expect(spy).toHaveBeenCalled();
    });

    fit('should contain name')
  });
});
