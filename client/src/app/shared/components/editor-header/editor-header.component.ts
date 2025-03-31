import {booleanAttribute, Component, computed, input, output,} from '@angular/core';
import {NgClass} from "@angular/common";

import {MatDivider} from "@angular/material/divider";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatLabel} from "@angular/material/form-field";

import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-editor-header',
  imports: [
    MatDivider,
    MatIconModule,
    MatButtonModule,
    MatLabel,
    TranslatePipe,
    NgClass,
  ],
  templateUrl: './editor-header.component.html',
  styleUrl: './editor-header.component.scss'
})
export class EditorHeaderComponent {
  title = input<string>();

  showRemoveButton = input(false, {transform: booleanAttribute});

  removeClick = output<void>();

  protected titleDefined
        = computed<boolean>(() => !!this.title());

  onRemove(): void {
    this.removeClick.emit();
  }
}
