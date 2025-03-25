import {Component, Directive, input, output} from '@angular/core';
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormModelGroup} from "./form-model-group";

@Component({
  selector: 'base-editor',
  template: ``,
  styles: ``
})
export abstract class BaseEditorComponent<T> {

  editorForm = input<FormModelGroup<T> | undefined>(undefined);

  title = input<string>('');

  fieldAppearance
      = input<MatFormFieldAppearance | undefined>(undefined);

  showRemoveButton = input(false);

  removeClick = output<void>();

  onRemove(): void {
    this.removeClick.emit();
  }
}
