import {Component} from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-language-switcher',
    imports: [
        MatSelectModule
    ],
  templateUrl: './language-switcher.component.html',
  styleUrl: './language-switcher.component.scss'
})
export class LanguageSwitcherComponent {

  private _selected: string;

  public get selected(): string {
    return this._selected;
  }

  public set selected(val: string) {
    this._selected = val;
  }

  constructor(private translate: TranslateService) {
    this._selected = this.translate.currentLang;
  }

  onLanguageChange() : void {
    this.translate.use(this._selected);
  }
}
