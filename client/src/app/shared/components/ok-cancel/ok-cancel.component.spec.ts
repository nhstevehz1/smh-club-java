import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {By} from '@angular/platform-browser';

import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';

import {TranslateModule} from '@ngx-translate/core';

import {OkCancelComponent} from './ok-cancel.component';

describe('OkCancelComponent', () => {
  let component: OkCancelComponent;
  let fixture: ComponentFixture<OkCancelComponent>;
  let loader: HarnessLoader;
  let harness: MatButtonHarness | null;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
          OkCancelComponent,
          TranslateModule.forRoot({})
      ],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OkCancelComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    harness = await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon'}));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show a title message', async() => {
    const title = 'title';
    fixture.componentRef.setInput('title', title);

    fixture.detectChanges()
    await fixture.whenStable();

    const native = fixture.debugElement.query(By.css('.card-title'));
    expect(native).toBeTruthy();
    expect(native.nativeElement.textContent).toBe(title);
  });

  it('should contain button', async () => {
    expect(harness).toBeTruthy();
  });

  it('should call onButtonClicked', async () => {
    const spy = spyOn(component, 'onButtonClicked').and.stub();
    await harness?.click();
    expect(spy).toHaveBeenCalled();
  })
});
