import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewAddressListComponent} from './view-address-list.component';
import {AuthService} from '@app/core/auth/services/auth.service';
import {AddressEditDialogService} from '@app/features/addresses/services/address-edit-dialog.service';
import {AddressService} from '@app/features/addresses/services/address.service';
import {asyncData} from '@app/shared/testing';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {AddressTest} from '@app/features/addresses/testing';
import {ViewModelListHarness} from '@app/shared/components/view-model-list/testing/view-model-list-harness';
import {ViewModelHarness} from '@app/shared/components/view-model-component/testing/view-model-harness';

describe('ViewAddressListComponent', () => {
  let component: ViewAddressListComponent;
  let fixture: ComponentFixture<ViewAddressListComponent>;
  let loader: HarnessLoader;

  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<AddressEditDialogService>;
  let apiSvcMock: jasmine.SpyObj<AddressService>;

  let harness: ViewModelListHarness;
  let modelHarnesses: ViewModelHarness[];

  let memberId: number;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    dialogSvcMock = jasmine.createSpyObj('AddressEditDialogService',
      ['openDialog', 'generateDialogInput']);

    apiSvcMock = jasmine.createSpyObj('AddressService', ['getAllByMember']);

    await TestBed.configureTestingModule({
      imports: [ViewAddressListComponent],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: AddressEditDialogService, useValue: {}},
        {provide: AddressService, useValue: {}}
      ]
    }).overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(AddressEditDialogService, {useValue: dialogSvcMock})
      .overrideProvider(AddressService, {useValue: apiSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(ViewAddressListComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('memberId', 0);
    expect(component).toBeTruthy();
  });

  describe('component interactions', () => {
    beforeEach(async () => {
      const address = AddressTest.generateAddress();
      fixture.componentRef.setInput('memberId', address.member_id);

      // must set to true for buttons to be rendered
      authSvcMock.hasPermission.and.returnValue(true);
      apiSvcMock.getAllByMember.and.returnValue(asyncData([address]));

      harness = await loader.getHarness(ViewModelListHarness);
      modelHarnesses = await loader.getAllHarnesses(ViewModelHarness);
    });

    it('should call onAddItem when add address click', async () => {
      const spy = spyOn(component, 'onAddItem').and.stub();
      await harness.addClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onEditItem when edit address click', async () => {
      const spy = spyOn(component, 'onEditItem').and.stub();
      await modelHarnesses[0].editClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onDeleteItem when delete address click', async() => {
      const spy = spyOn(component, 'onDeleteItem').and.stub();
      await modelHarnesses[0].deleteClick();
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('test address view list init', () => {
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
