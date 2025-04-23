import {InjectionToken, Type} from '@angular/core';
import {CrudService} from '@app/shared/services/api-service/crud-service';
import {PagedDataService} from '@app/shared/services/api-service/paged-data-service';
import {EditDialogService} from '@app/shared/services/dialog-edit-service/edit-dialog-service';

export const APP_CRUD_SERVICE = new InjectionToken<CrudService<never>>('app-crud-service-token');
export const APP_PAGED_DATA_SERVICE = new InjectionToken<PagedDataService<never>>('app-paged-data-service-token');
export const APP_DIALOG_SERVICE = new InjectionToken<EditDialogService<never, never>>('app-dialog-service-token');
