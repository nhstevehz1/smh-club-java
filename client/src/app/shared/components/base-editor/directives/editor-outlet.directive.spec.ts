import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TranslateModule} from '@ngx-translate/core';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {
  EditorOutletHostComponent
} from '@app/shared/components/base-editor/testing/editor-outlet-host/editor-outlet-host.component';
import {MockEditor2Component} from '@app/shared/components/base-editor/testing/mock-editor2/mock-editor2.component';
import {HarnessLoader} from '@angular/cdk/testing';
import {EditorHarness} from '@app/shared/components/base-editor/testing/editor-harness';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';

describe('DialogOutletDirective', () => {
  let component: EditorOutletHostComponent;
  let fixture: ComponentFixture<EditorOutletHostComponent>;
  let loader: HarnessLoader;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        EditorOutletHostComponent,
        MockEditor2Component,
        TranslateModule.forRoot({})
      ],
      providers: [
        provideNoopAnimations(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EditorOutletHostComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  fit('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  fit('should contain embedded component', async () => {
    const harness = await loader.getHarnessOrNull(EditorHarness);
    expect(harness).toBeTruthy();
  });
});
