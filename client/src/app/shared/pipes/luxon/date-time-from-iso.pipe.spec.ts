import {DateTimeFromIsoPipe} from './date-time-from-iso.pipe';
import {DateTime} from 'luxon';

describe('DateTimeFromIsoPipe', () => {
  const pipe = new DateTimeFromIsoPipe();

  it('should transform valid date/time ISO string to DatTime object', () => {
    const dateStr = '2025-01-23';

    const result = pipe.transform(dateStr);

    expect(result).toEqual(DateTime.fromISO(dateStr));
  });

  it('should transform to null when input is null', () => {
    const result = pipe.transform(null);

    expect(result).toBeNull();
  });

  it('should transform to be null when input is undefined', () => {
    const result = pipe.transform(undefined);

    expect(result).toEqual(null);
  });
});
