import {RadioCardDirective} from './radio-card.directive';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Component, ElementRef, Renderer2} from '@angular/core';
import {By} from '@angular/platform-browser';

@Component({
  selector: 'app-test-component',
  template: ` <div appRadioCard></div>`,
  imports: [RadioCardDirective]
})
class TestComponent {}

class MockElementRef extends ElementRef {}

const el = new MockElementRef(document.createElement('div'));

describe('RadioCardDirective', () => {
  let fixture: ComponentFixture<TestComponent>;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [{provide: ElementRef, useValue: el}, Renderer2]
    });
    fixture = TestBed.createComponent(TestComponent);
    fixture.detectChanges();
  });

  it('should create an instance', () => {
    TestBed.runInInjectionContext(() => {
      const directive = new RadioCardDirective();
      expect(directive).toBeTruthy();
    });
  });

  it('should add the role attribute with a value of radio', () => {
    const el = fixture.debugElement.query(By.directive(RadioCardDirective));
    expect(el.nativeElement.hasAttribute('role')).toBeTruthy();
    expect(el.nativeElement.getAttribute('role')).toBe('radio');
  });
});
