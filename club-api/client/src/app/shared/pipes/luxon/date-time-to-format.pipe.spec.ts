import {DateTimeToFormatPipe} from './date-time-to-format.pipe';
import {DateTime} from "luxon";

describe('DateTimeToFormatPipe', () => {
  const pipe = new DateTimeToFormatPipe();

  it('should transform to valid date time string', () => {
    const result = pipe.transform(DateTime.now(), DateTime.DATE_SHORT);
    expect(result).not.toBeNull();
  });

  it('should transform to null when the input value is null', () => {
    const result = pipe.transform(null, DateTime.DATE_SHORT);
    expect(result).toBeNull();
  });

  it('should transform to be null when the input value is undefined', () => {
    const result = pipe.transform(undefined, DateTime.DATE_SHORT);
    expect(result).toBeNull();
  });
});
