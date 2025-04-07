import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';
import {TranslateModule} from '@ngx-translate/core';

import {asyncData} from '@app/shared/testing/test-helpers';
import {DateTimeToFormatPipe} from '@app/shared/pipes';

import {ListMembersComponent} from './list-members.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTableService} from '@app/features/members/services/member-table.service';
import {AuthService} from '@app/core/auth/services/auth.service';
import {MemberTest} from '@app/features/members/testing/test-support';
import {MemberEditDialogService} from '@app/features/members/services/member-edit-dialog.service';
import {EditEvent} from '@app/shared/components/base-edit-dialog/models';
import {Member} from '@app/features/members/models';

describe('ListMembersComponent', () => {
  let fixture: ComponentFixture<ListMembersComponent>;
  let component: ListMembersComponent;
  let loader: HarnessLoader;

  let memberSvcMock: jasmine.SpyObj<MemberService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let tableSvcMock: jasmine.SpyObj<MemberTableService>;
  let dialogSvcMock: jasmine.SpyObj<MemberEditDialogService>
  let routerMock: jasmine.SpyObj<Router>;

  const columnDefs = MemberTest.generateColumnDefs();
  const data = MemberTest.generatePagedData(0, 5, 2);

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj('MemberService', ['getPagedData']);
    tableSvcMock = jasmine.createSpyObj('MemberTableService', ['getColumnDefs']);
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    dialogSvcMock = jasmine.createSpyObj('MemberEditDialogService',
      ['openDialog', 'generateDialogInput']);

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
        {provide: AuthService, useValue: {}},
        {provide: MemberEditDialogService, useValue: {}}
      ],
    }).overrideProvider(MemberService, {useValue: memberSvcMock})
      .overrideProvider(MemberTableService, {useValue: tableSvcMock})
      .overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(MemberEditDialogService, {useValue: dialogSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListMembersComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);

    tableSvcMock.getColumnDefs.and.returnValue(columnDefs);
    memberSvcMock.getPagedData.and.returnValue(asyncData(data));

    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should display add member button when user has write role', async () => {
    spyOn(component, 'hasWriteRole').and.returnValue(true);
    const harness = await loader.getHarnessOrNull(MatButtonHarness.with({text: 'add', variant: 'mini-fab'}));
    expect(harness).toBeTruthy();
  });

  it('should NOT display add member button when user does not have write role', async () => {
    spyOn(component, 'hasWriteRole').and.returnValue(false);
    const harness = await loader.getHarnessOrNull(MatButtonHarness.with({text: 'add', variant: 'mini-fab'}));
    expect(harness).toBeFalsy();
  });

  it('router.navigate should be called when add member button is clicked', async () => {
    spyOn(component, 'hasWriteRole').and.returnValue(true);
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true))

    const harness = await loader.getHarness(MatButtonHarness.with({text: 'add', variant: 'mini-fab'}));
    await harness.click();

    expect(spy).toHaveBeenCalledWith(['p/members/add']);
  });

  it('onViewClicked should call router.navigate', async () => {
    const item: EditEvent<Member> = {idx: 0, data: MemberTest.generateMember(0)};
    const spy = routerMock.navigate.and.returnValue(Promise.resolve(true));

    fixture.detectChanges();
    await fixture.whenStable();

    component.onViewClick(item);
    expect(spy).toHaveBeenCalledWith(['p/members/view', item.data.id]);
  })
});
