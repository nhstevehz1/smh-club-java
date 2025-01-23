import {ListMembersComponent} from './list-members.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {MembersService} from "../services/members.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {generateMemberPageData} from "../test/member-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {throwError} from "rxjs";

describe('ListMembersComponent', () => {
  let fixture: ComponentFixture<ListMembersComponent>;
  let component: ListMembersComponent;
  let memberSvcMock: jasmine.SpyObj<MembersService>;

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj('MembersService', ['getMembers']);

    await TestBed.configureTestingModule({
      imports: [ListMembersComponent],
      providers: [
          {provide: MembersService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideComponent(ListMembersComponent,
        { set: {providers: [{provide: MembersService, useValue: memberSvcMock}]}}
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
  });
});
