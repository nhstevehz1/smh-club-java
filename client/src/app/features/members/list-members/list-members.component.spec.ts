import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';

import {throwError} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {AuthService, PermissionType} from '@app/core/auth';

import {asyncData} from '@app/shared/testing/test-helpers';
import {DateTimeToFormatPipe} from '@app/shared/pipes';
import {PageRequest} from '@app/shared/services';

import {generateMemberPageData} from '@app/features/members/testing/test-support';

import {ListMembersComponent} from './list-members.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTableService} from '@app/features/members/services/member-table.service';

describe('ListMembersComponent', () => {
  let fixture: ComponentFixture<ListMembersComponent>;
  let component: ListMembersComponent;

  let memberSvcMock: jasmine.SpyObj<MemberService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let tableSvcMock: jasmine.SpyObj<MemberTableService>;
  let routerMock: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj('MembersService', ['getPagedData']);
    tableSvcMock = jasmine.createSpyObj('MemberTableService', ['getColumnDefs']);
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission', 'rolesLoaded$']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        ListMembersComponent,
        TranslateModule.forRoot({}),
      ],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
        DateTimeToFormatPipe,
        {provide: Router, useValue: routerMock},
        {provide: MemberService, useValue: {}},
        {provide: MemberTableService, useValue: {}},
        {provide: AuthService, useValue: {}}
      ],
    }).overrideProvider(MemberService, {useValue: memberSvcMock})
      .overrideProvider(MemberTableService, {useValue: tableSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListMembersComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
      it('should create', () => {
          expect(component).toBeTruthy();
      });

      it('should create column list', () => {
         fixture.detectChanges();
         expect(component.columns().length).toEqual(5);
      });
  });

  describe('test service interactions on init', () => {
      let loader: HarnessLoader;

      beforeEach(() => {
         loader = TestbedHarnessEnvironment.loader(fixture);
      });

      it('should call MemberService.getMembers() on init', async () => {
         const data = generateMemberPageData(0, 5, 100);

         memberSvcMock.getPagedData.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         const request = PageRequest.of(0, 5);
         expect(memberSvcMock.getPagedData).toHaveBeenCalledWith(request);
     });

    it('length should be set on init', async () => {
       const data = generateMemberPageData(0, 5, 100);

       memberSvcMock.getPagedData.and.returnValue(asyncData(data));

       fixture.detectChanges();
       await fixture.whenStable();

       expect(component.resultsLength).toEqual(data.page.totalElements);
    });

    it('datasource.data should be set on init', async () => {
        const data = generateMemberPageData(0, 5, 2);

        memberSvcMock.getPagedData.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
      memberSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource().data).toEqual([]);
    });

    it('should call canAddMember', async () => {
      const data = generateMemberPageData(0, 5, 2);

      const spy = spyOn(component, 'hasWriteRole').and.stub();
      memberSvcMock.getPagedData.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalled();
    });

    it('should call authService.hasPermissions', async () => {
      const data = generateMemberPageData(0, 5, 2);

      const spy = authSvcMock.hasPermission.and.stub();
      memberSvcMock.getPagedData.and.returnValue(asyncData(data));
      spyOn(component, 'hasWriteRole').and.callThrough();

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalledWith(PermissionType.write);
    });

    it('should display add member button', async () => {
      const data = generateMemberPageData(0, 5, 2);

      memberSvcMock.getPagedData.and.returnValue(asyncData(data));
      spyOn(component, 'hasWriteRole').and.returnValue(true);

      const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));

      expect(harnesses.length).toBe(1);
    });

    it('add member button should call addMemberHandler() when clicked', async () => {
      const data = generateMemberPageData(0, 5, 2);

      const spy = spyOn(component, 'addMemberHandler').and.stub();
      memberSvcMock.getPagedData.and.returnValue(asyncData(data));
      spyOn(component, 'hasWriteRole').and.returnValue(true);

      const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));
      await harnesses[0].click();

      expect(spy).toHaveBeenCalled();
    });

    it('router.navigate should be called when add member button is clicked', async () => {
      const data = generateMemberPageData(0, 5, 2);

      const spy = routerMock.navigate.and.returnValue(Promise.resolve(true))
      memberSvcMock.getPagedData.and.returnValue(asyncData(data));
      spyOn(component, 'hasWriteRole').and.returnValue(true);

      const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));
      await harnesses[0].click();

      expect(spy).toHaveBeenCalledWith(['p/members/add']);
    });
  });
});
