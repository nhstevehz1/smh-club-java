import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewModelListComponent } from './view-model-list.component';
import {
  ViewModelListHostComponent
} from '@app/shared/components/view-model-list/testing/view-model-list-host/view-model-list-host.component';

describe('ViewModelListComponent', () => {
  let component: ViewModelListHostComponent;
  let fixture: ComponentFixture<ViewModelListHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewModelListComponent]
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
