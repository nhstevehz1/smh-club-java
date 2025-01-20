import {ListPhonesComponent} from './list-phones.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {PhoneService} from "../services/phone.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('ListPhonesComponent', () => {
  let fixture: ComponentFixture<ListPhonesComponent>;
  let component: ListPhonesComponent;
  let service: PhoneService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
          ListPhonesComponent,
          PhoneService,
          provideHttpClient(),
          provideHttpClientTesting()
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListPhonesComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(PhoneService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
