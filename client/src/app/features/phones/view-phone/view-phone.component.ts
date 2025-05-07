import {Component, computed, model, input, output} from '@angular/core';
import {Phone, PhoneType} from '@app/features/phones/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {MatIcon} from '@angular/material/icon';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-view-phone',
  imports: [
    DataDisplayComponent,
    MatIcon,
    ViewModelComponent,
    TranslatePipe
  ],
  templateUrl: './view-phone.component.html',
  styleUrl: './view-phone.component.scss'
})
export class ViewPhoneComponent {
  allowEdit = input(false);
  allowDelete = input(false);
  phone = model.required<Phone>();

  phoneType = computed(() => this.phone().phone_type);
  phoneNumber = computed(() => {
    const phone = this.phone();
    return `+${phone.country_code} ${phone.phone_number}`;
  })

  icon = computed(() => this.typeMap.get(this.phoneType()));

  editClick = output<Phone>();
  deleteClick = output<Phone>();

  private typeMap: Map<PhoneType, string>;

  constructor() {
    this.typeMap = new Map<PhoneType, string>([
      [PhoneType.Work, 'corporate_fare'],
      [PhoneType.Home, 'home'],
      [PhoneType.Mobile, 'smartphone']
    ]);
  }

  onEdit(): void {
    this.editClick.emit(this.phone());
  }

  onDelete(): void {
    this.deleteClick.emit(this.phone())
  }
}
