import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  isAdmin(){
    if(localStorage.getItem('role') == "ROLE_ADMIN"){
      return true;
    } else {
      return false;
    }
  }

  isClient(){
    if(localStorage.getItem('role') == "ROLE_USER"){
      return true;
    } else {
      return false;
    }
    
  } 

  logout(){
    localStorage.clear();
    this.router.navigate(['login']);
  }

  changePassword() {
    this.router.navigate(['change-password'])
  }

  addAdmin() {
    this.router.navigate(['/new-admin'])
  }

}
