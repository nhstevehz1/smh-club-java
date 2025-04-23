import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewEmailListComponent} from './view-email-list.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {AuthService} from '@app/core/auth/services/auth.service';
import {ViewModelListHarness} from '@app/shared/components/view-model-list/testing/view-model-list-harness';
import {ViewModelHarness} from '@app/shared/components/view-model-component/testing/view-model-harness';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';
import {EmailService} from '@app/features/emails/services/email.service';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {asyncData} from '@app/shared/testing';
import {EmailTest} from '@app/features/emails/testing';

describe('ViewEmailListComponent', () => {
  let component: ViewEmailListComponent;
  let fixture: ComponentFixture<ViewEmailListComponent>;
  let loader: HarnessLoader;

  let authSvcMock: jasmine.SpyObj<AuthService>;
  let dialogSvcMock: jasmine.SpyObj<EmailEditDialogService>;
  let apiSvcMock: jasmine.SpyObj<EmailService>;

  let harness: ViewModelListHarness;
  let modelHarnesses: ViewModelHarness[];

  let memberId: number;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    dialogSvcMock = jasmine.createSpyObj('EmailEditDialogService',
      ['openDialog', 'generateDialogInput']);

    apiSvcMock = jasmine.createSpyObj('EmailService', ['getAllByMember']);

    await TestBed.configureTestingModule({
      imports: [ViewEmailListComponent],
      providers: [
        {provide: AuthService, useValue: {}},
        {provide: EmailEditDialogService, useValue: {}},
        {provide: EmailService, useValue: {}}
      ]
    }).overrideProvider(AuthService, {useValue: authSvcMock})
      .overrideProvider(EmailEditDialogService, {useValue: dialogSvcMock})
      .overrideProvider(EmailService, {useValue: apiSvcMock})
    .compileComponents();

    fixture = TestBed.createComponent(ViewEmailListComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    fixture.componentRef.setInput('memberId', 0);
    expect(component).toBeTruthy();
  });

  describe('component interactions', () => {
    beforeEach(async () => {
      const email = EmailTest.generateEmail();
      fixture.componentRef.setInput('memberId', email.member_id);

      // must set to true for buttons to be rendered
      authSvcMock.hasPermission.and.returnValue(true);
      apiSvcMock.getAllByMember.and.returnValue(asyncData([email]));

      harness = await loader.getHarness(ViewModelListHarness);
      modelHarnesses = await loader.getAllHarnesses(ViewModelHarness);
    });

    it('should call onAddItem when add email click', async () => {
      const spy = spyOn(component, 'onAddItem').and.stub();
      await harness.addClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onEditItem when edit email click', async () => {
      const spy = spyOn(component, 'onEditItem').and.stub();
      await modelHarnesses[0].editClick();
      expect(spy).toHaveBeenCalled();
    });

    it('should call onDeleteItem when delete email click', async() => {
      const spy = spyOn(component, 'onDeleteItem').and.stub();
      await modelHarnesses[0].deleteClick();
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('test email view list init', () => {
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
