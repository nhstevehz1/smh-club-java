import {
    booleanAttribute,
    Component,
    computed,
    EventEmitter,
    Input,
    Output,
    Signal,
    signal,
    WritableSignal
} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatLabel} from "@angular/material/form-field";
import {TranslatePipe} from "@ngx-translate/core";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-editor-header',
  imports: [
    MatDivider,
    MatIcon,
    MatIconButton,
    MatLabel,
    TranslatePipe,
    NgClass
  ],
  templateUrl: './editor-header.component.html',
  styleUrl: './editor-header.component.scss'
})
export class EditorHeaderComponent {
  protected readonly titleSignal: WritableSignal<string | undefined>  = signal(undefined);

  protected readonly titleDefinedSignal: Signal<boolean>
      = computed(() => !!this.titleSignal);

  protected readonly showButtonSignal: WritableSignal<boolean> = signal(false);

  @Input()
  public set title(title: string | undefined) {
    this.titleSignal.set(title);
  };

  @Input({transform: booleanAttribute})
  public set showRemoveButton(show:boolean) {
    this.showButtonSignal.set(show);
  }

  @Output()
  removeClick: EventEmitter<void> = new EventEmitter();

  onRemove(): void {
    this.removeClick.next();
  }
}
