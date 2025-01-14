import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListAddressesComponent} from './list-addresses.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {AddressService} from "../services/address.service";
import {CommonModule} from "@angular/common";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ListAddressesComponent', () => {
  let component: ListAddressesComponent;
  let fixture: ComponentFixture<ListAddressesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListAddressesComponent, CommonModule],
      providers: [
        AddressService,
        provideAnimations(),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListAddressesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
