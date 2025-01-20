import {ListEmailsComponent} from './list-emails.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {EmailService} from "../services/email.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('ListEmailsComponent', () => {
  let fixture: ComponentFixture<ListEmailsComponent>;
  let component: ListEmailsComponent;
  let service: EmailService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
          ListEmailsComponent,
          EmailService,
          provideHttpClient(),
          provideHttpClientTesting()
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListEmailsComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(EmailService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
