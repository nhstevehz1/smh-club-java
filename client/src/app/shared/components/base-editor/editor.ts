import {InputSignal} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {MatFormFieldAppearance} from '@angular/material/form-field';

export interface Editor<T> {
  editorForm: InputSignal<FormModelGroup<T> | undefined>;
  fieldAppearance: InputSignal<MatFormFieldAppearance>;
}
