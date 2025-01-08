import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortablePageableTableComponent } from './sortable-pageable-table.component';

describe('SortablePageableTableComponent', () => {
  let component: SortablePageableTableComponent;
  let fixture: ComponentFixture<SortablePageableTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SortablePageableTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SortablePageableTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
