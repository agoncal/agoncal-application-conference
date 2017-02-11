import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class AuthService {
    isLoggedIn: any;
    jwt: any;

  constructor() { }

    login(login: string, password: string): Observable<any> {
        return undefined;
    }
}
