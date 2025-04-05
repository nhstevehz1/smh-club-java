import {Type} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models/form-model-group';
import {Editor} from '@app/shared/components/base-editor/editor';

export interface EditorConfig<T, C extends Editor<T>> {
  form: FormModelGroup<T>
  component: Type<C>
}
