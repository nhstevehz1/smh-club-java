import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAddressDialogComponent } from './edit-address-dialog.component';

describe('UpdateAddressDialogComponent', () => {
  let component: EditAddressDialogComponent;
  let fixture: ComponentFixture<EditAddressDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditAddressDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditAddressDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
