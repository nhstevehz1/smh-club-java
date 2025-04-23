import {Observable} from 'rxjs';

export interface FilterByMemberService<T> {
  getAllByMember(memberId: number): Observable<T[]>;
}
