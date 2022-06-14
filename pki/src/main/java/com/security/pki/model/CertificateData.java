package com.security.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Nisam sig za publicKey
    //    @Column(name = "public_key", nullable = false, length = 2048)
    //    private String publicKey;

    @Column(name = "common_name", nullable = false)
    private String commonName;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "organization")
    private String organization;

    @Column(name = "organizational_unit")
    private String organizationalUnit;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "email_address")
    private String emailAddress;

}
