package com.example.demo.controller;
import com.example.demo.pki.keystores.KeyStoreReader;
import com.example.demo.pki.keystores.KeyStoreWriter;
import com.example.demo.service.CertificateService;
import com.example.demo.view.CertificatSubjectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;

@RestController
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/addCertificate")
    public ResponseEntity<?> addCertificate(@RequestBody CertificatSubjectView certificatSubjectView) throws ParseException, IOException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, UnrecoverableKeyException {
       this.certificateService.addCertificate(certificatSubjectView);
       return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/getAllCertificates")
    public ResponseEntity<?> getAllCertificates(){
        return new ResponseEntity<>(this.certificateService.getAllCertificates(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/getAllAliases")
    public ResponseEntity<?> getAllAliases() throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return new ResponseEntity<>(this.certificateService.getAllAliases(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/getAllCertificatesForCardList")
    public ResponseEntity<?> getAllCertificatesForCardList() {
        return new ResponseEntity<>(this.certificateService.getAllCertificatesForCardList(), HttpStatus.OK);
    }
}