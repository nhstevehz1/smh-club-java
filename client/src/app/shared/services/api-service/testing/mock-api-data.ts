import {FullName} from '@app/shared/models';

export interface ApiCreateMode {
  apiModelField: string;
}

export interface ApiModel extends ApiCreateMode {
  id: number;
}

export interface ApiFullNameModel extends ApiModel{
  fullName: FullName;
}

export class BaseApiTest {
  static generateCreateModel(): ApiCreateMode {
    return {
      apiModelField: 'test'
    }
  }

  static generateModel(): ApiModel {
    return {
      id: 0,
      apiModelField: 'test'
    }
  }
}
