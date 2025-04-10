import {ComponentFixture, TestBed} from '@angular/core/testing';
import {By} from '@angular/platform-browser';
import {TranslateModule} from '@ngx-translate/core';

import {TitlePageComponent} from './title-page.component';

describe('TitlePageComponent', () => {
  let component: TitlePageComponent;
  let fixture: ComponentFixture<TitlePageComponent>;
  const title = 'testTitle';
  const subTitle = 'testSubTitle';
  const iconName = 'testIconName';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        TitlePageComponent,
        TranslateModule.forRoot({})
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TitlePageComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('title', title);
    fixture.componentRef.setInput('subTitle', subTitle);
  });

  describe('title and sub title tests', () => {
    beforeEach(async () => {
      fixture.detectChanges();
      await fixture.whenStable();
    });

    it('should create', async  () => {
      expect(component).toBeTruthy();
    });

    it('should create a title', () => {
      const element = fixture.debugElement.query(By.css('.title'));
      expect(element).toBeTruthy();
    });

    it('should contain correct title', () => {
      const titleText = fixture.debugElement.query(By.css('.title')).nativeElement.textContent;
      expect(titleText).toBe(title);
    });

    it('should create a sub title', () => {
      const element = fixture.debugElement.query(By.css('.subTitle'));
      expect(element).toBeTruthy();
    });

    it('should contain correct sub title', () => {
      const titleText = fixture.debugElement.query(By.css('.subTitle')).nativeElement.textContent;
      expect(titleText).toBe(subTitle);
    });
  });

  describe('icon tests', () => {
    it('should create icon when icon is defined', async ()=> {
      fixture.componentRef.setInput('iconName', iconName);
      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css('mat-icon'));
      expect(element).toBeTruthy();
    });

    it('should NOT create icon when iconName is undefined', async() => {
      fixture.componentRef.setInput('iconName', undefined);
      fixture.detectChanges();
      await fixture.whenStable();

      const element = fixture.debugElement.query(By.css('mat-icon'));
      expect(element).toBeFalsy();
    });

    it('should contain the correct iconName', async () => {
      fixture.componentRef.setInput('iconName', iconName);
      fixture.detectChanges();
      await fixture.whenStable();

      const iconText = fixture.debugElement.query(By.css('mat-icon')).nativeElement.textContent;
      expect(iconText).toBe(iconName);
    });
  });
});
