import {Directive, ElementRef, Input, Renderer2} from '@angular/core';

@Directive({
  selector: '[appConditionalDirective]',
  standalone: true
})
export class ConditionalDirective {

  @Input({required: true}) set appConditionalDirective(classObject: {[key: string]: boolean}) {
    console.debug('inside set');
    for (const key in classObject) {
      console.debug('for key', key)
      if(classObject[key]) {
        console.debug('inside true');
        this.renderer.addClass(this.element.nativeElement, key)
      } else {
        console.debug('inside false')
        this.renderer.removeClass(this.element.nativeElement, key);
      }
    }
  }

  constructor(private element: ElementRef, private renderer: Renderer2) { }

}
