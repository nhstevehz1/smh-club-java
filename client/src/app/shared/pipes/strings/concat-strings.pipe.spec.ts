import {ConcatStringsPipe} from './concat-strings.pipe';

describe('ConcatStringsPipe', () => {
  const pipe = new ConcatStringsPipe();

  it('should concatenate two strings', () => {
    const result = pipe.transform('str1', 'str2');
    const expected = 'str1 str2';
    expect(result).toEqual(expected);
  });

  it('should concatenate two strings when first input is null', () => {
    const result = pipe.transform(null, 'str2');
    const expected = 'str2';
    expect(result).toEqual(expected);
  });

  it('should concatenate two strings when first input is undefined', () => {
    const result = pipe.transform(undefined, 'str2');
    const expected = 'str2';
    expect(result).toEqual(expected);
  });

  it('should concatenate two strings when second input is null', () => {
    const result = pipe.transform('str1', null);
    const expected = 'str1';
    expect(result).toEqual(expected);
  });

  it('should concatenate two strings when second input is undefined', () => {
    const result = pipe.transform('str1', undefined);
    const expected = 'str1';
    expect(result).toEqual(expected);
  });

  it('should return empty string when both inputs are null', () => {
    const result = pipe.transform(null, null);
    const expected = '';
    expect(result).toEqual(expected);
  });

  it('should return empty string when both inputs are undefined', () => {
    const result = pipe.transform(undefined, undefined);
    const expected = '';
    expect(result).toEqual(expected);
  });

  it('should return empty string when first input is null and second is undefined', () => {
    const result = pipe.transform(null, undefined);
    const expected = '';
    expect(result).toEqual(expected);
  });

  it('should return empty string when first input is undefined and second is null', () => {
    const result = pipe.transform(undefined, null);
    const expected = '';
    expect(result).toEqual(expected);
  });
});
