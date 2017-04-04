import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import 'rxjs/add/observable/interval';

import { AuthService } from '../auth.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent implements OnInit {
  title = 'Login';
  mdUserName: string;
  mdPassword: string;

  ngOnInit() {
  }

  constructor(private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService) {
  }


  onSubmit() {
    this.authService
      .login(this.mdUserName, this.mdPassword)
      .mergeMap(jwt => this.route.queryParams)
      .map(qp => qp['redirectTo'])
      .subscribe(redirectTo => {
        if (this.authService.isLoggedIn) {
          let url = redirectTo ? [redirectTo] : ['/'];
          this.router.navigate(url);
        }
      });


  }


}