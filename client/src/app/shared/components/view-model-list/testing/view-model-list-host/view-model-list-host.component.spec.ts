import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewModelListHostComponent } from './view-model-list-host.component';

describe('ViewModelListHostComponent', () => {
  let component: ViewModelListHostComponent;
  let fixture: ComponentFixture<ViewModelListHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewModelListHostComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewModelListHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
