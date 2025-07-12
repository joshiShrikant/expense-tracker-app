
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private userDetailsSubject = new BehaviorSubject<any>(null);
  userDetails$ = this.userDetailsSubject.asObservable();

  setUserDetails(details: any) {
    this.userDetailsSubject.next(details);
  }

  getUserDetails() {
    return this.userDetailsSubject.value;
  }
}
