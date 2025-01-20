import {ListAddressesComponent} from './list-addresses.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {AddressService} from "../services/address.service";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('ListAddressesComponent', () => {
  let fixture: ComponentFixture<ListAddressesComponent>;
  let component: ListAddressesComponent;
  let service: AddressService;

  beforeEach(async () => {
   await TestBed.configureTestingModule({
      providers: [
          ListAddressesComponent,
          AddressService,
          provideHttpClient(),
          provideHttpClientTesting()
      ],
     schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListAddressesComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(AddressService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
