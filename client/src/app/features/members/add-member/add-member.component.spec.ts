import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AddMemberComponent} from './add-member.component';
import {MembersService} from "../services/members.service";
import {Router} from "@angular/router";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {generateMember, generateMemberCreateForm} from "../test/member-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {Observable, Subject, throwError} from "rxjs";
import {MemberBase} from "../models/member";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatButtonHarness} from "@angular/material/button/testing";
import {By} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";
import {AddressService} from "../../addresses/services/address.service";
import {EmailService} from "../../emails/services/email.service";
import {PhoneService} from "../../phones/services/phone.service";
import {generateAddressCreateForm} from "../../addresses/test/address-test";
import {generateEmailCreateForm} from "../../emails/test/email-test";
import {generatePhoneCreateForm} from "../../phones/test/phone-test";
import {Validators} from "@angular/forms";

describe('AddMemberComponent', () => {
  let component: AddMemberComponent;
  let fixture: ComponentFixture<AddMemberComponent>;

  let memberSvcMock: jasmine.SpyObj<MembersService>;
  let addressSvcMock: jasmine.SpyObj<AddressService>;
  let emailSvcMock: jasmine.SpyObj<EmailService>;
  let phoneSvcMock: jasmine.SpyObj<PhoneService>;
  let routerMock: jasmine.SpyObj<Router>;
  const memberMock: MemberBase = generateMember(1);

  let createSubject$: Subject<MemberBase>;
  let createMember$: Observable<MemberBase>;

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj<MembersService>('MembersService', ['createMember', 'generateCreateForm']);
    addressSvcMock = jasmine.createSpyObj<AddressService>('AddressService', ['generateCreateForm']);
    emailSvcMock = jasmine.createSpyObj<EmailService>('EmailService', ['generateCreateForm']);
    phoneSvcMock = jasmine.createSpyObj<PhoneService>('PhoneService', ['generateCreateForm']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    createSubject$ = new Subject<MemberBase>();
    createMember$ = createSubject$.asObservable();

    addressSvcMock.generateCreateForm.and.returnValue(generateAddressCreateForm());
    emailSvcMock.generateCreateForm.and.returnValue(generateEmailCreateForm());
    phoneSvcMock.generateCreateForm.and.returnValue(generatePhoneCreateForm());
    memberSvcMock.generateCreateForm.and.returnValue(generateMemberCreateForm())

    await TestBed.configureTestingModule({
      imports: [
          AddMemberComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
        {provide: MembersService, useValue: {}},
        {provide: AddressService, useValue: {}},
        {provide: EmailService, useValue: {}},
        {provide: PhoneService, useValue: {}},
        {provide: Router, useValue: routerMock}
      ]
    }).overrideProvider(MembersService, {useValue: memberSvcMock})
        .overrideProvider(AddressService, {useValue: addressSvcMock})
        .overrideProvider(EmailService, {useValue: emailSvcMock})
        .overrideProvider(PhoneService, {useValue: phoneSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(AddMemberComponent);
    component = fixture.componentInstance;
    // set one validator so the form is not 'valid'
    component.createFormSignal().controls.first_name.setValidators(Validators.required);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('test properties', ()=> {

    it('should contain member create form group', () => {
      expect(component.createFormSignal()).not.toBeNull();
    });

    it('should contain address form group array', () => {
      expect(component.addressFormsComputed()).not.toBeNull();
    });

    it('address form array should have a length of 1', () => {
      expect(component.addressFormsComputed().length).toEqual(1);
    });

    it('should contain email form group array', () => {
      expect(component.emailFormsComputed).not.toBeNull();
    });

    it('email form array should have a length of 1', () => {
      expect(component.emailFormsComputed().length).toEqual(1);
    });

    it('should contain phone form group array', () => {
      expect(component.phoneFormsComputed()).not.toBeNull();
    });

    it('address form array should have a length of 1', () => {
      expect(component.phoneFormsComputed().length).toEqual(1);
    });
  });

  describe('test methods', () => {
    it('onOkOrCancel should call router.navigate', () => {
      const spy =
          routerMock.navigate.and.returnValue(Promise.resolve(true));

      component.onOkOrCancel();
      expect(spy).toHaveBeenCalledWith(['p/members']);
    });

    it('onAddAddress should should add an address form group to array', () => {
      const length = component.addressFormsComputed().length;
      component.onAddAddress();
      expect(component.addressFormsComputed().length).toEqual(length + 1);
    });

    it('onDeleteAddress should remove the correct form', () => {
      const spy = spyOn(component.addressFormsComputed(), 'removeAt').and.callThrough();
      component.onAddAddress();
      component.onAddAddress();
      const length = component.addressFormsComputed().length;

      component.onDeleteAddress(1);

      expect(spy).toHaveBeenCalledWith(1);
      expect(component.addressFormsComputed().length).toEqual(length - 1);
    });

    it('onAddEmail should add an email form group to the array', () => {
      const length = component.emailFormsComputed().length;
      component.onAddEmail();
      expect(component.emailFormsComputed().length).toEqual(length + 1);
    });

    it('onDeleteEmail should remove the correct form', () => {
      const spy = spyOn(component.emailFormsComputed(), 'removeAt').and.callThrough();
      component.onAddEmail();
      component.onAddEmail();
      const length = component.emailFormsComputed().length;

      component.onDeleteEmail(1);

      expect(spy).toHaveBeenCalledWith(1);
      expect(component.emailFormsComputed().length).toEqual(length - 1);
    });

    it('onAddPhone should add an email form group to the array', () => {
      const length = component.phoneFormsComputed().length;
      component.onAddPhone();
      expect(component.phoneFormsComputed().length).toEqual(length + 1);
    });

    it('onDeletePhone should remove the correct form', () => {
      const spy = spyOn(component.phoneFormsComputed(), 'removeAt').and.callThrough();
      component.onAddPhone();
      component.onAddPhone();
      const length = component.phoneFormsComputed().length;

      component.onDeletePhone(1);

      expect(spy).toHaveBeenCalledWith(1);
      expect(component.phoneFormsComputed().length).toEqual(length - 1);
    });

    it('onSave should call createForm.valid', () => {
      const spy =
          spyOnProperty(component.createFormSignal(), 'valid').and.stub();
      component.onSave();
      expect(spy).toHaveBeenCalled();
    });

    it('onSave should call MemberService.createMember createForm.valid is true', () => {
      spyOnProperty(component.createFormSignal(), 'valid', 'get').and.returnValue(true);
      const spy
          = memberSvcMock.createMember.and.returnValue(asyncData(memberMock));

      component.onSave();
      expect(spy).toHaveBeenCalled();
    });

    it('onSave should set errorMessage to undefined when Member.Service is success', fakeAsync( () => {
      component.errorMessage.set('error');
      const spy = spyOnProperty(component.createFormSignal(), 'valid', 'get').and.returnValue(true);
      memberSvcMock.createMember.and.returnValue(createMember$);
      component.onSave();
      createSubject$.next(memberMock);
      tick();

      expect(spy).toHaveBeenCalled();
      expect(component.errorMessage()).toBeNull();
    }));

    it('onSave should set errorMessage Member.Service returns an error', fakeAsync( () => {
      component.errorMessage.set(null);
      spyOnProperty(component.createFormSignal(), 'valid', 'get').and.returnValue(true);
      memberSvcMock.createMember.and.returnValue(throwError(() => new Error('error message')));

      component.onSave();
      createSubject$.next(memberMock);
      tick();

      expect(component.errorMessage()).toBeTruthy();
    }));

    it('onSave should set error message when formGroup.valid is false', () => {
      component.errorMessage.set(null);
      spyOnProperty(component.createFormSignal(), 'valid', 'get').and.returnValue(false);
      component.onSave();
      expect(component.errorMessage).toBeTruthy();
    })
  });

  describe('test component rendering', ()=> {
    beforeEach(async () => {
      fixture.detectChanges();
      await fixture.whenStable();
    });

    it('should display form when submitted is false', async ()=> {
      component.submitted.set(false);

      //const harness = await loader.getHarnessOrNull(MatCardHarness);
      fixture.detectChanges();
      await fixture.whenStable();

      const form = fixture.debugElement.nativeElement.querySelector('form');
      expect(form).toBeTruthy();
    });

    it('should NOT display form when submitted is true', async ()=> {
      component.submitted.set(true);

      fixture.detectChanges();
      await fixture.whenStable();

      const form = fixture.debugElement.nativeElement.querySelector('form');
      expect(form).toBeFalsy();
    });

    it('should display add address button', async () =>  {
      const button = fixture.debugElement.query(By.css('.add-address'));
      expect(button).toBeTruthy();
    });

    it('should call onAAddress when add address button is clicked', fakeAsync(() => {
      const spy = spyOn(component, 'onAddAddress').and.stub();

      fixture.debugElement.query(By.css('.add-address')).nativeElement.click();
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should display add email button', () =>  {
      const button = fixture.debugElement.query(By.css('.add-email'));
      expect(button).toBeTruthy();
    });

    it('should call onAddEmail when add email button is clicked', fakeAsync(() => {
      const spy = spyOn(component, 'onAddEmail').and.stub();

      fixture.debugElement.query(By.css('.add-email')).nativeElement.click();
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should display add phone button', () =>  {
      const button = fixture.debugElement.query(By.css('.add-email'));
      expect(button).toBeTruthy();
    });

    it('should call onAddPhone when add phone button is clicked', fakeAsync(() => {
      const spy = spyOn(component, 'onAddPhone').and.stub();

      fixture.debugElement.query(By.css('.add-phone')).nativeElement.click();
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should display cancel button', () =>  {
      const button = fixture.debugElement.query(By.css('.cancel'));
      expect(button).toBeTruthy();
    });

    it('should call onOkOrCancel when cancel button is clicked', fakeAsync(() => {
      const spy = spyOn(component, 'onOkOrCancel').and.stub();

      fixture.debugElement.query(By.css('.cancel')).nativeElement.click();
      tick();

      expect(spy).toHaveBeenCalled();
    }));

    it('should display save button', () =>  {
      const button = fixture.debugElement.query(By.css('.submit'));
      expect(button).toBeTruthy();
    });

    it('should call onSave when save form is submitted', fakeAsync(() => {
      const spy = spyOn(component, 'onSave').and.stub();
      const fakeEvent = {preventDefault: () => console.debug('fakeEvent')};
      const form = fixture.debugElement.query(By.css('form'));

      form.triggerEventHandler('submit', fakeEvent);

      tick();
      expect(spy).toHaveBeenCalled();
    }));

    it('should display app-ok-cancel when submitted is true', async () => {
      component.submitted.set(true);
      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css('app-ok-cancel'));

      expect(element).toBeTruthy();
    });

    it('should NOT display app-ok-cancel when submitted is false', async () => {
      component.submitted.set(false);
      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css('app-ok-cancel'));

      expect(element).toBeFalsy();
    });

    it('should call onOkOrCancel when button clicked in <app-ok-cancel> component', async () => {
      component.submitted.set(true);
      const spy = spyOn(component, 'onOkOrCancel').and.stub();
      const loader = TestbedHarnessEnvironment.loader(fixture);
      const harness = await loader.getHarnessOrNull(MatButtonHarness);
      await harness?.click();

      expect(spy).toHaveBeenCalled();
    })
  });
});
