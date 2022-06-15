import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Certificate } from '../model/certificate.model';
import { User } from '../model/user';
import { CreateCertificate } from '../model/create-certificate';
import { CreateSelfSignedCertificate } from '../model/create-self-signed-certificate';
import {map} from 'rxjs/operators';   


@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private _http:HttpClient) { }
  private readonly userPath = 'http://localhost:9000/api/certificate';


  findById(id:any) {
    return this._http.get<Certificate>(`${this.userPath}/findById/` + id)  
  }

  findBySerialNumber(id:any) {
    return this._http.get<Certificate>(`${this.userPath}/findBySerialNumber/` + id)  
  }

  findAllRootsAndCA() {
    return this._http.get<Certificate[]>(`${this.userPath}/findAllRootsAndCA`)  
  }

  findUserByCertificateSerialNumber(serialNumber: string) {
    return this._http.get<User>(`${this.userPath}/findUserByCertificateSerialNumber/` + serialNumber)  
  }

  issueCertificate(certificate: CreateCertificate) {
    return this._http.post<CreateCertificate>(`${this.userPath}/create`, certificate)  
  }

  issueSelfSignedCertificate(certificate: CreateSelfSignedCertificate) {
    return this._http.post<CreateSelfSignedCertificate>(`${this.userPath}/createSelfSigned`, certificate)  
  }

  revokeCertificate(serialNumber){
    return this._http.get(`${this.userPath}/revokeCerificate/` + serialNumber)
  }
  
  findCertificateBySerialNumber(serialNumber: string) {
    return this._http.get<Certificate>(`${this.userPath}/findCertificateBySerialNumber/` + serialNumber)  
  }

  findAllRootAndCAByUser(id: number) {
    return this._http.get<Certificate[]>(`${this.userPath}/findAllRootAndCAByUser/` + id)  
  }

  findIssuerEmailBySerialNumber(dto){
    return this._http.post('http://localhost:9000/api/certificate/findIssuerEmailBySerialNumber', dto)
  } 

  downloadCertificate(id: number){
    return this._http.get(`${this.userPath}/downloadCertificate/` + id, { responseType: 'blob'});
  }

  findAllByUserId(id: number) {
    return this._http.get<Certificate[]>(`${this.userPath}/getAllByUser/` + id)  
  }
}
