package com.example.demo.service;

import com.example.demo.dto.CheckResponse;
import com.example.demo.pki.keystores.KeyStoreReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

@Service
public class OcspService {

    KeyStoreReader keyStoreReader = new KeyStoreReader();

    @Autowired
    private OcspService ocspService;

    public String downloadCertificate(String uidd){
        String uid = uidd.split("=")[0];

        //Pravimo Issuar
        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> certificates2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        certificates.addAll(certificates1);
        certificates.addAll(certificates2);

        X509Certificate x509Certificate = null;
        Certificate cert = null;

        for (X509Certificate certificate : certificates)
        {
            if(certificate.getSubjectX500Principal().getName().split(",")[0].split("\\=")[1].equals(uid))
            {
                x509Certificate = certificate;
            }
        }
        if (x509Certificate != null)
            cert = (X509Certificate) x509Certificate;


        return cert.toString();
    }

    public CheckResponse checkCertificate(String uidd) throws NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        Boolean n = false;
        String uid = uidd.split("=")[0];

        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> certificates2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        certificates.addAll(certificates1);
        certificates.addAll(certificates2);

        X509Certificate x509Certificate = null;
        Certificate cert = null;

        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/root.jks"));
        ks.load(in, "12345".toCharArray());

        String uidIssuar = "";
        String uidSubject = "";
        uidSubject = uid;

        boolean endWhile = false;
        boolean endProgram = false; //Ako ne postoji taj sertifikat
        do {
            for (X509Certificate certificate : certificates)
            {
                if(certificate.getSubjectX500Principal().getName().split(",")[0].split("\\=")[1].equals(uidSubject))
                {
                    endProgram = true;
                    x509Certificate = certificate;
                    if(new Date().compareTo(x509Certificate.getNotAfter()) == 1) {
                        this.ocspService.pullCertificate(uidd);
                        n = true;
                    }

                    uidIssuar = x509Certificate.getIssuerX500Principal().getName().split(",")[0];
                }
            }
            if(endProgram == false) {
                CheckResponse crr = new CheckResponse();

                crr.setUid(null);
                crr.setCheck(true);

                return crr;
            }


            String certificatesFromFile = "";
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/ocsp.txt"))) {
                int ch = bufferedInputStream.read();
                while (ch != -1) {
                    certificatesFromFile = certificatesFromFile + (char) ch;
                    ch = bufferedInputStream.read();
                }
            } catch (FileNotFoundException e) {

            }

            //Pisanje
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("./src/main/resources/keystores/ocsp.txt"))) {

                if (certificatesFromFile != "") { //Znaci da postoji nesto upisano u fajl
                    //Provera da li je izabrani sertifikat vec povucen i upisan, da ga ne povlacimo dva puta

                    String certs[] = certificatesFromFile.split("END");

                    for (String s : certs) {
                        String str[] = s.split(" ");
                        for (int i = 0; i < str.length; i++)

                            if (str[i].split("=")[0].equals("UID")) {
                                if (str[i].split("=")[1].split(",")[0].equals(uidSubject)) {
                                    n = true;
                                    break;
                                }
                                break;
                            }
                    }
                    bufferedOutputStream.write(certificatesFromFile.getBytes());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(uidSubject.equals(uidIssuar.split("\\=")[1]))
                endWhile = true;

            uidSubject = uidIssuar.split("\\=")[1];
        } while(endWhile != true);

        //Dto object for response
        CheckResponse cr = new CheckResponse();
        cr.setUid(uid);
        cr.setCheck(n);

        return cr;
    }

    public void pullCertificate(String uidd) throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        String uid = uidd.split("=")[0];

        //Pravimo Issuar
        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> certificates2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        certificates.addAll(certificates1);
        certificates.addAll(certificates2);

        X509Certificate x509Certificate = null;
        Certificate cert = null;

        for (X509Certificate certificate : certificates)
        {
            if(certificate.getSubjectX500Principal().getName().split(",")[0].split("\\=")[1].equals(uid))
            {
                x509Certificate = certificate;
            }
        }
        if (x509Certificate != null)
            cert = (X509Certificate) x509Certificate;

        //Citanje
        String certificatesFromFile = "";
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/ocsp.txt"))) {
            int ch = bufferedInputStream.read();
            while(ch != -1) {
                certificatesFromFile = certificatesFromFile + (char)ch;
                ch = bufferedInputStream.read();
            }
        } catch (FileNotFoundException e) {

        }

        //Pisanje
        try(BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("./src/main/resources/keystores/ocsp.txt"))) {
            String certificateForWrite = cert.toString() + "END";

            if(certificatesFromFile != "") { //Znaci da postoji nesto upisano u fajl
                //Provera da li je izabrani sertifikat vec povucen i upisan, da ga ne povlacimo dva puta
                String certs[] = certificatesFromFile.split("END");

                Boolean n = false;
                for(String s : certs){
                    String str[] = s.split(" ");
                    for (int i=0; i < str.length; i++)

                        if (str[i].split("=")[0].equals("UID"))
                        {
                            if ( str[i].split("=")[1].split(",")[0].equals(uid)){
                                n = true;
                            }
                            break;
                        }
                }

                if (!n) { // Upisujemo novi sertifikat samo ako ga nismo pronasli da je vec upisan, jer nema smisla 2x upisati isti sertiikat
                    certificatesFromFile = certificatesFromFile + certificateForWrite;
                }
                bufferedOutputStream.write(certificatesFromFile.getBytes());
            }
            else { //Prvi put ako upisujemo u fajl
                bufferedOutputStream.write(certificateForWrite.getBytes());
            }
        } catch (IOException e) {
            // exception handling
        }
    }
}
