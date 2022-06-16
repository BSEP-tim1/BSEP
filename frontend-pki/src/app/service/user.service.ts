import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private _http: HttpClient) { }
  private readonly userPath = 'http://localhost:9000/api/users';

  findAll() {
    return this._http.get<User[]>(`${this.userPath}/findAll`)  
  }

  findAllClients() {
    return this._http.get<User[]>(`${this.userPath}/findAllClients`)  
  }

  findById(id: number) {
    return this._http.get<User>(`${this.userPath}/getById/` + id)    
  }

  findByEmail(email: string) {
    return this._http.get<User>(`${this.userPath}/getByEmail/` + email)    
  }

  changePassword(body: any){
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    });
    console.log(body)
    return this._http.post(`${this.userPath}/changePassword`, JSON.stringify(body), {'headers': headers})
  }
}
