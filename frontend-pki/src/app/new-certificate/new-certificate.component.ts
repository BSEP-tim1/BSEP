import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../model/certificate.model';
import { CreateCertificate } from '../model/create-certificate'; 
import { User } from '../model/user';
import { CertificateService } from '../service/certificate.service';
import { UserService } from '../service/user.service'; 


@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.scss']
})
export class NewCertificateComponent implements OnInit {

  constructor(private http: HttpClient, 
    private userService: UserService, 
    private certificateService: CertificateService,
    private router : Router) { }

  certificate: CreateCertificate = new CreateCertificate();
  users: User[] = [];
  issuerCertificates: Certificate[]; 
  selectedIssuerCertificate: Certificate;
  user: any = new User();
  userId: number; 
  loadedUser: boolean = false;
  minDate: Date = new Date();
  maxDate: Date;

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

    let idLocalStorage = localStorage.getItem('id')
    this.http.get('http://localhost:9000/api/users/getById/' + idLocalStorage)
    .subscribe(data => {
      this.user = data
      this.userId = this.user.id;
      this.loadedUser = true;

      this.loadUsers();
      this.loadIssuerCertificates();
    })
  }

  loadUsers(){
    this.userService.findAllClients().subscribe(
      (users: User[]) => {
        this.users = users;
      })
  }

  loadIssuerCertificates(){    
    this.certificateService.findAllRootAndCAByUser(this.userId).subscribe(    
      (issuerCertificates: Certificate[]) => {
        this.issuerCertificates = issuerCertificates;
      })
  }

  // selectSubject(){
  //   this.userService.findByEmail(this.certificate.subjectName).subscribe(
  //     (user: User) => {
  //       this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName
  //       this.certificate.certificateDataDTO.userId = user.id;
  //     })

  //     this.certificate.certificateDataDTO.emailAddress = this.certificate.subjectName
  // }

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
        let dateToFinal =  date.substring(6) + '-' + date.substring(3,5) + '-' + date.substring(0,2)
        this.maxDate = new Date(dateToFinal)  
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
        this.certificate.issuerName != undefined && this.certificate.issuerSerialNumber != undefined &&
        this.certificate.subjectName != undefined && this.certificate.validFrom != undefined && 
        this.certificate.validTo != undefined ){

      this.certificateService.issueCertificate(this.certificate).subscribe(
        (cer: CreateCertificate) => { 
          alert("Certificate created successfully!")
          this.certificate = new CreateCertificate()
          this.router.navigate(['user-home'])
        }
      )      
    } 
    else {
      alert("All fields must be filled in!")
      console.log('nisu sva polja popunjena');
    }
  }
}
