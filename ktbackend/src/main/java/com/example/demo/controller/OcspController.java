package com.example.demo.controller;
import com.example.demo.service.OcspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

@RestController
public class OcspController {

    @Autowired
    private OcspService ocspService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/checkCertificateOcsp")
    public ResponseEntity<?> checkCertificateOcsp(@RequestBody String uidd) throws IOException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException {
        return new ResponseEntity<>(this.ocspService.checkCertificate(uidd), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/pullCertificateOcsp")
    public ResponseEntity<?> pullCertificateOcsp(@RequestBody String uidd) throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        this.ocspService.pullCertificate(uidd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/downloadCertificate")
    public ResponseEntity<?> downloadCertificate(@RequestBody String uidd) throws NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        return new ResponseEntity<>(this.ocspService.downloadCertificate(uidd), HttpStatus.OK);

    }

}