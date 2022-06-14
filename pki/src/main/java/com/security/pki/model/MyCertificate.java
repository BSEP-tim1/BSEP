package com.security.pki.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.security.pki.enums.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity(name = "certificates")
public class MyCertificate {

	//TODO: svuda staviti u polja nullable = false

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private boolean revoked; 
	
	@Column(name = "validfrom", nullable = false)
	private Date validFrom;

    @Column(name = "validto", nullable = false)
    private Date validTo;
    
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
	@JsonManagedReference
	private User user;

	@Column(name = "certificateType", nullable = false)
	private CertificateType certificateType;

	@Column(name = "serialNumber", nullable = false)
	private String serialNumber;	// potrebno kod OCSP protokola za proveru povucenosti sertifikata

	@Column(name = "certificateUsage", nullable = false)
	private String certificateUsage; 	// namena sertifikata

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "certificateData_id", referencedColumnName = "id", nullable = false)
	private CertificateData certificateData;

}
