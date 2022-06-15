export class CreateSelfSignedCertificate {
    validFrom?: Date | string;
    validTo?: Date | string;
    issuerName: string;     //email
    subjectName: string;    //email
    certificateType : string;	
    certificateUsage: string;
}
