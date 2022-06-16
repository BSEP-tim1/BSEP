import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.scss']
})
export class PasswordRecoveryComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  token: string;

  ngOnInit(): void {
    var url = window.location.href;
    this.token  = url.split("/")[4]
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

  login() {
    if(this.changePasswordCriteria()) { return; }
    var newPassword = this.newPassword.value
    
    this.authService.activatePassword(this.token, newPassword).subscribe((token: any) =>{
      alert("Successfully changed password")
      this.router.navigate(['login'])
    }, err => {
      console.log(err)
      alert("Change password failed")
      this.router.navigate(['login'])
    })
  }

  changePasswordCriteria() {
    if(this.reenteredPassword.value == '' || this.newPassword.value == '') {
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
