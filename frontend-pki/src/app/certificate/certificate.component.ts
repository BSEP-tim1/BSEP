import { CertificateService } from './../service/certificate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Certificate } from './../model/certificate.model';
import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common'
import { ThisReceiver } from '@angular/compiler';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.scss']
})
export class CertificateComponent implements OnInit {

  certificate : Certificate;
  id:any;
  dto = {
    "serialNumber": "",
    "certType": ""
  }
  userId: any;
  found: boolean;
  expired: boolean = false;
  serialNum : string;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private http : HttpClient,
    private certificateService: CertificateService) { }

  ngOnInit(): void {

    let role = localStorage.getItem('role');
    if (role != "ROLE_USER" && role!= "ROLE_ADMIN"){
      this.router.navigate(['/login'])
      return;
    }

    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.userId = localStorage.getItem('userId');

    if (role == "ROLE_USER") {
      this.certificateService.findAllByUserId(this.userId).subscribe(
        (certificates: Certificate[]) => {

          for(let cer of certificates){
            if (cer.id == this.id){
              this.found = true;
            }
          }
    
          if (!this.found) {
              this.router.navigate(['/user-home'])
              return;
          }
        }
      );
    }  

    this.certificateService.findById(this.id).subscribe((data) => {
      this.certificate = data;
      this.serialNum = data.serialNumber
      console.log(this.certificate)
      this.dto.certType = data.certificateType;
      this.dto.serialNumber = data.serialNumber;
      var dto = {
        'serialNumber': data.serialNumber,
        'certType': data.certificateType
      }

      if(this.certificate.certificateType == "SELF_SIGNED"){
        this.certificate.certificateType = "SELF SIGNED"
      }  
      else if(this.certificate.certificateType == "END_ENTITY"){
        this.certificate.certificateType = "END ENTITY"
      }

      this.certificateService.findBySerialNumber(this.serialNum).subscribe(
        (issuer: Certificate) => {
  
          let date = <string> issuer.validTo
          let dateFinal =  date.substring(6) + '-' + date.substring(3,5) + '-' + date.substring(0,2)
          let maxDate = new Date(dateFinal)  
          this.expired = (maxDate < new Date) 
        }) 

    });
  }
  
  isAdmin(){
    if(localStorage.getItem('role') == "ROLE_ADMIN"){
      return true;
    } else {
      return false;
    }
  }
}
