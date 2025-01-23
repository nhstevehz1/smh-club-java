import {ListPhonesComponent} from './list-phones.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {PhoneService} from "../services/phone.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import SpyObj = jasmine.SpyObj;
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {AddressService} from "../../addresses/services/address.service";

describe('ListPhonesComponent', () => {
  let fixture: ComponentFixture<ListPhonesComponent>;
  let component: ListPhonesComponent;
  let phoneSvcMock: SpyObj<PhoneService>;

  beforeEach(async () => {
    phoneSvcMock = jasmine.createSpyObj('PhoneService', ['getPhones']);

    await TestBed.configureTestingModule({
      imports: [ListPhonesComponent],
      providers: [
          {provide: PhoneService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListPhonesComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should create column list', () => {
       fixture.detectChanges();
       expect(component.columns.length).toEqual(3);
    });
  });
});
