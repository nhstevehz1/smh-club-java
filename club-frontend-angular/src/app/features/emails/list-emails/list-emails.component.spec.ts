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
import SpyObj = jasmine.SpyObj;

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;
  let emailServiceMock: SpyObj<EmailService>;

  beforeEach(async () => {
    emailServiceMock = jasmine.createSpyObj('EmailService', ['getEmails']);
    await TestBed.configureTestingModule({
      imports: [ListEmailsComponent],
      providers: [
          {provide: EmailService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideComponent(ListEmailsComponent,
      { set: {providers: [{provide: EmailService, useValue: emailServiceMock}]}}
    ).compileComponents();
    fixture = TestBed.createComponent(ListEmailsComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
      it('should create', () => {
          expect(component).toBeTruthy();
      });

      it('should create column list', () => {
         fixture.detectChanges();
         expect(component.columns.length).toBe(3);
      });
  });

  describe('test service interactions on init', () => {

     it('should call EmailService.getEmails on init', async () => {
         const data = generateEmailPagedData(0, 5, 100);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         const request = PageRequest.of(0, 5);
         expect(emailServiceMock.getEmails).toHaveBeenCalledOnceWith(request)
     });

      it('length should be set on init', async () => {
         const data = generateEmailPagedData(0, 5, 100);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         const request = PageRequest.of(0, 5);
         expect(component.resultsLength).toEqual(data.page.totalElements);
      });

      it('datasource.data should be set on init', async () => {
         const data = generateEmailPagedData(0, 5, 2);
         emailServiceMock.getEmails.and.returnValue(asyncData(data));

         fixture.detectChanges();
         await fixture.whenStable();

         expect(component.datasource.data).toBe(data._content);
      });

      it('datasource.data should be empty when an error occurs while calling getEmails', async () => {
          emailServiceMock.getEmails.and.returnValue(throwError(() => 'error'));

          fixture.detectChanges();
          await fixture.whenStable();

          expect(component.datasource.data).toEqual([]);
      });
  });
});
