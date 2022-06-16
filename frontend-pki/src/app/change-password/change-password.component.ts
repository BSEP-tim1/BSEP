import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) { }

  oldPassword = new FormControl('', [Validators.required]);
  getErrorMessage() {
    if (this.oldPassword.hasError('required')) {
      return 'You must enter a value';
    }
    return;
  }
  newPassword = new FormControl('', [Validators.required]);
  getNewPasswordErrorMessage() {
    if (this.newPassword.hasError('required')) {
      return 'You must enter a value';
    }
    return;
  }
  reenteredPassword = new FormControl('', [Validators.required]);
  getReenteredPasswordErrorMessage() {
    if (this.reenteredPassword.hasError('required')) {
      return 'You must enter a value';
    }
    return;
  }

  ngOnInit(): void {
  }

  login() {
    if(this.changePasswordCriteria()) {
      return;
    }
    let body = {
      "oldPassword": this.oldPassword.value,
      "newPassword": this.newPassword.value,
      "reenteredPassword": this.reenteredPassword.value
    }
    this.userService.changePassword(body)
    .subscribe(data => {
      alert('Password is changed')
      this.router.navigate(['login'])
    }, err => {
      console.log(err)
      if(typeof(err.error) === 'string') {
        alert(err.error)
      } else {
        alert('Something wrong, try again.')
      }
    })
  }

  changePasswordCriteria() {
    if(this.reenteredPassword.value == '' || this.newPassword.value == '' || this.oldPassword.value == '') {
      alert('All fields are required')
      return true;
    }
    if(!this.isValid(this.newPassword.value)) {
      return true;
    }
    if(this.reenteredPassword.value != this.newPassword.value) {
      alert("Passwords do not match")
      return true;
    }
    return false;
  }

  isValid(password: string) {
    let pattern = new RegExp('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[-+_!@#$%^&*.,?:;<>=`~\\]\x22\x27\(\)\{\}\|\/\[\\\\?]).{8,}$')
    if(!pattern.test(password)){
      let message = "Password must contain minimum 8 characters,"+
      " at least one uppercase letter, one lowercase letter, one number and one special character.";
      alert(message);
      return false;
    }
    return true;
  }

}
