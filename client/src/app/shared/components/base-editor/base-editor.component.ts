import {Component, input, output} from '@angular/core';
import {MatFormFieldAppearance} from '@angular/material/form-field';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {IEditor} from '@app/shared/components/base-editor/IEditor';

@Component({
  selector: 'app-base-editor',
  template: ``,
  styles: ``
})
export abstract class BaseEditorComponent<T> implements IEditor<T> {

  public editorForm = input<FormModelGroup<T> | undefined>(undefined);

  title = input<string>('');

  public fieldAppearance
      = input<MatFormFieldAppearance>('outline' as MatFormFieldAppearance);

  showRemoveButton = input(false);

  removeClick = output<void>();

  onRemove(): void {
    this.removeClick.emit();
  }
}
