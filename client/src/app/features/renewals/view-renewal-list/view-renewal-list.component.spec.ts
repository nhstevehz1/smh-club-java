import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewRenewalListComponent} from './view-renewal-list.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {AuthService} from '@app/core/auth/services/auth.service';
import {ViewModelListHarness} from '@app/shared/components/view-model-list/testing/view-model-list-harness';
import {ViewModelHarness} from '@app/shared/components/view-model-component/testing/view-model-harness';
import {RenewalEditDialogService} from '@app/features/renewals/services/renewal-edit-dialog.service';
import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {asyncData} from '@app/shared/testing';
import {RenewalTest} from '@app/features/renewals/testing/test-support';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';

describe('ViewRenewalListComponent', () => {
  let component: ViewRenewalListComponent;
  let fixture: ComponentFixture<ViewRenewalListComponent>;
  let loader: HarnessLoader;

  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<RenewalEditDialogService>;
  let apiSvcMock: jasmine.SpyObj<RenewalService>;

  let harness: ViewModelListHarness;
  let modelHarnesses: ViewModelHarness[];

  let memberId: number;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    dialogSvcMock = jasmine.createSpyObj('RenewalEditDialogService',
      ['openDialog', 'generateDialogInput']);

    apiSvcMock = jasmine.createSpyObj('RenewalService', ['getAllByMember']);

    await TestBed.configureTestingModule({
      imports: [ViewRenewalListComponent],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: RenewalEditDialogService, useValue: {}},
        {provide: RenewalService, useValue: {}}
      ]
    }).overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(RenewalEditDialogService, {useValue: dialogSvcMock})
      .overrideProvider(RenewalService, {useValue: apiSvcMock})
    .compileComponents();

    fixture = TestBed.createComponent(ViewRenewalListComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    fixture.componentRef.setInput('memberId', 0);
    expect(component).toBeTruthy();
  });

  describe('component interactions', () => {
    beforeEach(async () => {
      const renewal = RenewalTest.generateRenewal();
      fixture.componentRef.setInput('memberId', renewal.member_id);

      // must set to true for buttons to be rendered
      authSvcMock.hasPermission.and.returnValue(true);
      apiSvcMock.getAllByMember.and.returnValue(asyncData([renewal]));

      harness = await loader.getHarness(ViewModelListHarness);
      modelHarnesses = await loader.getAllHarnesses(ViewModelHarness);
    });

    it('should call onAddItem when add renewal click', async () => {
      const spy = spyOn(component, 'onAddItem').and.stub();
      await harness.addClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onEditItem when edit renewal click', async () => {
      const spy = spyOn(component, 'onEditItem').and.stub();
      await modelHarnesses[0].editClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onDeleteItem when delete renewal click', async() => {
      const spy = spyOn(component, 'onDeleteItem').and.stub();
      await modelHarnesses[0].deleteClick();
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('test renewal view list init', () => {
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
