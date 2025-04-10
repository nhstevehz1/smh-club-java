import {Component, WritableSignal, signal, Type} from '@angular/core';
import {FormGroup, FormControl, ReactiveFormsModule} from '@angular/forms';
import {EditorModel} from '@app/shared/components/base-editor/testing/models/EditorModel';
import {EditorOutletDirective} from '@app/shared/components/base-editor/directives/editor-outlet.directive';
import {EditorConfig} from '@app/shared/components/base-editor/models/editor-config';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {MockEditorComponent} from '@app/shared/components/base-editor/testing/mock-editor/mock-editor.component';

@Component({
  selector: 'app-dialog-outlet-host',
  imports: [
    ReactiveFormsModule,
    EditorOutletDirective,
  ],
  templateUrl: './editor-outlet-host.component.html'
})
export class EditorOutletHostComponent {

  form: WritableSignal<FormModelGroup<EditorModel>>;
  component: WritableSignal<Type<MockEditorComponent>>;

  constructor() {

    const config: EditorConfig<EditorModel, MockEditorComponent> = {
      form: new FormGroup({
        id: new FormControl(0, {nonNullable: true}),
        editorField: new FormControl('', {nonNullable: true})
      }),
      component: MockEditorComponent
    }

    this.form = signal(config.form);
    this.component = signal(config.component);
  }
}
