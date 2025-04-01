import {Component, signal, WritableSignal} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldAppearance, MatFormFieldModule} from '@angular/material/form-field';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {provideLuxonDateAdapter} from '@angular/material-luxon-adapter';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

import {TranslatePipe} from '@ngx-translate/core';

import {OkCancelComponent} from '@app/shared/components/ok-cancel/ok-cancel.component';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

import {Member, MemberCreate} from '@app/features/members/models/member';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberTableService} from '@app/features/members/services/member-table.service';
import {MemberEditDialogService} from '@app/features/members/services/member-edit-dialog.service';


@Component({
  selector: 'app-create-member',
  imports: [
    MatFormFieldModule,
    MatCardModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    ReactiveFormsModule,
    OkCancelComponent,
    TranslatePipe
  ],
  providers: [
    provideLuxonDateAdapter(),
    MemberService,
    MemberTableService
  ],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent {

  createForm: WritableSignal<FormModelGroup<Member>>;

  fieldAppearance
      = signal<MatFormFieldAppearance>('outline');

  errorMessage = signal<string | null>(null);

  submitted = signal(false);

  constructor(private svc: MemberService,
              private dialogSvc: MemberEditDialogService,
              private router: Router) {

    this.createForm = signal(this.dialogSvc.generateForm());
  }

  onSave(): void {
   if (this.createForm().valid) {
     const val = this.createForm().value as Member;
     const create: MemberCreate = {
       member_number: val.member_number,
       first_name: val.first_name,
       middle_name: val.middle_name,
       last_name: val.last_name,
       suffix: val.suffix,
       birth_date: val.birth_date,
       joined_date: val.joined_date,
     }

     this.svc.create(create).subscribe({
       next: data => {
         this.errorMessage.update(() => null)
         this.submitted.update(() => true);
         this.router.navigate(['p/members/view', data.id]).then();
       },
       error: (err: HttpErrorResponse | any) => {
         console.debug(err);
         this.errorMessage.set('An error occurred saving member data.');
       }
     });
   } else {
     this.errorMessage.set('Cannot save member.  Invalid data'); //TODO: translate
     return;
    }
  }

  onCancel(): void {
    this.router.navigate(['p/members']).then();
  }
}
