import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
@Injectable()
export class RoleGuardService implements CanActivate {
  constructor(public auth: AuthService, public router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRole = route.data['expectedRole'];
    const token = localStorage.getItem('jwt');
    let role;
    
    if (token != null){
      let jwtData = token.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)

      role = decodedJwtData.role
      console.log("rola je "+ role)
    }

    if (!this.auth.isAuthenticated() || !expectedRole.includes(role)) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
}