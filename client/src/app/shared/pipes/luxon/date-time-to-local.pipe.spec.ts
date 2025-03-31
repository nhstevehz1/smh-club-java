import {DateTimeToLocalPipe} from './date-time-to-local.pipe';
import {DateTime} from 'luxon';

describe('DateTimeToLocalPipe', () => {
  const pipe = new DateTimeToLocalPipe();

  it('should transform to a local date/time', () => {
    const now = DateTime.now();
    const result = pipe.transform(now);
    expect(result).toEqual(now.toLocal());
  });

  it('should transform to null when input is null', () => {
    const result = pipe.transform(null);
    expect(result).toBeNull();
  });

  it('should transform to be null when input is undefined', () => {
    const result = pipe.transform(undefined);
    expect(result).toBeNull();
  });
});
