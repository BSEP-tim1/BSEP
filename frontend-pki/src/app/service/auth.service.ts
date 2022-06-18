import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import jwt_decode from "jwt-decode";
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly activateAccountPath = /*environment.backend_api + */'http://localhost:9000/auth/activateAccount?token=';
  private readonly activatePasswordPath = /*environment.backend_api + */'http://localhost:9000/auth/activateNewPassword?token=';
  private readonly loginPath = /*environment.backend_api + */'http://localhost:9000/auth/login';
  private readonly registerPath = /*environment.backend_api + */'http://localhost:9000/auth/register';

  constructor(private http: HttpClient, private router: Router) { }

  logged: Boolean = false;
  private access_token = null;
  public jwtHelper: JwtHelperService = new JwtHelperService();

  activateAccount(jwt: string): Observable<any> {
    console.log(jwt)
    const headers = new HttpHeaders({
      responseType: 'text'
    })
    return this.http.get<any>(`${this.activateAccountPath}`+ jwt, {'headers': headers})
    .pipe(map((res: any) => { }))
    .pipe(catchError(error => this.checkActivationAccountError(error)));
  }

  activatePassword(jwt: string, pass: string): Observable<any> {
    console.log(jwt)
    const headers = new HttpHeaders({
      responseType: 'text'
    })
    return this.http.post<any>(`${this.activatePasswordPath}`+ jwt, pass, {'headers': headers})
    .pipe(map((res: any) => { }))
    .pipe(catchError(error => this.checkActivationAccountError(error)));
  }

  private checkActivationAccountError(error: any): any {
    if(error.message.text == "Account is activated") {
      alert('You can login')
      return;
    } 
    throw error;
  }

  login(body: {email: string, password: string}) {
    const headers = new HttpHeaders({
       'Accept': 'application/json',
       'Content-Type': 'application/json'
    });
    return this.http.post(this.loginPath, JSON.stringify(body), {'headers': headers})
    .pipe(map((res: any) => {

      console.log(res)
      this.logged = true;
      this.access_token = res['accessToken'];
      if(!this.access_token) {
        alert('Access token is null')
        return;
      }
      let decoded: any = jwt_decode(this.access_token)
      localStorage.setItem("user", decoded.sub)
      localStorage.setItem("role", decoded.role)
      localStorage.setItem("jwt", this.access_token);
      localStorage.setItem("id", decoded.id)
    }));
  }

  isAuthenticated(): boolean {
    if (this.tokenIsPresent() && this.roleIsPresent() && !this.tokenIsExpired()){
      return true;
    }
    return false;
  }

  tokenIsPresent() {
    return localStorage.getItem("jwt") != undefined && localStorage.getItem("jwt") != null;
  }

  roleIsPresent(){
    return localStorage.getItem("role")!= undefined && localStorage.getItem("role") != null;
  }

  tokenIsExpired(){
    if (localStorage.getItem("jwt") != undefined && localStorage.getItem("jwt") != null)  {
      let locStorageToken = localStorage.getItem("jwt")
      if (!locStorageToken){
        return true;
      }
      if(this.jwtHelper.isTokenExpired(locStorageToken)) {
        console.log("Token je istekao")
        this.router.navigate(['login']);
      }
      return this.jwtHelper.isTokenExpired(locStorageToken);
    }
    return true;
  }

  getToken() {
    return this.access_token;
  }


}
