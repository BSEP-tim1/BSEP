import { CertificateData } from "./certificateData.model";
import { User } from "./user";

export class Certificate{
    id: number;
    validFrom: Date | string;
    validTo: Date | string;
    certificateType : string;	
	revoked: boolean; 
    serialNumber: string;
    certificateUsage: string;
    certificateData : CertificateData = new CertificateData();
    user: User = new User();
    commonName: string;
    email: string;
    isValid: boolean;

    // constructor(commonName: string, revoked: boolean, validFrom: Date, validTo: Date){
    //     this.commonName = commonName;
    //     this.revoked = revoked;
    //     this.validFrom = validFrom;
    //     this.validTo = validTo;
    // }
}
