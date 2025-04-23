import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewModelListComponent} from './view-model-list.component';
import {
  ViewModelListHostComponent
} from '@app/shared/components/view-model-list/testing/view-model-list-host/view-model-list-host.component';
import {ViewListModel, ViewModelListTest} from '@app/shared/components/view-model-list/testing/test-support';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness, ButtonHarnessFilters} from '@angular/material/button/testing';
import {ViewModelHarness} from '@app/shared/components/view-model-component/testing/view-model-harness';

describe('ViewModelListComponent', () => {
  let component: ViewModelListHostComponent;
  let fixture: ComponentFixture<ViewModelListHostComponent>;
  let loader: HarnessLoader;

  let models: ViewListModel[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewModelListComponent]
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

    it('should show the correct number of items', async () => {
      models = ViewModelListTest.generateModelList(10);
      component.models.set(models);
      const harnesses = await loader.getAllHarnesses(ViewModelHarness);
      expect(harnesses.length).toEqual(models.length);
    });
  });

  describe('component interaction', () => {
    const addBtnFilter: ButtonHarnessFilters = {selector: '#addButton', variant: 'icon'};
    const editBtnFilter: ButtonHarnessFilters = {selector: '#editButton', variant: 'icon'};
    const deleteBtnFilter: ButtonHarnessFilters = {selector: '#deleteButton', variant: 'icon'};

    beforeEach(() => {
      models = ViewModelListTest.generateModelList(1);
      component.models.set(models);
    });

    it('should emit addClicked event', async () => {
      fixture.componentRef.setInput('allowAdd', true);
      const spy = spyOn(component, 'onAdd').and.stub();
      const harness = await loader.getHarness(MatButtonHarness.with(addBtnFilter));
      await harness.click();
      expect(spy).toHaveBeenCalled();
    });

    it('should emit editItemClicked event', async () => {
      fixture.componentRef.setInput('allowEdit', true);
      const spy = spyOn(component, 'onEditItem').and.stub();
      const harness = await loader.getHarness(MatButtonHarness.with(editBtnFilter));
      await harness.click();
      expect(spy).toHaveBeenCalledWith(models[0]);
    });

    it('should emit deleteItemClick event', async () => {
      fixture.componentRef.setInput('allowDelete', true);
      const spy = spyOn(component, 'onDeleteItem').and.stub();
      const harness = await loader.getHarness(MatButtonHarness.with(deleteBtnFilter));
      await harness.click();
      expect(spy).toHaveBeenCalledWith(models[0]);
    });
  });
});
