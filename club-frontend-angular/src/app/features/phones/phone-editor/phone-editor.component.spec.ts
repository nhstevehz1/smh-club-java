import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhoneEditorComponent } from './phone-editor.component';

describe('PhoneEditorComponent', () => {
  let component: PhoneEditorComponent;
  let fixture: ComponentFixture<PhoneEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhoneEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhoneEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
