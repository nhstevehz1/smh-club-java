import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListEmailsComponent } from './list-emails.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {EmailService} from "../services/email.service";
import {BrowserAnimationsModule, provideAnimations} from "@angular/platform-browser/animations";
import {MockBuilder, MockRender} from "ng-mocks";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";

describe('ListEmailsComponent', () => {

  beforeEach(async () => {
    return MockBuilder(ListEmailsComponent)
        .keep(SortablePageableTableComponent)
        .keep(BrowserAnimationsModule);
  });

  it('should create', () => {
    const fixture = MockRender(ListEmailsComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
