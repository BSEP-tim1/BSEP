import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
  }

  email = new FormControl('', [Validators.required, Validators.email]);
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  send() {
    if(!this.email.hasError('required') && !this.email.hasError('email')){
      const headers = new HttpHeaders({
        responseType: 'text'
      })
      this.http.get('http://localhost:9000/auth/recoveryEmail?email='+ this.email.value, {'headers': headers})
      .subscribe(data => {
        alert("We send you recovery password link on email")
        this.router.navigate(['login'])
      }, error =>{
        console.log(error)
        alert("We can not find that email")
      })
    }else{this.email.setErrors({'required': true}); this.email.markAsTouched({onlySelf:true}); this.email.touched}
  }
}
