import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataDisplayComponent } from './data-display.component';
import {TranslateModule} from '@ngx-translate/core';

describe('DataDisplayComponent', () => {
  let component: DataDisplayComponent;
  let fixture: ComponentFixture<DataDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DataDisplayComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DataDisplayComponent);
    component = fixture.componentInstance;
  });

  it('should create', async () => {
    fixture.componentRef.setInput('data', 'my data');
    fixture.componentRef.setInput('label', 'my label');

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });
});
