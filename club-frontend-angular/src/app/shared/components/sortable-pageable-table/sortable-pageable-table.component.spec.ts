import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortablePageableTableComponent } from './sortable-pageable-table.component';
import {provideAnimations} from "@angular/platform-browser/animations";

describe('SortablePageableTableComponent', () => {
  let component: SortablePageableTableComponent;
  let fixture: ComponentFixture<SortablePageableTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SortablePageableTableComponent],
      providers: [provideAnimations()]
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
