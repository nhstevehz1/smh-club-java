import {DateTimeFromIsoPipe} from './date-time-from-iso.pipe';

describe('DateTimeFromIsoPipe', () => {
  it('create an instance', () => {
    const pipe = new DateTimeFromIsoPipe();
    expect(pipe).toBeTruthy();
  });
});
