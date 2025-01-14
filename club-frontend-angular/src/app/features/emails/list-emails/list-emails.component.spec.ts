import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListEmailsComponent } from './list-emails.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {EmailService} from "../services/email.service";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ListEmailsComponent', () => {
  let component: ListEmailsComponent;
  let fixture: ComponentFixture<ListEmailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListEmailsComponent],
      providers: [
        EmailService,
        provideAnimations(),
        provideHttpClient(),
        provideHttpClientTesting()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListEmailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
