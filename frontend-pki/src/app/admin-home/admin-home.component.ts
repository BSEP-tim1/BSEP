import { Component, OnInit } from '@angular/core'; 
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { HttpClient } from '@angular/common/http';
import { CertificateService } from '../service/certificate.service';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements OnInit {

  allCert : Certificate[] = []
  displayedColumns: string[] = ['subject', 'validPeriod', 'viewCert', 'download', 'state', "revoke"];
  showCertDetails = false;

  constructor(private router: Router,
     private http: HttpClient,
     private certificateService: CertificateService,
     ) { }

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
      this.allCert = data 
    });

  }

  review(id: any){
    this.router.navigate(['/certificate-review/'+ id]);
  }
  addAdmin() {
    this.router.navigate(['/new-admin'])
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
