import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { CertificateData } from '../model/certificateData.model';
import { CreateCertificate } from '../model/create-certificate';
import { CreateSelfSignedCertificate } from '../model/create-self-signed-certificate';
import { User } from '../model/user';
import { CertificateService } from '../service/certificate.service';
import { UserService } from '../service/user.service'; 
 
@Component({
  selector: 'app-new-certificate-admin',
  templateUrl: './new-certificate-admin.component.html',
  styleUrls: ['./new-certificate-admin.component.scss']
})
export class NewCertificateAdminComponent implements OnInit {

  constructor(private http: HttpClient, 
     private userService: UserService,
     private certificateService: CertificateService,
     private router : Router) { }

  certificate: CreateCertificate = new CreateCertificate();
  selfSignedCertificate: CreateSelfSignedCertificate = new CreateSelfSignedCertificate();
  users: User[] = [];
  issuerCertificates: Certificate[]; 
  selectedIssuerCertificate: Certificate;
  adminEmail: any;
  adminId: any;
  minDate: Date = new Date()
  maxDate: Date;

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

    this.adminEmail = localStorage.getItem('user')
    this.adminId = localStorage.getItem('id');
 
    this.loadUsers();
    this.loadIssuerCertificates();
  }

  loadUsers(){
    this.userService.findAll().subscribe(
      (users: User[]) => {
        this.users = users;
      })
  }

  loadIssuerCertificates(){
    this.certificateService.findAllRootsAndCA().subscribe(
      (issuerCertificates: Certificate[]) => {
        this.issuerCertificates = issuerCertificates;
      })
  }

  selectedCertificateType(){
    if (this.certificate.certificateType ==="SELF_SIGNED"){
      this.certificate.certificateDataDTO.emailAddress = this.adminEmail
      this.certificate.certificateDataDTO.userId = this.adminId;
      this.certificate.subjectName = this.adminEmail;
      this.certificate.issuerName = this.adminEmail;
    }
  }

  selectSubject(){
    this.userService.findByEmail(this.certificate.subjectName).subscribe(
      (user: User) => {
        this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName
        this.certificate.certificateDataDTO.userId = user.id;
      })

      this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName
  }

  selectIssuer(){
    this.certificate.validTo = undefined;
    this.certificate.validFrom = undefined;

    this.certificateService.findBySerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (issuer: Certificate) => {
        let dateFrom = <string> issuer.validFrom
        let dateFromFinal =  new Date(dateFrom.substring(6) + '-' + dateFrom.substring(3,5) + '-' + dateFrom.substring(0,2))
        this.minDate = new Date()
        if ( this.minDate < dateFromFinal){

          this.minDate = dateFromFinal
        } 
        
        let date = <string> issuer.validTo
        let dateFinal =  date.substring(6) + '-' + date.substring(3,5) + '-' + date.substring(0,2)
        this.maxDate = new Date(dateFinal)  
      })


    this.certificateService.findUserByCertificateSerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (issuer: User) => {
        this.certificate.issuerName = issuer.email;
      })

    this.certificateService.findCertificateBySerialNumber(this.certificate.issuerSerialNumber).subscribe(
      (certificate: Certificate) => {
        this.selectedIssuerCertificate = certificate;
      })   
      
      this.checkIssuerCertificateDuration();  
  }

  checkDates(){
    if(this.certificate.validFrom != undefined && this.certificate.validTo != undefined){
      if(this.certificate.validFrom > this.certificate.validTo){
        this.certificate.validTo = undefined;
        this.certificate.validFrom = undefined;
      }
    }

    this.checkIssuerCertificateDuration();
  }

  checkIssuerCertificateDuration(){
    
    if (this.selectedIssuerCertificate != undefined &&
        this.certificate.validFrom != undefined && this.certificate.validTo != undefined){
     
        let issuerValidFrom = Number(this.selectedIssuerCertificate.validFrom);
        let issuerValidTo = Number(this.selectedIssuerCertificate.validTo);

        let validFrom: number = new Date(this.certificate.validFrom).getTime();
        let validTo: number = new Date(this.certificate.validTo).getTime();
        
        // console.log(validFrom);   
        // console.log(validTo);   

        // console.log(issuerValidFrom);  
        // console.log(issuerValidTo);  

        if ( validFrom < issuerValidFrom || validTo > issuerValidTo){
          this.certificate.validTo = undefined;
          this.certificate.validFrom = undefined;

          console.log('datumi od-kad do-kad je sertifikat validan nisu ispravni')
        }
    }
  }

  createNewCertificate(){

    // TODO: nisu pokrivene sve situacije, npr. ako je neko polje prazan string
    if(this.certificate.certificateType != undefined && this.certificate.certificateUsage != undefined &&
        this.certificate.subjectName != undefined && this.certificate.validFrom != undefined && 
        this.certificate.validTo != undefined && 
        this.certificate.certificateDataDTO.commonName != undefined &&
        this.certificate.certificateDataDTO.countryCode != undefined && this.certificate.certificateDataDTO.emailAddress != undefined &&
        this.certificate.certificateDataDTO.givenName != undefined && this.certificate.certificateDataDTO.organization != undefined &&
        this.certificate.certificateDataDTO.organizationalUnit != undefined && this.certificate.certificateDataDTO.surname != undefined &&
        this.certificate.certificateDataDTO.userId != undefined){

        if(this.certificate.certificateType == "SELF_SIGNED"){
            this.createSelfSignedCertificate();

            this.certificateService.issueSelfSignedCertificate(this.selfSignedCertificate).subscribe(
              (cer: CreateSelfSignedCertificate) => {
                alert("Certificate created successfully!")
                this.certificate = new CreateCertificate()
                this.router.navigate(['admin-home'])}
            )
        } 
        else {
            if(this.certificate.issuerName != undefined && this.certificate.issuerSerialNumber != undefined) {

                this.certificateService.issueCertificate(this.certificate).subscribe(
                  (cer: CreateCertificate) => { 
                    alert("Certificate created successfully!")
                    this.certificate = new CreateCertificate() 
                    this.router.navigate(['admin-home'])
                    }
                )
            }
            else {
              console.log('nije izabran issuer');
            }
        }
    } 
    else {
      alert("All fields must be filled in!")
      console.log('nisu sva polja popunjena');
    }
  }

  createSelfSignedCertificate(){
    this.selfSignedCertificate.certificateDataDTO = this.certificate.certificateDataDTO;
    this.selfSignedCertificate.certificateType = this.certificate.certificateType;
    this.selfSignedCertificate.certificateUsage = this.certificate.certificateUsage;
    this.selfSignedCertificate.issuerName = this.certificate.subjectName;   // jer je self-signed
    this.selfSignedCertificate.subjectName = this.certificate.subjectName;
    this.selfSignedCertificate.validFrom = this.certificate.validFrom;
    this.selfSignedCertificate.validTo = this.certificate.validTo;
  }
}
function moment(dateString: any, arg1: string): Date {
  throw new Error('Function not implemented.');
}

