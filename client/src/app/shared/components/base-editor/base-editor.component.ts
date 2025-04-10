import {Component, input} from '@angular/core';
import {MatFormFieldAppearance} from '@angular/material/form-field';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {Editor} from '@app/shared/components/base-editor/editor';

@Component({
  selector: 'app-base-editor',
  template: ``,
  styles: ``
})
export abstract class BaseEditorComponent<T> implements Editor<T> {
  public editorForm = input<FormModelGroup<T> | undefined>(undefined);
  public fieldAppearance = input<MatFormFieldAppearance>('outline' as MatFormFieldAppearance);
}
