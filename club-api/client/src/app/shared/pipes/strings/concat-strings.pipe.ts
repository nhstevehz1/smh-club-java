import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'concatStrings'
})
export class ConcatStringsPipe implements PipeTransform {
  transform(value: string | null | undefined, value2: string | null | undefined): string {
    let val1 = value ? value : '';
    let val2 = value2 ? value2 : '';
    let combined = `${val1} ${val2}`;
    return combined.trim();
  }
}
