import {TestBed} from '@angular/core/testing';
import {MatPaginatorIntl} from '@angular/material/paginator';
import {TranslateModule} from '@ngx-translate/core';

import {CustomMatPaginatorIntlService} from './custom-mat-paginator-intl.service';

describe('CustomMatPaginatorIntlService', () => {
  let service: CustomMatPaginatorIntlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
          TranslateModule.forRoot({})
      ],
      providers: [
        MatPaginatorIntl,
        CustomMatPaginatorIntlService
      ]
    });
    service = TestBed.inject(CustomMatPaginatorIntlService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
