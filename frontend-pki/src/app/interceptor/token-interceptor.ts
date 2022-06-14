import { Observable } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Injectable, Injector } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpEvent
} from '@angular/common/http';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(public auth: AuthService, private inj: Injector) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (this.auth.tokenIsPresent()) {
       request = request.clone({
         setHeaders: {
           //'Content-Type': 'application/json',
           Authorization: `Bearer ${localStorage.getItem("jwt")}` 
         }
       });

    }
    return next.handle(request);
  }
}