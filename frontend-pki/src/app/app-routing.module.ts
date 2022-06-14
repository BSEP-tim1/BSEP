import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TestComponent } from './test/test.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { CertificateComponent } from './certificate/certificate.component';
import { NewAdminComponent } from './new-admin/new-admin.component';
import { NewCertificateComponent } from './new-certificate/new-certificate.component';
import { NewCertificateAdminComponent } from './new-certificate-admin/new-certificate-admin.component';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

const routes: Routes = [
  {path: "", redirectTo: '/login', pathMatch: 'full'},
  {path: "login", component: LoginComponent},
  {path: "registration", component: RegistrationComponent},
  {path: "admin-home", component: AdminHomeComponent},
  {path: "user-home", component: UserHomeComponent},
  {path: "certificate-review/:id", component: CertificateComponent},
  {path: "new-admin", component: NewAdminComponent},
  {path: "new-certificate", component: NewCertificateComponent},
  {path: "new-certificate-admin", component: NewCertificateAdminComponent},
  {path: "activate-account/:token", component: AccountActivationComponent},
  {path: "change-password", component: ChangePasswordComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
