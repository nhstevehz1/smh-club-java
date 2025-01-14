import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListMembersComponent } from './list-members.component';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {MembersService} from "../services/members.service";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ListMembersComponent', () => {
  let component: ListMembersComponent;
  let fixture: ComponentFixture<ListMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListMembersComponent],
      providers: [
          MembersService,
          provideAnimations(),
          provideHttpClient(),
          provideHttpClientTesting()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
