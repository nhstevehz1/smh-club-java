import {ListPhonesComponent} from './list-phones.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {PhoneService} from "../services/phone.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import SpyObj = jasmine.SpyObj;
import {generatePhonePageData} from "../test/phone-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {throwError} from "rxjs";

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
    }).overrideComponent(ListPhonesComponent,
        {set: {providers: [{provide: PhoneService, useValue: phoneSvcMock}]}},

    ).compileComponents();
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

  describe('test service interactions on init', () => {
    it('should call PhoneService.getPhones on init', async () => {
      const data = generatePhonePageData(0, 5, 100);
      phoneSvcMock.getPhones.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      const request = PageRequest.of(0, 5);
      expect(phoneSvcMock.getPhones).toHaveBeenCalledWith(request);
    });

    it('length should be set on init', async () => {
      const data = generatePhonePageData(0, 5, 2);
      phoneSvcMock.getPhones.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource.data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
      phoneSvcMock.getPhones.and.returnValue(throwError(() => 'error'));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource.data).toEqual([]);
    });
  });
});
