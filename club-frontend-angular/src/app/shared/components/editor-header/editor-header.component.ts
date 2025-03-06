import {booleanAttribute, Component, computed, input, output,} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatLabel} from "@angular/material/form-field";
import {TranslatePipe} from "@ngx-translate/core";
import {NgClass} from "@angular/common";

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
  titleSignal = input<string>(undefined, {alias: 'title'});

  showRemoveButtonSignal = input(false, {
      alias: 'showRemoveButton',
      transform: booleanAttribute
  });

  removeSignal = output<void>({alias: 'removeClick'});

  protected titleDefinedSignal
        = computed<boolean>(() => !!this.titleSignal());

  onRemove(): void {
    this.removeSignal.emit();
  }
}
