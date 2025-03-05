import {Directive, input, output} from '@angular/core';
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormModelGroup} from "./form-model-group";

@Directive()
export abstract class BaseEditorComponent<T> {

  editorFormSignal
      = input.required<FormModelGroup<T>>({alias: 'editorForm'});

  titleSignal = input<string>(undefined, {alias: 'title'});

  fieldAppearanceSignal
      = input<MatFormFieldAppearance>(undefined, {alias: 'fieldAppearance'});

  showRemoveButtonSignal = input(false, {
    alias: 'showRemoveButton'});

  removeSignal = output<void>({alias: 'removeClick'});

  onRemove(): void {
    this.removeSignal.emit();
  }
}
