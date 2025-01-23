import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {provideRouter, RouterLink} from "@angular/router";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {MatDividerModule} from "@angular/material/divider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {provideNoopAnimations} from "@angular/platform-browser/animations";

describe('HeaderComponent', () => {
  let fixture: ComponentFixture<HeaderComponent>;
  let component: HeaderComponent;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        MatToolbarModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        RouterLink,
        MatTooltipModule,
        MatSlideToggleModule,
      ],
      providers: [
        HeaderComponent,
        provideNoopAnimations(),
        provideRouter([])
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
