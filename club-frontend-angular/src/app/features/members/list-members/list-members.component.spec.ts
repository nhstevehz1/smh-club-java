import {ListMembersComponent} from './list-members.component';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {MembersService} from "../services/members.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('ListMembersComponent', () => {
  let fixture: ComponentFixture<ListMembersComponent>;
  let component: ListMembersComponent;
  let service: MembersService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
          ListMembersComponent,
          MembersService,
          provideHttpClient(),
          provideHttpClientTesting()
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(ListMembersComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(MembersService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
