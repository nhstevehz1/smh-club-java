import {Pipe, PipeTransform} from '@angular/core';
import {DateTime} from "luxon";

@Pipe({
  name: 'dateTimeToShort'
})
export class DateTimeToShortPipe implements PipeTransform {

  transform <T extends DateTime | null | undefined>(value: T) {
    return (value == null ? null : value.toLocaleString(DateTime.DATETIME_SHORT)) as T extends DateTime ? string : null;
  }

}
