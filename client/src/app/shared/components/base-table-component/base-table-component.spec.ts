import {BaseTestTableComponent} from "./base-test-table-component";
import {FullName} from "../../models/full-name";
import {AuthService} from '../../../core/auth/services/auth.service';
import SpyObj = jasmine.SpyObj;
import {PermissionType} from '../../../core/auth/models/permission-type';

export interface TestType {
  test: string;
}

describe('BaseTableComponent', () => {
  let component: BaseTestTableComponent<TestType>;
  let authServiceMock: SpyObj<AuthService>;

  beforeEach(() => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['hasPermission']);
    //component = new BaseTestTableComponent<TestType>(authServiceMock);
  });

  it('when getPageRequest, should return PageRequest', () => {
     const pageIndex = 0;
     const pageSize = 10;
     const sort = 'field';
     const direction = 'asc';

     const pr = component.getPageRequestExternal(pageIndex, pageSize, sort, direction);
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

  it('should call AuthService.hasPermission', () => {
    const spy = authServiceMock.hasPermission.and.stub();

    expect(spy).toHaveBeenCalledOnceWith(PermissionType.write);
  });
});
