import {TestBed} from '@angular/core/testing';
import {AddressEditDialogService} from './address-edit-dialog.service';

describe('AddressEditDialogService', () => {
  let service: AddressEditDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AddressEditDialogService]
    });
    service = TestBed.inject(AddressEditDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
