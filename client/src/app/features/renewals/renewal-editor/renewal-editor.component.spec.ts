import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenewalEditorComponent } from './renewal-editor.component';

describe('RenewalEditorComponent', () => {
  let component: RenewalEditorComponent;
  let fixture: ComponentFixture<RenewalEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenewalEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenewalEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
