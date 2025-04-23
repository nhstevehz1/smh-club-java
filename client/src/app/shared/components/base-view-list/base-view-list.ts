import {Updatable} from '@app/shared/models/updatable';
import {CrudService} from '@app/shared/services/api-service/crud-service';
import {Editor} from '@app/shared/components/base-editor/editor';
import {EditDialogService} from '@app/shared/services/dialog-edit-service/edit-dialog-service';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';
import {model} from '@angular/core';
import {mergeMap, map} from 'rxjs/operators';
import {of} from 'rxjs';

export abstract class BaseViewList<T extends Updatable, C extends Editor<T>> {

  items = model<T[]>([]);
  error = model<string>();

  protected constructor(private apiSvc: CrudService<T>,
                        private dialogSvc: EditDialogService<T, C>) {}


  protected processAction(title: string, context: T, action: EditAction): void {
    if(action == EditAction.Create) {
      this.processCreate(title, context);
    } else if( action == EditAction.Edit) {
      this.processEdit(title, context);
    } else if (action == EditAction.Delete) {
      this.processDelete(title, context);
    }
  }

  private processCreate(title: string, context: T): void {
    const input = this.dialogSvc.generateDialogInput(title, context, EditAction.Create);

    this.dialogSvc.openDialog(input).pipe(
      mergeMap(result => {
        if(result.action == EditAction.Create) {
          return this.apiSvc.create(result.context);
        } else return of(null);
      })
    ).subscribe({
      next: data => {
        if(data) {
          this.items.update((list) => [data, ...list]);
        }
      },
      error: err => console.debug(err) // TODO: error handling
    })
  }

  private processEdit(title: string, context: T): void {
    const input = this.dialogSvc.generateDialogInput(title, context, EditAction.Edit);

    this.dialogSvc.openDialog(input).pipe(
      mergeMap(result => {
        if(result.action == EditAction.Edit) {
          return this.apiSvc.update(result.context);
        }  else {
          return of(null);
        }
      })
    ).subscribe({
      next: result => {
        if (result) {
          this.updateList(result);
        }
      }, error: err => console.debug(err) // todo error handling
    });
  }

  private processDelete(title: string, context: T): void {
    const input = this.dialogSvc.generateDialogInput(title, context, EditAction.Delete);

    this.dialogSvc.openDialog(input).pipe(
      mergeMap(result => {
        if(result.action == EditAction.Delete) {
          return this.apiSvc.delete(result.context.id).pipe(map(() => result));
        } else {
          return of(result);
        }
      })
    ).subscribe({
      next: result => {
        if(result.action == EditAction.Delete) {
          this.deleteFromList(result.context);
        }
      }, error: err => console.debug(err) // TODO: error handling
    });

  }

  private getItemIndex(id: number): number {
    return this.items().findIndex(item => item.id == id);
  }

  private updateList(item: T): void {
    const index = this.getItemIndex(item.id);
    this.items.update(list => {
      list[index] = item;
      return list;
    });
  }

  private deleteFromList(item: T): void {
    const index = this.getItemIndex(item.id);
    this.items.update(list => {
      list.splice(index, 1);
      return list;
    })
  }
}
