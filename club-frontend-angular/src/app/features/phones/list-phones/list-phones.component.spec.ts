import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListPhonesComponent } from './list-phones.component';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {PhoneService} from "../services/phone.service";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ListPhonesComponent', () => {
  let component: ListPhonesComponent;
  let fixture: ComponentFixture<ListPhonesComponent>;
  let service: PhoneService;
  let httpClient: HttpClient;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListPhonesComponent],
      providers: [
        PhoneService,
        provideAnimations(),
        provideHttpClient(),
        provideHttpClientTesting()
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListPhonesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    service = TestBed.inject(PhoneService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
