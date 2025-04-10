import { TestBed } from '@angular/core/testing';

import {TranslateModule} from '@ngx-translate/core';
import {DateTimeToFormatPipe} from '@app/shared/pipes';

import { MemberTableService } from './member-table.service';

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
