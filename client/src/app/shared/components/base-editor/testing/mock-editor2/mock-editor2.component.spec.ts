import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MockEditor2Component } from './mock-editor2.component';

describe('MockEditor2Component', () => {
  let component: MockEditor2Component;
  let fixture: ComponentFixture<MockEditor2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MockEditor2Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MockEditor2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
