import {Directive, input, ViewContainerRef, OnInit, Type} from '@angular/core';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

@Directive({
  selector: '[appEditorOutlet]'
})
export class EditorOutletDirective<T, C extends BaseEditorComponent<T>> implements OnInit {

  component = input.required<Type<C>>();
  form = input.required<FormModelGroup<T>>();

  constructor(private viewContainerRef: ViewContainerRef) {}

  ngOnInit(): void {
    const componentRef = this.viewContainerRef.createComponent(this.component());
    componentRef.setInput('editorForm', this.form());
  }
}
