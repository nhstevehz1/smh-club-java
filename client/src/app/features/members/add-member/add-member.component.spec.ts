import {ComponentFixture, TestBed, fakeAsync, tick} from '@angular/core/testing';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideLuxonDateAdapter} from '@angular/material-luxon-adapter';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {TranslateModule} from '@ngx-translate/core';

import {AddMemberComponent} from './add-member.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberEditDialogService} from '@app/features/members/services/member-edit-dialog.service';
import {MemberTest} from '@app/features/members/testing/test-support';
import {Member} from '@app/features/members/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {MatButtonHarness} from '@angular/material/button/testing';
import {asyncData} from '@app/shared/testing';

describe('AddMemberComponent', () => {
  let component: AddMemberComponent;
  let fixture: ComponentFixture<AddMemberComponent>;
  let loader: HarnessLoader;

  let apiSvcMock: jasmine.SpyObj<MemberService>;
  let dialogSvcMock: jasmine.SpyObj<MemberEditDialogService>;
  let routerMock: jasmine.SpyObj<Router>;
  let locationMock: jasmine.SpyObj<Location>;

  let form: FormModelGroup<Member>;

  beforeEach(async () => {
    apiSvcMock = jasmine.createSpyObj('MemberService', ['create']);
    dialogSvcMock = jasmine.createSpyObj('MemberEditDialogService', ['generateForm']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);
    locationMock = jasmine.createSpyObj('Location', ['back']);

    await TestBed.configureTestingModule({
      imports: [
        AddMemberComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
        provideLuxonDateAdapter(),
        {provide: MemberService, useValue: {}},
        {provide: MemberEditDialogService, useValue: {}},
        {provide: Router, useValue: {}},
        {provide: Location, useValue: {}},
      ]
    }).overrideProvider(MemberService, {useValue: apiSvcMock})
      .overrideProvider(MemberEditDialogService, {useValue: dialogSvcMock})
      .overrideProvider(Router, {useValue: routerMock})
      .overrideProvider(Location, {useValue: locationMock})
    .compileComponents();

    fixture = TestBed.createComponent(AddMemberComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('interactions on init', () => {
    it('should call MemberEditDialogService.generateForm', async () => {
      form = MemberTest.generateForm();
      const spy = dialogSvcMock.generateForm.and.returnValue(form);

      fixture.detectChanges();
      await fixture.whenStable();

      expect(spy).toHaveBeenCalled();
    });

    it('should bind createForm', async () => {
      form = MemberTest.generateForm();
      dialogSvcMock.generateForm.and.returnValue(form);

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.createForm()).toBeTruthy();
    });
  });

  describe('save and cancel buttons', () => {
    beforeEach(async () => {
      form = MemberTest.generateForm();
      dialogSvcMock.generateForm.and.returnValue(form);

      fixture.detectChanges()
      await fixture.whenStable();
    });

    it('should contain two buttons', async () => {
      // need to filter out the date control buttons.
      const harnesses =
        await loader.getAllHarnesses(MatButtonHarness.with({variant: 'basic'}));
      expect(harnesses.length).toEqual(2);
    })

    it('should contain a save button', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'basic', text: 'save'}));
      expect(harness).toBeTruthy();
    });

    it('save button should be disabled', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'basic', text: 'save'}));
      expect(await harness?.isDisabled()).toBeTrue();
    });

    it('should contain a cancel button', async () => {
      const harness =
        await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'basic', text: 'cancel'}));
      expect(harness).toBeTruthy();
    });

    it('cancel button click should call onCancel', async() => {
      const spy = spyOn(component, 'onCancel').and.stub();
      const harness =
        await loader.getHarness(MatButtonHarness.with({variant: 'basic', text: 'cancel'}));

      await harness.click();

      expect(spy).toHaveBeenCalled();
    });

    it('save button click should call onSave', async () => {
      const spy = spyOn(component, 'onSave').and.stub();
      component.createForm()?.markAsTouched();
      const harness =
        await loader.getHarness(MatButtonHarness.with({variant: 'basic', text: 'save'}));

      await harness.click();

      expect(spy).toHaveBeenCalled();
    });
  });

  describe('component interactions', () => {
    beforeEach(async () => {
      form = MemberTest.generateForm();
      form.patchValue(MemberTest.generateMember(1));
      dialogSvcMock.generateForm.and.returnValue(form);

      fixture.detectChanges()
      await fixture.whenStable();
    });

    it('onSave should call MemberService.create', fakeAsync(() => {
      routerMock.navigate.and.returnValue(Promise.resolve(true));
      const member = MemberTest.generateMember(1);
      const spy = apiSvcMock.create.and.returnValue(asyncData(member));

      component.onSave();
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('onCancel should call location.back', () => {
      const spy = locationMock.back.and.stub();

      component.onCancel();

      expect(spy).toHaveBeenCalled();
    });

  });
});
