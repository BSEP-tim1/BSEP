import { User } from "./user";

export class Certificate{
    id: number;
    validFrom: Date | string;
    validTo: Date | string;
    certificateType : string;	
	revoked: boolean; 
    serialNumber: string;
    certificateUsage: string;
    user: User = new User();
    isValid: boolean;
    issuerName: string;
}
