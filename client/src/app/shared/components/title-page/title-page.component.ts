import {Component, computed, input} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-title-page',
  imports: [
    MatIcon,
    TranslatePipe
  ],
  templateUrl: './title-page.component.html',
  styleUrl: './title-page.component.scss'
})
export class TitlePageComponent {
  title = input.required<string>();
  subTitle = input.required<string>();
  iconName = input<string>();
  protected iconDefined = computed<boolean>(() => !!this.iconName());
}
