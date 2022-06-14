import { Component, OnInit } from '@angular/core'; 
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements OnInit {

  rootCert : Certificate[] = []
  caCert : Certificate[] = []
  endEntityCert : Certificate[] = []
 
  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void { 

    let role = localStorage.getItem('role');
    if (role == "ROLE_USER"){
      this.router.navigate(['/user-home'])
      return;
    } 
    else if (role != "ROLE_USER" && role!= "ROLE_ADMIN"){
      this.router.navigate(['/login'])
      return;
    }

    this.http.get<Certificate[]>('http://localhost:9000/api/certificate')
    .subscribe(data => {
      var allCertificates : Certificate[] = data   
      for(var c of allCertificates){
        if(c.certificateType === "SELF_SIGNED"){
          this.rootCert.push(c)
        }else if(c.certificateType === "INTERMEDIATE"){
          this.caCert.push(c);
        }else{
          this.endEntityCert.push(c)
        }
      }
    });

  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }
  addAdmin() {
    this.router.navigate(['/new-admin'])
  }

  changePassword() {
    this.router.navigate(['change-password'])
  }

}
