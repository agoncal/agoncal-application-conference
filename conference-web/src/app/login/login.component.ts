import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthService } from '../auth.service';

@Component({
  selector: 'conf-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  // title = 'Login';
  // mdUserName: string;
  // mdPassword: string;
  //
  // constructor(private router: Router,
  //   private route: ActivatedRoute,
  //   private authService: AuthService) {
  // }

  ngOnInit() {
  }

  // onSubmit() {
  //   this.authService
  //     .login(this.mdUserName, this.mdPassword)
  //     .mergeMap(jwt => this.route.queryParams)
  //     .map(qp => qp['redirectTo'])
  //     .subscribe(redirectTo => {
  //       if (this.authService.isLoggedIn) {
  //         const url = redirectTo ? [redirectTo] : ['/'];
  //         this.router.navigate(url);
  //       }
  //     });
  // }
}
