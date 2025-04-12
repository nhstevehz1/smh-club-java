import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewEmailComponent} from './view-email.component';
import {Email, EmailType} from '@app/features/emails/models';
import {EmailTest} from '@app/features/emails/testing';
import {MatIconHarness} from '@angular/material/icon/testing';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {DataDisplayHarness} from '@app/shared/components/data-display/testing/data-display-harness';

describe('ViewEmailComponent', () => {
  let component: ViewEmailComponent;
  let fixture: ComponentFixture<ViewEmailComponent>;
  let loader: HarnessLoader;

  let email: Email;

  beforeEach(async () => {
    email = EmailTest.generateEmail();

    await TestBed.configureTestingModule({
      imports: [ViewEmailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewEmailComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  fit('should create', async () => {
    fixture.componentRef.setInput('email', email);

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  fit('should display an icon', async () => {
    fixture.componentRef.setInput('email', email);
    const harness = await loader.getHarnessOrNull(MatIconHarness);
    expect(harness).toBeTruthy();
  });

  fit('should display the home icon', async () => {
    email.email_type = EmailType.Home;
    fixture.componentRef.setInput('email', email);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('home');
  });

  fit('should display the work icon', async () => {
    email.email_type = EmailType.Work;
    fixture.componentRef.setInput('email', email);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('corporate_fare');
  });

  fit('should display the other icon', async () => {
    email.email_type = EmailType.Other;
    fixture.componentRef.setInput('email', email);

    const harness = await loader.getHarness(MatIconHarness);
    const name = await harness.getName();
    expect(name).toEqual('question_mark');
  });

  fit('should display email', async () => {
    fixture.componentRef.setInput('email', email);

    const harness = await
      loader.getHarnessOrNull(DataDisplayHarness.with({value: email.email}));

    expect(harness).toBeTruthy();
  });
});
