import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {throwError} from 'rxjs';
import {TranslateModule} from '@ngx-translate/core';

import {asyncData} from '@app/shared/testing/test-helpers';
import {generateRenewalPageData} from '@app/features/renewals/testing/test-support';

import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {ListRenewalsComponent} from './/list-renewals.component';
import {PageRequest} from '@app/shared/services/api-service/models';

describe('ListRenewalsComponent', () => {
  let fixture: ComponentFixture<ListRenewalsComponent>;
  let component: ListRenewalsComponent;
  let renewalSvcMock: jasmine.SpyObj<RenewalService>;

  beforeEach(async () => {
    renewalSvcMock = jasmine.createSpyObj('RenewalService', ['getPagedData']);

    await TestBed.configureTestingModule({
      imports: [
          ListRenewalsComponent,
          TranslateModule.forRoot({})
      ],
      providers: [
          {provide: RenewalService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideProvider(RenewalService, {useValue: renewalSvcMock})
      .compileComponents();
    fixture = TestBed.createComponent(ListRenewalsComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should create column list', () => {
       fixture.detectChanges();
       expect(component.columns.length).toEqual(3);
    });
  });

  describe('test service interactions on init', async () => {
    it('should call RenewalService.getRenewals() on init', async () => {
       const data = generateRenewalPageData(0, 5, 100);
       renewalSvcMock.getPagedData.and.returnValue(asyncData(data));

       fixture.detectChanges();
       await fixture.whenStable();

       const request = PageRequest.of(0, 5)
       expect(renewalSvcMock.getPagedData).toHaveBeenCalledWith(request);
    });

    it('length should be set on init', async () => {
      const data = generateRenewalPageData(0, 5, 100);
      renewalSvcMock.getPagedData.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.resultsLength).toEqual(data.page.totalElements);
    });

    it('datasource.data should be set on init', async () => {
      const data = generateRenewalPageData(0, 5, 2);
      renewalSvcMock.getPagedData.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource().data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
      renewalSvcMock.getPagedData.and.returnValue(throwError(() => 'error'));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource().data).toEqual([]);
    });
  });
});
