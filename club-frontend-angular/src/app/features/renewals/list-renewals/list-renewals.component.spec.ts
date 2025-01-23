import {ListRenewalsComponent} from './list-renewals.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {RenewalService} from "../services/renewal.service";
import {provideNoopAnimations} from "@angular/platform-browser/animations";
import {generateRenewalPageData} from "../test/renewal-test";
import {asyncData} from "../../../shared/test-helpers/test-helpers";
import {PageRequest} from "../../../shared/models/page-request";
import {throwError} from "rxjs";

describe('ListRenewalsComponent', () => {
  let fixture: ComponentFixture<ListRenewalsComponent>;
  let component: ListRenewalsComponent;
  let renewalServiceMock: jasmine.SpyObj<RenewalService>;

  beforeEach(async () => {
    renewalServiceMock = jasmine.createSpyObj('RenewalService', ['getRenewals']);

    await TestBed.configureTestingModule({
      imports: [ListRenewalsComponent],
      providers: [
          {provide: RenewalService, useValue: {}},
          provideHttpClient(),
          provideHttpClientTesting(),
          provideNoopAnimations()
      ],
    }).overrideComponent(ListRenewalsComponent,
        {set: {providers: [{provide: RenewalService, useValue: renewalServiceMock}]}}
    ).compileComponents();
    fixture = TestBed.createComponent(ListRenewalsComponent);
    component = fixture.componentInstance;
  });

  describe('test component', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should create column list', () => {
       fixture.detectChanges();
       expect(component.columns.length).toEqual(4);
    });
  });

  describe('test service interactions on init', async () => {
    it('should call RenewalService.getRenewals() on init', async () => {
       const data = generateRenewalPageData(0, 5, 100);
       renewalServiceMock.getRenewals.and.returnValue(asyncData(data));

       fixture.detectChanges();
       await fixture.whenStable();

       const request = PageRequest.of(0, 5)
       expect(renewalServiceMock.getRenewals).toHaveBeenCalledWith(request);
    });

    it('length should be set on init', async () => {
      const data = generateRenewalPageData(0, 5, 100);
      renewalServiceMock.getRenewals.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.resultsLength).toEqual(data.page.totalElements);
    });

    it('datasource.data should be set on init', async () => {
      const data = generateRenewalPageData(0, 5, 2);
      renewalServiceMock.getRenewals.and.returnValue(asyncData(data));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource.data).toBe(data._content);
    });

    it('datasource.data should be empty when an error occurs while calling getAddresses', async () => {
      renewalServiceMock.getRenewals.and.returnValue(throwError(() => 'error'));

      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.datasource.data).toEqual([]);
    });
  });
});
