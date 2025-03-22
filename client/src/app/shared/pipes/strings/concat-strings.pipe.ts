import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'concatStrings'
})
export class ConcatStringsPipe implements PipeTransform {
  transform(value: string | null | undefined, value2: string | null | undefined): string {
    const val1 = value ? value : '';
    const val2 = value2 ? value2 : '';
    const combined = `${val1} ${val2}`;
    return combined.trim();
  }
}
