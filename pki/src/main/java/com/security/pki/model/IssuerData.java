package com.security.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import javax.persistence.*;
import java.security.PrivateKey;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IssuerData {
    private X500Name x500name;
    private PrivateKey privateKey;

}
