import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditorOutletHostComponent} from './editor-outlet-host.component';
import {TranslateModule} from '@ngx-translate/core';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {MockEditor2Component} from '@app/shared/components/base-editor/testing/mock-editor2/mock-editor2.component';

// Testing performed in DialogOutletDirective unit tests.
describe('DialogOutletHostComponent', () => {
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
        provideNoopAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorOutletHostComponent);
    component = fixture.componentInstance;

  });

  fit('should create', async () => {
    fixture.detectChanges();
    await fixture.whenStable();
    expect(component).toBeTruthy();
  });
});
