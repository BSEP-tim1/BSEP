import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { CertificateService } from '../service/certificate.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {

  caCert : Certificate[] = []
  endEntityCert : Certificate[] = []
  email: string = '';
  user: any;

  allCert : Certificate[] = []
  displayedColumns: string[] = ['subject', 'validPeriod', 'viewCert', 'download', 'state', "revoke"];


  constructor(private router: Router, private http: HttpClient, private certificateService: CertificateService) { }


  ngOnInit(): void {

    let role = localStorage.getItem('role');
    if (role == "ROLE_ADMIN"){
      this.router.navigate(['/admin-home'])
      return;
    } 
    else if (role != "ROLE_USER" && role!= "ROLE_ADMIN"){
      this.router.navigate(['/login'])
      return;
    }

    this.email = localStorage.getItem('user') || ""
    this.http.get('http://localhost:9000/api/users/getByEmail/' + this.email)
    .subscribe(data => {
      this.user = data
      this.email = this.user.email
    })

    this.getCertificates();
  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }

  getCertificates(){
    this.http.get<Certificate[]>('http://localhost:9000/api/certificate/getAllByUser/' + localStorage.getItem('id'))
    .subscribe(data => {this.allCert = data });
  }

  downloadCertificate(certificate: Certificate) {
    this.certificateService.downloadCertificate(certificate.id).subscribe(data => { 
      let blob = new Blob([data], { type: 'application/octet-stream' })
      let link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = certificate.serialNumber + ".cer"
      link.click()
      URL.revokeObjectURL(link.href)
      alert('Certificate is downloaded')
    });
  }

  revokeCertificate(serialNumber){
    console.log(serialNumber)
    this.http.get('http://localhost:9000/api/certificate/revokeCerificate/' + serialNumber)
    .subscribe(data => { 
      alert('Certificate is revoked')
    });
    window.location.reload();
  }

}
