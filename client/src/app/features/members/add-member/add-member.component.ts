import {Component, signal, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

import {MatDialogTitle} from '@angular/material/dialog';
import {MatDividerModule} from '@angular/material/divider';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {TranslatePipe} from '@ngx-translate/core';

import {MemberEditorComponent} from '@app/features/members/member-editor/member-editor.component';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {Member, MemberCreate} from '@app/features/members/models';
import {MemberService} from '@app/features/members/services/member.service';
import {MemberEditDialogService} from '@app/features/members/services/member-edit-dialog.service';
import {provideLuxonDateAdapter} from '@angular/material-luxon-adapter';


@Component({
  selector: 'app-add-member',
  imports: [
    MemberEditorComponent,
    MatDialogTitle,
    TranslatePipe,
    ReactiveFormsModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
  ],
  providers: [
    MemberService,
    MemberEditDialogService,
    Location,
    provideLuxonDateAdapter()
  ],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent implements OnInit {
  readonly createForm = signal<FormModelGroup<Member> | undefined>(undefined);

  constructor(private apiSvc: MemberService,
              private dialogSvc: MemberEditDialogService,
              private router: Router,
              private location: Location) {}

  ngOnInit(): void {
    this.createForm.set(this.dialogSvc.generateForm());
  }

  onSave(): void {
    this.apiSvc.create(this.createForm()!.value as MemberCreate).subscribe({
      next: member => this.router.navigate(['p/members/view', member.id]).then(),
      error: err => console.debug(err) // TODO display error
    })
  }

  onCancel(): void {
    // go back to last page viewed
    this.location.back();
  }
}
