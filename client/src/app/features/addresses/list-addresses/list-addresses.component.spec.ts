import {ListAddressesComponent} from './list-addresses.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {AddressService} from "../services/address.service";
import {throwError} from "rxjs";
import {generateAddressPagedData} from "../test/address-test";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {PageRequest} from "../../../shared/models/page-request";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {TranslateModule} from "@ngx-translate/core";

describe('ListAddressesComponent', () => {
  let fixture: ComponentFixture<ListAddressesComponent>;
  let component: ListAddressesComponent;
  let addressSvcMock: jasmine.SpyObj<AddressService>;

  beforeEach(async () => {
    addressSvcMock = jasmine.createSpyObj('AddressService', ['getAddresses']);
    await TestBed.configureTestingModule({
      imports: [
          ListAddressesComponent,
          TranslateModule.forRoot({})
      ],
        providers: [
          {provide: AddressService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideComponent(ListAddressesComponent,
        { set: {providers: [{provide: AddressService, useValue: addressSvcMock}]}}
    ).compileComponents();
   fixture = TestBed.createComponent(ListAddressesComponent);
   component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should create column list', () => {
        fixture.detectChanges();
        expect(component.columns.length).toEqual(6);
    });
  });

  describe('test service interactions on init', () => {
    it('should call AddressService.getAddresses() on init', async () => {
      const data = generateAddressPagedData(0, 5, 100);
      addressSvcMock.getAddresses.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      const request = PageRequest.of(0, 5);
      expect(addressSvcMock.getAddresses).toHaveBeenCalledWith(request);//With(request);
    });

    it('length should be set on init', async () => {
      const data = generateAddressPagedData(0, 5, 100);
      addressSvcMock.getAddresses.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.resultsLength).toEqual(data.page.totalElements);
    });

    it('datasource.data should be set on init', async () => {
        const data = generateAddressPagedData(0, 5, 2);
        addressSvcMock.getAddresses.and.returnValue(asyncData(data));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
        addressSvcMock.getAddresses.and.returnValue(throwError(() => 'error'));

        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.datasource().data).toEqual([]);
    });
  });
});
