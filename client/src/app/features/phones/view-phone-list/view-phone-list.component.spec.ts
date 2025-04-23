import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewPhoneListComponent} from './view-phone-list.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {AuthService} from '@app/core/auth/services/auth.service';
import {ViewModelListHarness} from '@app/shared/components/view-model-list/testing/view-model-list-harness';
import {ViewModelHarness} from '@app/shared/components/view-model-component/testing/view-model-harness';
import {PhoneEditDialogService} from '@app/features/phones/services/phone-edit-dialog.service';
import {PhoneService} from '@app/features/phones/services/phone.service';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {asyncData} from '@app/shared/testing';
import {PhoneTest} from '@app/features/phones/testing';

describe('ViewPhoneListComponent', () => {
  let component: ViewPhoneListComponent;
  let fixture: ComponentFixture<ViewPhoneListComponent>;
  let loader: HarnessLoader;

  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<PhoneEditDialogService>;
  let apiSvcMock: jasmine.SpyObj<PhoneService>;

  let harness: ViewModelListHarness;
  let modelHarnesses: ViewModelHarness[];

  let memberId: number;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    dialogSvcMock = jasmine.createSpyObj('PhoneEditDialogService',
      ['openDialog', 'generateDialogInput']);

    apiSvcMock = jasmine.createSpyObj('PhoneService', ['getAllByMember']);

    await TestBed.configureTestingModule({
      imports: [ViewPhoneListComponent],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: PhoneEditDialogService, useValue: {}},
        {provide: PhoneService, useValue: {}}
      ]
    }).overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(PhoneEditDialogService, {useValue: dialogSvcMock})
      .overrideProvider(PhoneService, {useValue: apiSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ViewPhoneListComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    fixture.componentRef.setInput('memberId', 0);
    expect(component).toBeTruthy();
  });

  describe('component interactions', () => {
    beforeEach(async () => {
      const phone = PhoneTest.generatePhone();
      fixture.componentRef.setInput('memberId', phone.member_id);

      // must set to true for buttons to be rendered
      authSvcMock.hasPermission.and.returnValue(true);
      apiSvcMock.getAllByMember.and.returnValue(asyncData([phone]));

      harness = await loader.getHarness(ViewModelListHarness);
      modelHarnesses = await loader.getAllHarnesses(ViewModelHarness);
    });

    it('should call onAddItem when add phone click', async () => {
      const spy = spyOn(component, 'onAddItem').and.stub();
      await harness.addClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onEditItem when edit phone click', async () => {
      const spy = spyOn(component, 'onEditItem').and.stub();
      await modelHarnesses[0].editClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onDeleteItem when delete phone click', async() => {
      const spy = spyOn(component, 'onDeleteItem').and.stub();
      await modelHarnesses[0].deleteClick();
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('test phone view list init', () => {
    beforeEach(() => {
      memberId = 5;
      fixture.componentRef.setInput('memberId', memberId);
    });

    it('should call auth service hasPermission', async () => {
      apiSvcMock.getAllByMember.and.returnValue(asyncData([]));
      const spy = authSvcMock.hasPermission.and.returnValue(false);

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalled();
    });

    it('should call api service getAllByMember', async () => {
      authSvcMock.hasPermission.and.returnValue(false);
      const spy = apiSvcMock.getAllByMember.and.returnValue(asyncData([]));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalledWith(memberId);
    });
  });
});
