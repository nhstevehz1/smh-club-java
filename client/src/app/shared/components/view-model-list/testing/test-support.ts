export interface ViewListModel {
  id: number;
  modelField: string;
}

export class ViewModelListTest {
  static generateModel(prefix: number): ViewListModel {
    return {
      id: prefix,
      modelField: prefix + 'modelField'
    }
  }

  static generateModelList(size: number): ViewListModel[] {
    const list: ViewListModel[] = [];
    for(let ii = 0; ii < size; ii++) {
      list.push(this.generateModel(ii));
    }
    return list;
  }
}
