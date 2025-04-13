import { ComponentFixture, TestBed } from '@angular/core/testing';
import {Location} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';

import { ViewMemberComponent } from './view-member.component';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTest} from '@app/features/members/testing/test-support';
import {asyncData} from '@app/shared/testing';
import {Member} from '@app/features/members/models';
import {HarnessLoader} from '@angular/cdk/testing';
import {TestbedHarnessEnvironment} from '@angular/cdk/testing/testbed';
import {MatButtonHarness} from '@angular/material/button/testing';
import {DataDisplayComponent} from '@app/shared/components/data-display/data-display.component';
import {DataDisplayHarness} from '@app/shared/components/data-display/testing/data-display-harness';

describe('ViewMemberComponent', () => {
  let component: ViewMemberComponent;
  let fixture: ComponentFixture<ViewMemberComponent>;
  let loader: HarnessLoader;

  let member: Member;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [
        ViewMemberComponent,
        TranslateModule.forRoot({})
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ViewMemberComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);

    member = MemberTest.generateMember(1);
  });

  it('should create', async () => {
    fixture.componentRef.setInput('member', member);

    fixture.detectChanges();
    await fixture.whenStable();

    expect(component).toBeTruthy();
  });

  describe('test component', () => {
    beforeEach(() => {
      fixture.componentRef.setInput('member', member);
    });

    it('should contain the correct number of app-data-display elements', async ()=> {
      const harnesses = await loader.getAllHarnesses(DataDisplayHarness);
      expect(harnesses.length).toEqual(4);
    });

    it('should should render full name', async ()=> {
      const harness =
        await loader.getHarnessOrNull(DataDisplayHarness.with({selector: '#fullName'}));
      expect(harness).toBeTruthy();
    });

    it('full name should have the correct label', async ()=> {
      const harness =
        await loader.getHarness(DataDisplayHarness.with({selector: '#fullName'}));
      const label = await harness.getLabel();
      expect(label).toEqual('members.view.member');
    });

    it('should should render member number', async ()=> {
      const harness =
        await loader.getHarnessOrNull(DataDisplayHarness.with({selector: '#memberNumber'}));
      expect(harness).toBeTruthy();
    });

    it('member number should contain the correct label', async ()=> {
      const harness =
        await loader.getHarness(DataDisplayHarness.with({selector: '#memberNumber'}));
      const label = await harness.getLabel();
      expect(label).toEqual('members.view.number');
    });

    it('should should render birth date', async ()=> {
      const harness =
        await loader.getHarnessOrNull(DataDisplayHarness.with({selector: '#birthDate'}));
      expect(harness).toBeTruthy();
    });

    it('birth date should have the correct label', async ()=> {
      const harness =
        await loader.getHarness(DataDisplayHarness.with({selector: '#birthDate'}));
      const label = await harness.getLabel();
      expect(label).toEqual('members.view.birthDate');
    });

    it('should should render joined date', async ()=> {
      const harness =
        await loader.getHarnessOrNull(DataDisplayHarness.with({selector: '#joinedDate'}));
      expect(harness).toBeTruthy();
    });

    it('joined date should have the correct label', async ()=> {
      const harness =
        await loader.getHarness(DataDisplayHarness.with({selector: '#joinedDate'}));
      const label = await harness.getLabel();
      expect(label).toEqual('members.view.joinedDate');
    });
  });
});
