export class CreateCertificate {
    validFrom?: Date | string;
    validTo?: Date | string;
    issuerName: string;     //email
    subjectName: string;    //email
    certificateType : string;	
    issuerSerialNumber: string;
    certificateUsage: string;
}
