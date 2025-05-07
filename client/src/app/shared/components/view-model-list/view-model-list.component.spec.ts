import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewModelListComponent} from './view-model-list.component';
import {
  ViewModelListHostComponent
} from '@app/shared/components/view-model-list/testing/view-model-list-host/view-model-list-host.component';
import {ViewListModel, ViewModelListTest} from '@app/shared/components/view-model-list/testing/test-support';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness, ButtonHarnessFilters} from '@angular/material/button/testing';
import {TranslateModule} from '@ngx-translate/core';

describe('ViewModelListComponent', () => {
  let component: ViewModelListHostComponent;
  let fixture: ComponentFixture<ViewModelListHostComponent>;
  let loader: HarnessLoader;

  let models: ViewListModel[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ViewModelListComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewModelListHostComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  describe('component rendering', () => {
    const addBtnFilter: ButtonHarnessFilters = {selector: '#addButton', variant: 'icon'};

    it('should show add button when allowAdd is true', async () => {
      fixture.componentRef.setInput('allowAdd', true);
      const harness = await loader.getHarnessOrNull(MatButtonHarness.with(addBtnFilter))
      expect(harness).toBeTruthy();
    });

    it('should NOT show add button when allowAdd is false', async () => {
      fixture.componentRef.setInput('allowAdd', false);
      const harness = await loader.getHarnessOrNull(MatButtonHarness.with(addBtnFilter))
      expect(harness).toBeFalsy();
    });
  });

  describe('component interaction', () => {
    const addBtnFilter: ButtonHarnessFilters = {selector: '#addButton', variant: 'icon'};

    beforeEach(() => {
      models = ViewModelListTest.generateModelList(1);
      component.list.set(models);
    });

    it('should emit addClicked event', async () => {
      fixture.componentRef.setInput('allowAdd', true);
      const spy = spyOn(component, 'onAdd').and.stub();
      const harness = await loader.getHarness(MatButtonHarness.with(addBtnFilter));
      await harness.click();
      expect(spy).toHaveBeenCalled();
    });
  });
});
