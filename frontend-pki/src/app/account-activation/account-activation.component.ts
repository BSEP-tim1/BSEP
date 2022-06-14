import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-account-activation',
  templateUrl: './account-activation.component.html',
  styleUrls: ['./account-activation.component.scss']
})
export class AccountActivationComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  token: string;
  expired = true;

  ngOnInit(): void {
    var url = window.location.href;
    this.token  = url.split("/")[4]

    this.authService.activateAccount(this.token).subscribe(
      (token: any) => {
        this.expired = false;
    }, err => {
      console.log(err)
      alert("Account verification failed")
      // alert(err);
    })
  }

  login() {
    this.router.navigate(['login'])
  }

}
