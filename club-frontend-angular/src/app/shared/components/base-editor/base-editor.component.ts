import {Component, EventEmitter, Input, Output, WritableSignal} from '@angular/core';
import {FormControl} from "@angular/forms";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormModelGroup} from "./form-model-group";

@Component({
  selector: 'base-editor',
  imports: [],
  template: ``,
  styles: ``
})
export abstract class BaseEditorComponent<T> {
  @Input({required: true})
  public editorForm!: FormModelGroup<T>;

  @Input()
  public title?: string;

  @Input()
  public fieldAppearance: MatFormFieldAppearance = 'outline'

  @Input()
  public showRemoveButton: boolean = false;

  @Output()
  public removeClick: EventEmitter<any> = new EventEmitter();

  hasError(formControl: FormControl): boolean {
    return formControl.invalid && (formControl.dirty || !formControl.untouched)
  }

  onRemove(): void {
    this.removeClick.next(null);
  }

  isTitleDefined(): boolean {
    return this.title !== undefined;
  };

  setErrorSignal(signal: WritableSignal<boolean>, fc: FormControl): void {
    fc.valueChanges.subscribe(() => {
      signal.update(() => this.hasError(fc));
    })
  }
}
