import {Directive, input, output} from '@angular/core';
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormModelGroup} from "./form-model-group";

@Directive()
export abstract class BaseEditorComponent<T> {

  editorForm = input.required<FormModelGroup<T>>();

  title = input<string>();

  fieldAppearance
      = input<MatFormFieldAppearance>();

  showRemoveButton = input(false);

  removeClick = output<void>();

  onRemove(): void {
    this.removeClick.emit();
  }
}
