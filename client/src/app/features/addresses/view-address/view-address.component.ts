import {Component, computed, model} from '@angular/core';
import {Address, AddressType} from '@app/features/addresses/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {MatIconModule} from '@angular/material/icon';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';

@Component({
  selector: 'app-view-address',
  imports: [
    DataDisplayComponent,
    MatIconModule,
    ViewModelComponent
  ],
  templateUrl: './view-address.component.html',
  styleUrl: './view-address.component.scss'
})
export class ViewAddressComponent {
  address = model.required<Address>();

  address1 = computed(() => this.address().address1);
  address2 = computed(() => this.address().address2);
  addressType = computed(() => this.address().address_type);

  cityStatePostalCode = computed(() => {
    const address = this.address();
    return `${address.city}, ${address.state} ${address.postal_code}`
  });

  showAddress2 = computed(() => {
    const val = this.address2();
    return val && val.length > 0;
  });

  icon = computed(() => this.typeMap.get(this.addressType()));

  private typeMap: Map<AddressType, string>;

  constructor() {
    this.typeMap = new Map<AddressType, string>([
      [AddressType.Work, 'corporate_fare'],
      [AddressType.Home, 'home'],
      [AddressType.Other, 'question_mark']
    ]);
  }
}
