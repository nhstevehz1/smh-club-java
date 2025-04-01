import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {Router} from '@angular/router';
import {provideHttpClient} from '@angular/common/http';
import {By} from '@angular/platform-browser';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';

import {Observable, Subject} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {generateMember} from '@app/features/members/testing/test-support';

import {Member} from '@app/features/members/models/member';
import {MemberService} from '@app/features/members/services/member.service';
import {AddMemberComponent} from './add-member.component';


describe('AddMemberComponent', () => {
  let component: AddMemberComponent;
  let fixture: ComponentFixture<AddMemberComponent>;

  let memberSvcMock: jasmine.SpyObj<MemberService>;
  let routerMock: jasmine.SpyObj<Router>;
  const memberMock: Member = generateMember(1);

  let createSubject$: Subject<Member>;
  let createMember$: Observable<Member>;

  beforeEach(async () => {
    memberSvcMock = jasmine.createSpyObj<MemberService>('MembersService', ['getPagedData']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    createSubject$ = new Subject<Member>();
    createMember$ = createSubject$.asObservable();

    await TestBed.configureTestingModule({
      imports: [
          AddMemberComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
        {provide: MemberService, useValue: {}},
        {provide: Router, useValue: routerMock}
      ]
    }).overrideProvider(MemberService, {useValue: memberSvcMock})
      .compileComponents();

    fixture = TestBed.createComponent(AddMemberComponent);
    component = fixture.componentInstance;
    // set one validator so the form is not 'valid'
    //component.createFormSignal().controls.first_name.setValidators(Validators.required);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });


  describe('test methods', () => {
    it('onOkOrCancel should call router.navigate', () => {
      const spy =
          routerMock.navigate.and.returnValue(Promise.resolve(true));

      component.onCancel();
      expect(spy).toHaveBeenCalledWith(['p/members']);
    });

    /*

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
    })*/
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

    it('should display cancel button', () =>  {
      const button = fixture.debugElement.query(By.css('.cancel'));
      expect(button).toBeTruthy();
    });

    it('should call onOkOrCancel when cancel button is clicked', fakeAsync(() => {
      const spy = spyOn(component, 'onCancel').and.stub();

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
      const spy = spyOn(component, 'onCancel').and.stub();
      const loader = TestbedHarnessEnvironment.loader(fixture);
      const harness = await loader.getHarnessOrNull(MatButtonHarness);
      await harness?.click();

      expect(spy).toHaveBeenCalled();
    })
  });
});
