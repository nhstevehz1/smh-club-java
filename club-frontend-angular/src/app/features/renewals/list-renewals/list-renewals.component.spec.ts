import {ListRenewalsComponent} from './list-renewals.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {RenewalService} from "../services/renewal.service";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('ListRenewalsComponent', () => {
  let fixture: ComponentFixture<ListRenewalsComponent>;
  let component: ListRenewalsComponent;
  let service: RenewalService

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
          ListRenewalsComponent,
          RenewalService,
          provideHttpClient(),
          provideHttpClientTesting()
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListRenewalsComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(RenewalService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
