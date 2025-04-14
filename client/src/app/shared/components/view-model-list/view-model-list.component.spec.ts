import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewModelListComponent } from './view-model-list.component';

describe('ViewModelListComponent', () => {
  let component: ViewModelListComponent;
  let fixture: ComponentFixture<ViewModelListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewModelListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewModelListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
