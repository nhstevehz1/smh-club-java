import {ComponentHarness} from '@angular/cdk/testing';

export class ViewModelHarness extends ComponentHarness {
  static hostSelector = 'app-view-model';

  protected getEditButton = this.locatorFor('#editButton');
  protected getDeleteButton = this.locatorFor('#deleteButton');

  async editClick(): Promise<void> {
    const button = await this.getEditButton();
    return button.click();
  }

  async deleteClick(): Promise<void> {
    const button = await this.getDeleteButton();
    return button.click();
  }
}
