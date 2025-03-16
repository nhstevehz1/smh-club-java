import {TestBed} from '@angular/core/testing';

import {CustomMatPaginatorIntlService} from './custom-mat-paginator-intl.service';
import {MatPaginatorIntl} from "@angular/material/paginator";
import {TranslateModule} from "@ngx-translate/core";

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
