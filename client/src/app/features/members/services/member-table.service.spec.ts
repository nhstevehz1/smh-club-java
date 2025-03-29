import { TestBed } from '@angular/core/testing';

import { MemberTableService } from './member-table.service';
import {TranslateModule} from '@ngx-translate/core';
import {DateTimeToFormatPipe} from '../../../shared/pipes/luxon/date-time-to-format.pipe';

describe('MemberTableService', () => {
  let service: MemberTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ TranslateModule.forRoot({})],
      providers: [
        MemberTableService,
        DateTimeToFormatPipe
      ]
    });
    service = TestBed.inject(MemberTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
