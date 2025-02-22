import {ListMembersComponent} from './list-members.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {MembersService} from "../services/members.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {generateMemberPageData} from "../test/member-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {AuthService} from "../../../core/auth/services/auth.service";
import {DateTimeToFormatPipe} from "../../../shared/pipes/luxon/date-time-to-format.pipe";
import {PermissionType} from "../../../core/auth/models/permission-type";
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatButtonHarness} from "@angular/material/button/testing";
import {Router} from "@angular/router";

describe('ListMembersComponent', () => {
  let fixture: ComponentFixture<ListMembersComponent>;
  let component: ListMembersComponent;

  let memberSvcMock: jasmine.SpyObj<MembersService>;
  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dtFormatMock: jasmine.SpyObj<DateTimeToFormatPipe>
  let routerMock: jasmine.SpyObj<Router>;

   let rolesLoadedSubjectMock$: BehaviorSubject<boolean>;
  let rolesLoadedMock$: Observable<boolean>;

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj('MembersService', ['getMembers']);
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission', 'rolesLoaded$']);
    dtFormatMock = jasmine.createSpyObj('DateTimeToFormatPipe', ['transform'])
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    rolesLoadedSubjectMock$ = new BehaviorSubject(false);
    rolesLoadedMock$ = rolesLoadedSubjectMock$.asObservable();
    authSvcMock.rolesLoaded$ = rolesLoadedMock$;

    await TestBed.configureTestingModule({
      imports: [ListMembersComponent],
      providers: [
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations(),
          {provide: Router, useValue: routerMock},
          {provide: DateTimeToFormatPipe, useValue: dtFormatMock},
          {provide: MembersService, useValue: {}},
          {provide: AuthService, useValue: {}}
      ],
    }).overrideComponent(ListMembersComponent,
        { set: {providers: [
            {provide: MembersService, useValue: memberSvcMock},
            {provide: AuthService, useValue: authSvcMock}
        ]}}
    ).compileComponents();

    fixture = TestBed.createComponent(ListMembersComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
      it('should create', () => {
          expect(component).toBeTruthy();
      });

      it('should create column list', () => {
         fixture.detectChanges();
         expect(component.columns.length).toEqual(5);
      });
  });

  describe('test service interactions on init', () => {
      let loader: HarnessLoader;

      beforeEach(() => {
         loader = TestbedHarnessEnvironment.loader(fixture);
      });

      it('should call MemberService.getMembers() on init', async () => {
         const data = generateMemberPageData(0, 5, 100);
         memberSvcMock.getMembers.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         const request = PageRequest.of(0, 5);
         expect(memberSvcMock.getMembers).toHaveBeenCalledWith(request);
     });

    it('length should be set on init', async () => {
       const data = generateMemberPageData(0, 5, 100);
       memberSvcMock.getMembers.and.returnValue(asyncData(data));

       fixture.detectChanges();
       await fixture.whenStable();

       expect(component.resultsLength).toEqual(data.page.totalElements);
    });

    it('datasource.data should be set on init', async () => {
        const data = generateMemberPageData(0, 5, 2);
        memberSvcMock.getMembers.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource.data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
      memberSvcMock.getMembers.and.returnValue(throwError(() => 'error'));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource.data).toEqual([]);
    });

    it('should call canAddPermissions', async () => {
        const data = generateMemberPageData(0, 5, 2);
        memberSvcMock.getMembers.and.returnValue(asyncData(data));
        const spy = spyOn(component, 'canAddMember').and.stub();

        rolesLoadedSubjectMock$.next(true);

        fixture.detectChanges();
        await fixture.whenStable();

      expect(spy).toHaveBeenCalled();
    });

      it('should call authService.hasPermissions', async () => {
          const data = generateMemberPageData(0, 5, 2);
          memberSvcMock.getMembers.and.returnValue(asyncData(data));
          spyOn(component, 'canAddMember').and.callThrough();
          const spy = authSvcMock.hasPermission.and.stub();

          rolesLoadedSubjectMock$.next(true);

          fixture.detectChanges();
          await fixture.whenStable();

          expect(spy).toHaveBeenCalledWith(PermissionType.write);
      });

      it('should display add member button', async () => {
          const data = generateMemberPageData(0, 5, 2);
          memberSvcMock.getMembers.and.returnValue(asyncData(data));

          spyOn(component, 'canAddMember').and.returnValue(true);
          rolesLoadedSubjectMock$.next(true);

          const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));

          expect(harnesses.length).toBe(1);
      });

      it('add member button should call addMemberHandler() when clicked', async () => {
          const data = generateMemberPageData(0, 5, 2);
          memberSvcMock.getMembers.and.returnValue(asyncData(data));

          spyOn(component, 'canAddMember').and.returnValue(true);
          rolesLoadedSubjectMock$.next(true);

          const spy = spyOn(component, 'addMemberHandler').and.stub();

          const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));
          await harnesses[0].click();

          expect(spy).toHaveBeenCalled();
      });

      it('router.navigate should be called when add member button is clicked', async () => {
          const data = generateMemberPageData(0, 5, 2);
          memberSvcMock.getMembers.and.returnValue(asyncData(data));

          spyOn(component, 'canAddMember').and.returnValue(true);
          rolesLoadedSubjectMock$.next(true);

          const spy = routerMock.navigate.and.returnValue(Promise.resolve(true))

          const harnesses = await loader.getAllHarnesses(MatButtonHarness.with({variant: 'mini-fab'}));
          await harnesses[0].click();

          expect(spy).toHaveBeenCalledWith(['p/members/add']);
      });

  });
});
