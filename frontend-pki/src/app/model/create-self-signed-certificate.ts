import { CertificateData } from "./certificateData.model";

export class CreateSelfSignedCertificate {
    validFrom?: Date | string;
    validTo?: Date | string;
    issuerName: string;     //email
    subjectName: string;    //email
    certificateDataDTO : CertificateData = new CertificateData();
    certificateType : string;	
    certificateUsage: string;
}
