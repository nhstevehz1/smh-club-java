import {Component, WritableSignal, signal, Type} from '@angular/core';
import {FormGroup, FormControl, ReactiveFormsModule} from '@angular/forms';
import {EditorModel} from '@app/shared/components/base-editor/testing/models/EditorModel';
import {EditorOutletDirective} from '@app/shared/components/base-editor/directives/editor-outlet.directive';
import {EditorConfig} from '@app/shared/components/base-editor/models/editor-config';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {MockEditor2Component} from '@app/shared/components/base-editor/testing/mock-editor2/mock-editor2.component';

@Component({
  selector: 'app-dialog-outlet-host',
  imports: [
    ReactiveFormsModule,
    EditorOutletDirective,
  ],
  templateUrl: './editor-outlet-host.component.html',
  styleUrl: './editor-outlet-host.component.scss'
})
export class EditorOutletHostComponent {

  form: WritableSignal<FormModelGroup<EditorModel>>;
  component: WritableSignal<Type<MockEditor2Component>>;

  constructor() {

    const config: EditorConfig<EditorModel, MockEditor2Component> = {
      form: new FormGroup({
        id: new FormControl(0, {nonNullable: true}),
        editorField: new FormControl('', {nonNullable: true})
      }),
      component: MockEditor2Component
    }

    this.form = signal(config.form);
    this.component = signal(config.component);
  }
}
