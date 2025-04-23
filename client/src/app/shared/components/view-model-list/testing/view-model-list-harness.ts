import {ComponentHarness} from '@angular/cdk/testing';

export class ViewModelListHarness extends ComponentHarness {
  static hostSelector = 'app-view-model-list'

  protected getAddButton = this.locatorFor('#addButton');

  async addClick(): Promise<void> {
    const button = await this.getAddButton();
    return button.click();
  }
}
