export interface ViewModel {
  id: number;
}

export class ViewModelTest {
  static generateModel(id: number): ViewModel {
    return {
      id: id
    }
  }
}
