import {input, Component} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {MatFormFieldAppearance} from '@angular/material/form-field';
import {IEditor} from '@app/shared/components/base-editor/IEditor';

@Component({
  selector: 'app-base-editor',
  template: ``,
  styles: ``
})
export class BaseEditorExComponent<T> implements IEditor<T>{
  public editorForm = input<FormModelGroup<T> | undefined>(undefined);
  public fieldAppearance = input<MatFormFieldAppearance>('outline' as MatFormFieldAppearance);
}
