import {ComponentFixture, TestBed} from '@angular/core/testing';
import {
  MockViewModelHostComponent
} from '@app/shared/components/view-model-component/testing/mock-view-model-host.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';
import {By} from '@angular/platform-browser';

describe('ViewModelComponent', () => {
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

  it('should show edit button when allowEdit is true', async() => {
    fixture.componentRef.setInput('allowEdit', true);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', selector: '#editButton'}));

    expect(harness).toBeTruthy();
  });

  it('should NOT show edit button when allowEdit is false', async() => {
    fixture.componentRef.setInput('allowEdit', false);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', selector: '#editButton'}));

    expect(harness).toBeFalsy();
  });

  it('edit button click should emit event with model', async () => {
    fixture.componentRef.setInput('allowEdit', true);
    const spy = spyOn(component, 'onEdit').and.stub();

    const harness =
      await loader.getHarness(MatButtonHarness.with({variant: 'icon', selector: '#editButton'}));
    await harness.click();

    expect(spy).toHaveBeenCalledWith(component.model());
  });

  it('should show delete button when allowDelete is true', async() => {
    fixture.componentRef.setInput('allowDelete', true);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', selector: '#deleteButton'}));

    expect(harness).toBeTruthy();
  });

  it('should NOT show delete button when allowDelete is false', async() => {
    fixture.componentRef.setInput('allowDelete', false);

    const harness =
      await loader.getHarnessOrNull(MatButtonHarness.with({variant: 'icon', selector: '#deleteButton'}));

    expect(harness).toBeFalsy();
  });

  it('delete button click should emit event with model', async () => {
    fixture.componentRef.setInput('allowDelete', true);
    const spy = spyOn(component, 'onDelete').and.stub();

    const harness =
      await loader.getHarness(MatButtonHarness.with({variant: 'icon', text: 'delete'}));
    await harness.click();

    expect(spy).toHaveBeenCalledWith(component.model());
  });

  it('should render content', async () => {
    fixture.detectChanges();
    await fixture.whenStable();

    const element = fixture.debugElement.query(By.css('span'));
    expect(element).toBeTruthy();
  });
});
