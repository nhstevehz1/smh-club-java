import {ComponentHarness, BaseHarnessFilters, HarnessPredicate} from '@angular/cdk/testing';

export class DataDisplayHarness extends ComponentHarness {
  static hostSelector = 'app-data-display';

  protected getLabelElement = this.locatorForOptional('.label');
  protected getValueElement = this.locatorFor('.value');

  // get the label text
  async getLabel(): Promise<string | null>  {
    const element = await this.getLabelElement();
    if(element != null){
      return element.text();
    } else {
      return Promise.resolve(null);
    }
  }

  // get the value text
  async getValue(): Promise<string>  {
    const element = await this.getValueElement();
    return element.text();
  }

  static with(options: DataDisplayHarnessFilters): HarnessPredicate<DataDisplayHarness> {
    return new HarnessPredicate(DataDisplayHarness, options)
      .addOption('label text', options.label,
        (harness, text) =>
          HarnessPredicate.stringMatches(harness.getLabel(), text))
      .addOption('value text', options.value,
        (harness, text) =>
          HarnessPredicate.stringMatches(harness.getValue(), text));
  }
}

export interface DataDisplayHarnessFilters extends BaseHarnessFilters {
  label?: string;
  value?: string;
}
