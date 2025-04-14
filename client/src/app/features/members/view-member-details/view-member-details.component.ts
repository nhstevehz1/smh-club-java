import { Component } from '@angular/core';
import {
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader, MatExpansionPanelTitle
} from '@angular/material/expansion';
import {TranslatePipe} from '@ngx-translate/core';
import {ViewAddressComponent} from '@app/features/addresses/view-address/view-address.component';
import {ViewEmailComponent} from '@app/features/emails/view-email/view-email.component';
import {ViewPhoneComponent} from '@app/features/phones/view-phone/view-phone.component';
import {ViewRenewalComponent} from '@app/features/renewals/view-renewal/view-renewal.component';
import {MatDivider} from '@angular/material/divider';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';

@Component({
  selector: 'app-view-member-details',
  imports: [
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    TranslatePipe,
    ViewAddressComponent,
    ViewEmailComponent,
    ViewPhoneComponent,
    ViewRenewalComponent,
    MatDivider,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './view-member-details.component.html',
  styleUrl: './view-member-details.component.scss'
})
export class ViewMemberDetailsComponent {

}
