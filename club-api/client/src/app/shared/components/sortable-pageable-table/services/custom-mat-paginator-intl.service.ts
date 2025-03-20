import {Injectable} from '@angular/core';
import {MatPaginatorIntl} from "@angular/material/paginator";
import {TranslateService} from "@ngx-translate/core";

@Injectable()
export class CustomMatPaginatorIntlService extends MatPaginatorIntl{

  constructor(private translateService: TranslateService) {
    super();
    translateService.onLangChange.subscribe(() => this.getTranslations());
    this.getTranslations();
  }

  private getTranslations(): void {
    this.translateService.get([
        'table.paginator.firstPage',
        'table.paginator.itemsPerPage',
        'table.paginator.lastPage',
        'table.paginator.nextPage',
        'table.paginator.previousPage'
    ]).subscribe(translations => {
      this.firstPageLabel = translations['table.paginator.firstPage'];
      this.itemsPerPageLabel = translations['table.paginator.itemsPerPage'];
      this.lastPageLabel = translations['table.paginator.lastPage'];
      this.nextPageLabel = translations['table.paginator.nextPage'];
      this.previousPageLabel = translations['table.paginator.previousPage'];

      this.changes.next();
    });
  }

}
