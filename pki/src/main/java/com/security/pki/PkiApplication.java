package com.security.pki;


import com.security.pki.model.UserType;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;


@SpringBootApplication
public class PkiApplication implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CertificateRepository certificateRepository;

	@Autowired
	private UserTypeRepository userTypeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args)  {
		SpringApplication.run(PkiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		UserType userRole = new UserType();
		userRole.setName("ROLE_USER");
		userTypeRepository.save(userRole);

		UserType adminRole = new UserType();
		adminRole.setName("ROLE_ADMIN");
		userTypeRepository.save(adminRole);

		User admin = new User(1, "admin@gmail.com", passwordEncoder.encode("admin"), adminRole, null, true, Timestamp.from(Instant.now()));
		userRepository.save(admin);

		User user = new User(2, "user@gmail.com", passwordEncoder.encode("user"), userRole, null, true, Timestamp.from(Instant.now()));
		userRepository.save(user);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2022);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);


//		SubjectData subjectData = new SubjectData();
//		subjectData.setCommonName("Ceritificate 1");
//		subjectDataRepository.save(subjectData);
//		SubjectData subjectData2 = new SubjectData();
//		subjectData2.setCommonName("Ceritificate 2");
//		subjectDataRepository.save(subjectData2);
//
//		Certificate certificate = new Certificate(1, false, cal.getTime(), cal.getTime(), admin);
//		certificate.setSubjectData(subjectData);
//		certificate.setCertificateType(CertificateType.SELF_SIGNED);
//		certificateRepository.save(certificate);
//
//		Certificate certificate2 = new Certificate(2, false, cal.getTime(), cal.getTime(), user);
//		certificate2.setSubjectData(subjectData2);
//		certificate2.setCertificateType(CertificateType.INTERMEDIATE);
//		certificateRepository.save(certificate2);
	}

}
