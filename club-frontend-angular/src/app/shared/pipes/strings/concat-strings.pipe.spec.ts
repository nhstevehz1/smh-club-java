import {ConcatStringsPipe} from './concat-strings.pipe';

describe('ConcatStringsPipe', () => {
  it('create an instance', () => {
    const pipe = new ConcatStringsPipe();
    expect(pipe).toBeTruthy();
  });
});
