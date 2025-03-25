import {ListEmailsComponent} from './list-emails.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {EmailService} from "../services/email.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {generateEmailPagedData} from "../test/email-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {throwError} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
import SpyObj = jasmine.SpyObj;
import {AuthService} from '../../../core/auth/services/auth.service';

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;
  let emailServiceMock: SpyObj<EmailService>;
  let authServiceMock: SpyObj<AuthService>;

  beforeEach(async () => {
    emailServiceMock = jasmine.createSpyObj('EmailService', ['getEmails']);
    authServiceMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    await TestBed.configureTestingModule({
      imports: [
        ListEmailsComponent,
        TranslateModule.forRoot({})
      ],
      providers: [
        {provide: EmailService, useValue: {}},
        {provide: AuthService, useValue: {}},
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations()
      ],
    })
      .overrideProvider(EmailService, {useValue: emailServiceMock})
      .overrideProvider(AuthService, {useValue: authServiceMock})
      .compileComponents();

    fixture = TestBed.createComponent(ListEmailsComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
      fit('should create', () => {
          expect(component).toBeTruthy();
      });

      fit('should create column list', () => {
         fixture.detectChanges();
         expect(component.columns.length).toBe(3);
      });
  });

  describe('test service interactions on init', () => {

     fit('should call EmailService.getEmails on init', async () => {
         const data = generateEmailPagedData(0, 5, 100);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         const request = PageRequest.of(0, 5);
         expect(emailServiceMock.getEmails).toHaveBeenCalledOnceWith(request)
     });

      fit('length should be set on init', async () => {
         const data = generateEmailPagedData(0, 5, 100);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         expect(component.resultsLength).toEqual(data.page.totalElements);
      });

      fit('datasource.data should be set on init', async () => {
         const data = generateEmailPagedData(0, 5, 2);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         expect(component.datasource().data).toBe(data._content);
      });

      fit('datasource.data should be empty when an error occurs while calling getEmails', async () => {
          emailServiceMock.getEmails.and.returnValue(throwError(() => 'error'));

          fixture.detectChanges();
          await fixture.whenStable();

          expect(component.datasource().data).toEqual([]);
      });
  });
});
