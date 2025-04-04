import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TranslateModule} from '@ngx-translate/core';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {
  EditorOutletHostComponent
} from '@app/shared/components/base-editor/testing/editor-outlet-host/editor-outlet-host.component';
import {MockEditor2Component} from '@app/shared/components/base-editor/testing/mock-editor2/mock-editor2.component';
import {By} from '@angular/platform-browser';

describe('DialogOutletDirective', () => {
  let component: EditorOutletHostComponent;
  let fixture: ComponentFixture<EditorOutletHostComponent>;

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
  });

  it('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });

  it('should contain embedded component', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    const element = fixture.debugElement.query(By.css('app-mock-editor2'));
    expect(element).toBeTruthy();
  });
});
