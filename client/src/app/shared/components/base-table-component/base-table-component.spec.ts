import {FullName} from "../../models";
import {AuthService} from '../../../core/auth/services/auth.service';
import SpyObj = jasmine.SpyObj;
import {MockTableComponent} from './test-support/mock-table-component';
import {ComponentFixture, TestBed} from '@angular/core/testing';

describe('BaseTableComponent', () => {
  let fixture: ComponentFixture<MockTableComponent>;
  let component: MockTableComponent;
  let authSvcMock: SpyObj<AuthService>;

  beforeEach(async () => {
    authSvcMock = jasmine.createSpyObj('AuthService', ['hasPermission']);

    await TestBed.configureTestingModule({
      imports: [MockTableComponent],
      providers: [
        {provide: AuthService, useValue: {}}
      ]
    }).overrideProvider(AuthService, {useValue: authSvcMock})
      .compileComponents();

    authSvcMock.hasPermission.and.returnValue(true);

    fixture = TestBed.createComponent(MockTableComponent);
    component = fixture.componentInstance;
  });

  it('when getPageRequest, should return PageRequest', () => {
     const pageIndex = 0;
     const pageSize = 10;
     const sort = 'field';
     const direction = 'asc';

     const pr = component.processPageRequestExternal(pageIndex, pageSize, sort, direction);
     expect(pr).toBeDefined();
     expect(pr.page).toBe(pageIndex);
     expect(pr.size).toBe(pageSize);
     expect(pr.sorts.length).toBe(1);
     expect(pr.sorts[0]).toBeDefined();
     expect(pr.sorts[0].sort).toBe(sort);
     expect(pr.sorts[0].direction).toBe(direction);
  });

  it('when FullName fully populated, should return string', () => {
    const fullName: FullName = {
      first_name: 'First',
      middle_name: 'Middle',
      last_name: 'Last',
      suffix: 'Jr.'
    };

    const result = component.getFullNameExternal(fullName);

    expect(result).toBeDefined();
    expect(result).toBe('Last Jr., First Middle');
  });

  it('hasWriteRole should be true', () => {
    expect(component.hasWriteRole()).toBeTrue();
  });
});
