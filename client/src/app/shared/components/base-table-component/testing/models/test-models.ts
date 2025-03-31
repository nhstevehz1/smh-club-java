import {Updatable} from '@app/shared/models/updatable';

export interface TestCreate {
  test: string,
}

export interface TestModel extends TestCreate, Updatable {}
