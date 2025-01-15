import {MockInstance, MockService, ngMocks} from 'ng-mocks';
import {getTestBed} from "@angular/core/testing";
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from "@angular/platform-browser-dynamic/testing";
import {DefaultTitleStrategy, TitleStrategy} from "@angular/router";
import {ApplicationModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";

ngMocks.autoSpy('jasmine');

getTestBed().initTestEnvironment(
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting(),
    {
        errorOnUnknownElements: true,
        errorOnUnknownProperties: true,
    },
);

ngMocks.defaultMock(TitleStrategy, () => MockService(DefaultTitleStrategy));
ngMocks.globalKeep(ApplicationModule, true);
ngMocks.globalKeep(CommonModule, true);
ngMocks.globalKeep(BrowserModule, true);

jasmine.getEnv().addReporter({
    specDone: MockInstance.restore,
    specStarted: MockInstance.remember,
    suiteDone: MockInstance.restore,
    suiteStarted: MockInstance.remember,
});
