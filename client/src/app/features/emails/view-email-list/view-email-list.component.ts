import {Component, OnInit, input, signal} from '@angular/core';
import {BaseViewList} from '@app/shared/components/base-view-list/base-view-list';
import {Email, EmailType} from '@app/features/emails/models';
import {EmailEditorComponent} from '@app/features/emails/email-editor/email-editor.component';
import {AuthService} from '@app/core/auth/services/auth.service';
import {EmailService} from '@app/features/emails/services/email.service';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';
import {ViewEmailComponent} from '@app/features/emails/view-email/view-email.component';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-view-email-list',
  imports: [
    ViewModelListComponent,
    ViewEmailComponent,
    TranslatePipe
  ],
  providers: [
    AuthService,
    EmailService,
    EmailEditDialogService
  ],
  templateUrl: './view-email-list.component.html',
  styleUrl: './view-email-list.component.scss'
})
export class ViewEmailListComponent extends BaseViewList<Email, EmailEditorComponent> implements OnInit {
  memberId = input.required<number>();
  hasWritePrivileges = signal(false);

  private emailSvc: EmailService;

  constructor(private auth: AuthService,
              apiSvc: EmailService,
              dialogSvc: EmailEditDialogService){
    super(apiSvc, dialogSvc);
    this.emailSvc = apiSvc;
  }

  ngOnInit(): void {
    this.hasWritePrivileges.update(() => this.auth.hasPermission(PermissionType.write));

    this.emailSvc.getAllByMember(this.memberId()).subscribe({
      next: list => this.items.update(() => list),
      error: err => console.debug(err) // TODO: better error handling
    })
  }

  onAddItem(): void {
    const title = 'editDialog.email.create';
    const context = this.generateEmptyEmail();
    this.processAction(title, context, EditAction.Create);
  }

  onEditItem(item: Email): void {
    const title = 'editDialog.email.edit';
    this.processAction(title, item, EditAction.Edit);
  }

  onDeleteItem(item: Email): void {
    const title = 'editDialog.email.delete';
    this.processAction(title, item, EditAction.Delete);
  }

  private generateEmptyEmail(): Email {
    return {
      id: 0,
      member_id: this.memberId(),
      email: '',
      email_type: EmailType.Other
    }
  }
}
