import {Type} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models/form-model-group';
import {IEditor} from '@app/shared/components/base-editor/IEditor';

export interface EditorConfig<T, C extends IEditor<T>> {
  form: FormModelGroup<T>
  component: Type<C>
}
