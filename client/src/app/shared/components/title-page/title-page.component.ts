import {Component, computed, input} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";

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
  titleSignal = input.required<string>({alias: 'title'});
  subTitleSignal = input.required<string>({alias: 'subTitle'});
  iconNameSignal = input<string>(undefined, {alias: 'iconName'});
  protected iconDefinedSignal = computed<boolean>(() => !!this.iconNameSignal());
}
