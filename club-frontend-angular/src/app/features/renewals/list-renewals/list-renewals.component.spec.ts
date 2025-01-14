import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRenewalsComponent } from './list-renewals.component';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {RenewalService} from "../services/renewal.service";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ListRenewalsComponent', () => {
  let component: ListRenewalsComponent;
  let fixture: ComponentFixture<ListRenewalsComponent>;
  let service: RenewalService;
  let httpClient: HttpClient;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListRenewalsComponent],
      providers: [
        RenewalService,
        provideAnimations(),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListRenewalsComponent);
    service = TestBed.inject(RenewalService);
    httpClient = TestBed.inject(HttpClient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
