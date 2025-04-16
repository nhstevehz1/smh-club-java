import {FullName} from '@app/shared/models';

export interface ApiModel {
  id: number;
  apiModelField: string;
}

export interface ApiMemberNameModel extends ApiModel{
  fullName: FullName;
}

export class BaseApiTest {
  static generateModel(): ApiModel {
    return {
      id: 0,
      apiModelField: 'test'
    }
  }
}
