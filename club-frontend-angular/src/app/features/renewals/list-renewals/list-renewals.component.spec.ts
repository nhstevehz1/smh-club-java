import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRenewalsComponent } from './list-renewals.component';

describe('ListRenewalsComponent', () => {
  let component: ListRenewalsComponent;
  let fixture: ComponentFixture<ListRenewalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListRenewalsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListRenewalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
