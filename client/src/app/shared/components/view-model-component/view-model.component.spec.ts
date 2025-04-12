import {ComponentFixture, TestBed} from '@angular/core/testing';
import {
  MockViewModelHostComponent
} from '@app/shared/components/view-model-component/testing/mock-view-model-host.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';

describe('BaseViewListPanelComponent', () => {
  let component: MockViewModelHostComponent;
  let fixture: ComponentFixture<MockViewModelHostComponent>;
  let loader: HarnessLoader;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MockViewModelHostComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(MockViewModelHostComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should contain one button when showAllButtons is false', async () => {
    const harnesses = await loader.getAllHarnesses(MatButtonHarness);
    expect(harnesses.length).toEqual(0);
  });

  it('show contain two buttons when showAllButtons is true', async () => {
    fixture.componentRef.setInput('showButtons', true);
    const harnesses = await loader.getAllHarnesses(MatButtonHarness);
    expect(harnesses.length).toEqual(2);
  })

  it('should show edit button when showEditButton is true and showAllButtons is false', async() => {
    fixture.componentRef.setInput('showEdit', true);
    fixture.componentRef.setInput('showButtons', false);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', text: 'edit'}));

    expect(harness).toBeTruthy();
  });

  it('should show edit button when showEditButton is false and showAllButtons is true', async() => {
    fixture.componentRef.setInput('showEdit', false);
    fixture.componentRef.setInput('showButtons', true);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', text: 'edit'}));

    expect(harness).toBeTruthy();
  });

  it('edit button click should emit event with model', async () => {
    fixture.componentRef.setInput('showEdit', true);
    const spy = spyOn(component, 'onEdit').and.stub();

    const harness =
      await loader.getHarness(MatButtonHarness.with({variant: 'icon', text: 'edit'}));
    await harness.click();

    expect(spy).toHaveBeenCalledWith(component.model());
  });

  it('should show delete button when showDeleteButton is true and showAllButtons is false', async() => {
    fixture.componentRef.setInput('showDelete', true);
    fixture.componentRef.setInput('showButtons', false);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', text: 'delete'}));

    expect(harness).toBeTruthy();
  });

  it('should show delete button when showDeleteButton is false and showAllButtons is true', async() => {
    fixture.componentRef.setInput('showDelete', false);
    fixture.componentRef.setInput('showButtons', true);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', text: 'delete'}));

    expect(harness).toBeTruthy();
  });

  it('delete button click should emit event with model', async () => {
    fixture.componentRef.setInput('showDelete', true);
    const spy = spyOn(component, 'onDelete').and.stub();

    const harness =
      await loader.getHarness(MatButtonHarness.with({variant: 'icon', text: 'delete'}));
    await harness.click();

    expect(spy).toHaveBeenCalledWith(component.model());
  });
});
