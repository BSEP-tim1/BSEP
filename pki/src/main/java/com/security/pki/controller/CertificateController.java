package com.security.pki.controller;

import com.security.pki.dto.*;
import com.security.pki.mapper.CertificateMapper;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.User;
import com.security.pki.service.CertificateService;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping(value="")
    public List<AllCertificatesViewDTO> getAll() {
        return this.certificateService.findAll();
    }

    @GetMapping(value="/getAllByUser/{id}")
    public List<AllCertificatesViewDTO> getAllByUser(@PathVariable Integer id) {
        return this.certificateService.findAllByUser(id);
    }

    @GetMapping(value = "/downloadCertificate/{id}")
    @PreAuthorize("hasAuthority('downloadCertificate')")
    public ResponseEntity<?> downloadCertificate(@PathVariable Integer id) throws KeyStoreException, CertificateEncodingException, IOException {
        MyCertificate cert = certificateService.findById(id);
        if(cert.isRevoked()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Certificate certificate = certificateService.findCertificateBySerialNumber(cert.getSerialNumber(), cert.getCertificateType().toString());
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =" + cert.getSerialNumber() +".cer");

        try {
            var resource = certificate.getEncoded();
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (CertificateEncodingException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/findById/{id}")
    public CertificateReviewDTO findById(@PathVariable Integer id) {
        return this.certificateService.findDtoById(id);
    }

    @PreAuthorize("hasAuthority('issueCertificate')")
    @PostMapping(value="/create")
    public ResponseEntity<?> issueCertificate(@RequestBody CreateCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueCertificate(dto);
        if(certificate == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('createSelfSigned')")
    @PostMapping(value="/createSelfSigned")
    public ResponseEntity<?> createSelfSigned(@RequestBody CreateSelfSignedCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueSelfSignedCertificate(dto);
        if(certificate == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('findAllRootsAndCA')")
    @GetMapping(value="/findAllRootsAndCA")
    public List<MyCertificate> findAllRootsAndCA() {
        return this.certificateService.findAllRootsAndCA();
    }

    @GetMapping(value="/findUserByCertificateSerialNumber/{serialNumber}")
    public User findUserByCertificateSerialNumber(@PathVariable String serialNumber) {
        MyCertificate certificate = this.certificateService.findMyCertificateBySerialNumber(serialNumber);
        return certificate.getUser();
    }

    @PreAuthorize("hasAuthority('revokeCerificate')")
    @GetMapping(value="/revokeCerificate/{serialNumber}")
    public void revokeCerificate(@PathVariable String serialNumber){
        certificateService.revokeCerificate(serialNumber);
    }
  
    @GetMapping(value="/findCertificateBySerialNumber/{serialNumber}")
    public MyCertificate findCertificateBySerialNumber(@PathVariable String serialNumber) {
        return this.certificateService.findMyCertificateBySerialNumber(serialNumber);
    }

    @GetMapping(value="/findAllRootAndCAByUser/{id}")
    public List<MyCertificate> findAllRootAndCAByUser(@PathVariable Integer id) {
        List<MyCertificate> certificates = new ArrayList<>();

        for (MyCertificate c: this.certificateService.findAllRootsAndCA()) {
            if(c.getUser().getId() == id){
                certificates.add(c);
            }
        }

        return certificates;
    }

    @PostMapping(value="/findIssuerEmailBySerialNumber")
    public ResponseEntity<?> findIssuerEmailBySerialNumber(@RequestBody RevokeCertificateDTO dto){
        if(certificateService.findIssuerEmailBySerialNumber(dto) == null){ return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<>(certificateService.findIssuerEmailBySerialNumber(dto), HttpStatus.OK);
    }

    @GetMapping(value="/findBySerialNumber/{serialNumber}")
    public AllCertificatesViewDTO findBySerialNumber(@PathVariable String serialNumber) {
        CertificateMapper certificateMapper = new CertificateMapper();
        return certificateMapper.certificateWithCommonNameToCertificateDto(this.certificateService.findMyCertificateBySerialNumber(serialNumber));
    }
}
