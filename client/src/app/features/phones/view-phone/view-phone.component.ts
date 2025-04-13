import {Component, computed, model} from '@angular/core';
import {Phone, PhoneType} from '@app/features/phones/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {MatIcon} from '@angular/material/icon';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';

@Component({
  selector: 'app-view-phone',
  imports: [
    DataDisplayComponent,
    MatIcon,
    ViewModelComponent
  ],
  templateUrl: './view-phone.component.html',
  styleUrl: './view-phone.component.scss'
})
export class ViewPhoneComponent {
  phone = model.required<Phone>();

  phoneType = computed(() => this.phone().phone_type);
  phoneNumber = computed(() => {
    const phone = this.phone();
    return `+${phone.country_code} ${phone.phone_number}`;
  })

  icon = computed(() => this.typeMap.get(this.phoneType()));

  private typeMap: Map<PhoneType, string>;

  constructor() {
    this.typeMap = new Map<PhoneType, string>([
      [PhoneType.Work, 'corporate_fare'],
      [PhoneType.Home, 'home'],
      [PhoneType.Mobile, 'smartphone']
    ]);
  }
}
