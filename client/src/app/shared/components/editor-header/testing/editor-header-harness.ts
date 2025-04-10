import {ContentContainerComponentHarness} from '@angular/cdk/testing';
import {MatButtonHarness} from '@angular/material/button/testing';

export class EditorHeaderHarness extends ContentContainerComponentHarness {
    static hostSelector = 'app-editor-header';

    protected  getTitleHarness
        = this.locatorForOptional(TitleHarness);

    protected getButtonHarness
        = this.locatorForOptional(MatButtonHarness);

    async removeButton(): Promise<MatButtonHarness | null> {
        return this.getButtonHarness();
    }

    async isButtonVisible(): Promise<boolean> {
        const button = await this.getButtonHarness();
        const visible = (button !== null);
        return Promise.resolve(visible)
    }

    async isTitleVisible(): Promise<boolean> {
        const title = await this.getTitleHarness();
        const visible = (title != null);
        return Promise.resolve(visible);
    }

    async title(): Promise<TitleHarness | null> {
        return this.getTitleHarness();
    }

    async titleText(): Promise<string | null> {
        const title = await this.getTitleHarness();
        if (title != null) {
            return title.titleLabel();
        } else {
            return Promise.resolve(null);
        }
    }
}

export class TitleHarness extends ContentContainerComponentHarness {
    static hostSelector = 'mat-label';

    async titleLabel(): Promise<string> {
        const host = await this.host();
       return host.text();
    }
}
