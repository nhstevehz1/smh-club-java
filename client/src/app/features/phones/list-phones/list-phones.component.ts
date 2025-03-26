import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {PhoneService} from "../services/phone.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {Phone, PhoneMember} from "../models/phone";
import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {HttpErrorResponse} from '@angular/common/http';
import {AuthService} from '../../../core/auth/services/auth.service';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {PagedData} from '../../../shared/models/paged-data';
import {PhoneEditorComponent} from '../phone-editor/phone-editor.component';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [
    PhoneService,
    AuthService
  ],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends BaseTableComponent<PhoneMember> implements AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<PhoneMember>;

  constructor(private svc: PhoneService,
              auth: AuthService,
              private dialog: MatDialog) {
    super(auth);
    this.columns.set(this.svc.getColumnDefs());
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => this.getCurrentPage())
        ).subscribe({
          // set the data source with the new page
          next: data => this.processPageData(data),
          error: (err: HttpErrorResponse) => this.processRequestError(err)
        });
  }

  onEditClick(event: EditEvent<PhoneMember>) {
    this.openDialog(event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<PhoneMember>) {
    this.openDialog(event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<PhoneMember>> {
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);

    return this.svc.getPhones(pr).pipe(first());
  }

  private openDialog(event: EditEvent<PhoneMember>, action: EditAction): void {
    const dialogData: EditDialogData<Phone> = {
      title: 'Phone Title',
      component: PhoneEditorComponent,
      form: this.svc.generatePhoneForm(),
      context: event.data as Phone,
      action: action
    }

    const dialogRef =
      this.dialog.open<EditDialogComponent<Phone>, EditDialogData<Phone>>(
        EditDialogComponent<Phone>, {data: dialogData});

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Phone>) => {
        if(result.action == EditAction.Edit) {
          this.updatePhone(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deletePhone(result.context.id);
        }
      }
    })
  }

  private updatePhone(phone: Phone): void {
    this.svc.updatePhone(phone).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse)=> this.processRequestError(err)
    })
  }

  private deletePhone(id: number): void{
    this.svc.deletePhone(id).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse)=> this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
