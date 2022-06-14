import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatDividerModule} from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule} from '@angular/material/toolbar'
import { MatMenuModule} from '@angular/material/menu'
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { FormsModule, ReactiveFormsModule, NgForm} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TestComponent } from './test/test.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { CertificateComponent } from './certificate/certificate.component';
import { NewAdminComponent } from './new-admin/new-admin.component';
import { HeaderComponent } from './header/header.component';
import { NewCertificateComponent } from './new-certificate/new-certificate.component';
import { NewCertificateAdminComponent } from './new-certificate-admin/new-certificate-admin.component';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { TokenInterceptor } from './interceptor/token-interceptor';
import { ChangePasswordComponent } from './change-password/change-password.component';

@NgModule({
  declarations: [
    AppComponent,
    TestComponent,
    LoginComponent,
    RegistrationComponent,
    AdminHomeComponent,
    UserHomeComponent,
    CertificateComponent,
    NewAdminComponent,
    HeaderComponent,
    NewCertificateComponent,
    NewCertificateAdminComponent,
    AccountActivationComponent,
    ChangePasswordComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatDividerModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatMenuModule,
    FormsModule,
    MatMenuModule, 
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
      }, 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
