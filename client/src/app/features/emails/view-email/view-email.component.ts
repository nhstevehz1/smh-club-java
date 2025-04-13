import {Component, computed, model} from '@angular/core';
import {Email, EmailType} from '@app/features/emails/models';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {MatIcon} from '@angular/material/icon';
import {ViewModelComponent} from '@app/shared/components/view-model-component/view-model.component';

@Component({
  selector: 'app-view-email',
  imports: [
    DataDisplayComponent,
    MatIcon,
    ViewModelComponent
  ],
  templateUrl: './view-email.component.html',
  styleUrl: './view-email.component.scss'
})
export class ViewEmailComponent {
  email = model.required<Email>();

  emailAddress = computed(() => this.email().email);
  emailType = computed(() => this.email().email_type);

  icon = computed(() => this.typeMap.get(this.emailType()));

  private typeMap: Map<EmailType, string>;

  constructor() {
    this.typeMap = new Map<EmailType, string>([
      [EmailType.Work, 'corporate_fare'],
      [EmailType.Home, 'home'],
      [EmailType.Other, 'question_mark']
    ]);
  }
}
