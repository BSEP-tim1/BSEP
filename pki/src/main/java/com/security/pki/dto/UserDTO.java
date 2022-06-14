package com.security.pki.dto;

import java.util.List;

public class UserDTO {
	public Integer id;

	public String email;
	
	public String password;
	
	public String userType;

	public List<CertificateDTO> certificates;
}
