import {AfterViewInit, Directive, ElementRef, inject, Renderer2} from '@angular/core';

/**
 * @description
 * Directive, which add the `role` attribute with a value of `radio` to the element
 * on which it is applied.
 *
 * @usageNotes
 * This directive should be used in addition to Oblique's `obSelectable` directive applied
 * on a `mat-card` element. The raison for this custom directive is that even if the parent
 * `obSelectableGroup` has an attribute `mode="radio"` the selectable cards display a checkbox.
 */
@Directive({
  selector: '[appRadioCard]'
})
export class RadioCardDirective implements AfterViewInit {
  private readonly el = inject(ElementRef);
  private readonly renderer = inject(Renderer2);

  ngAfterViewInit(): void {
    this.renderer.setAttribute(this.el.nativeElement, 'role', 'radio');
  }
}
