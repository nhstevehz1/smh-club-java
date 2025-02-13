import {Component, inject, Input, OnInit} from '@angular/core';
import {ControlContainer, FormControl, FormGroup} from "@angular/forms";
import {MatFormFieldAppearance} from "@angular/material/form-field";

@Component({
  selector: 'base-editor',
  imports: [],
  template: ``,
  styles: ``,
  viewProviders: [
    {
      provide: ControlContainer,
      useFactory: (): ControlContainer => inject(ControlContainer, {skipSelf: true})
    }
  ]
})
export abstract class BaseEditorComponent implements OnInit {
  private parentContainer = inject(ControlContainer);

  private get parentFormGroup(): FormGroup {
    return this.parentContainer.control as FormGroup;
  }

  private fg?: FormGroup;

  public get formGroup(): FormGroup {
    return this.fg!;
  }

  @Input({required: true})
  public controlKey = '';

  @Input()
  public title?: string;

  @Input()
  public fieldAppearance: MatFormFieldAppearance = 'outline'

  ngOnInit() {
    console.log('ngOnInit in base editor fired')
    this.fg = this.createGroup();
    this.parentFormGroup.addControl(this.controlKey, this.createGroup());
  }

  hasError(formControl: FormControl): boolean {
    return formControl.invalid && (formControl.dirty || !formControl.untouched)
  }

  protected abstract createGroup(): FormGroup;
}
