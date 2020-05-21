package com.example.demo.service;

import com.example.demo.pki.GenerateRootCertificate;
import com.example.demo.pki.certificates.CertificateGenerator;
import com.example.demo.pki.data.IssuerData;
import com.example.demo.pki.data.SubjectData;
import com.example.demo.pki.keystores.KeyStoreReader;
import com.example.demo.pki.keystores.KeyStoreWriter;
import com.example.demo.view.CertificatSubjectView;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class CertificateService {

    KeyStoreReader keyStoreReader = new KeyStoreReader();
    KeyStoreWriter keyStoreWriter = new KeyStoreWriter();

    public List<String> getAllCertificatesForCardList() {
        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> certificates2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        certificates.addAll(certificates1);
        certificates.addAll(certificates2);

        List<String> subjectData = new ArrayList<>();
        for (X509Certificate c : certificates) {
            subjectData.add(c.getSubjectX500Principal().getName() + "+" + c.getNotBefore().toString() + "+" + c.getNotAfter().toString() + "+");
        }

        return subjectData;
    }

    public List<String> getAllAliases() throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/root.jks"));
        ks.load(in, "12345".toCharArray());

        KeyStore ks1 = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in1 = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/CA.jks"));
        ks1.load(in1, "12345".toCharArray());

        KeyStore ks2 = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in2 = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/endEntity.jks"));
        ks2.load(in2, "12345".toCharArray());

        Enumeration<String> aliases = ks.aliases();
        Enumeration<String> aliases1 = ks1.aliases();
        Enumeration<String> aliases2 = ks2.aliases();

        List<String> al = Collections.list(aliases);
        List<String> al1 = Collections.list(aliases1);
        List<String> al2 = Collections.list(aliases2);


        al.addAll(al1);
        al.addAll(al2);

        return al;
    }

    public List<String> getAllCertificates() {
        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> certificates2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        certificates.addAll(certificates1);
        certificates.addAll(certificates2);

        List<String> subjectData = new ArrayList<>();
        for (X509Certificate c : certificates)
        {
            subjectData.add(c.getSubjectX500Principal().getName() + "+" + c.getNotBefore().toString() + "+" + c.getNotAfter().toString() + "+");
        }

        List<String> nameAndUid = new ArrayList<>();
        for (String s : subjectData)
        {
            String split = s.split(",")[5].split("=")[1] + "(" + s.split(",")[0] + ")";
            nameAndUid.add(split.split("\\+")[0] + split.split("\\+")[3] + "+" + s.split("\\+")[1] + "+" + s.split("\\+")[2] + "+" + s.split(",")[1].split("=")[1]);
        }

        return nameAndUid;
    }

    public void addCertificate(CertificatSubjectView certificatSubjectView) throws ParseException, NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        //Pravimo Subject
        GenerateRootCertificate grc = new GenerateRootCertificate();
        KeyPair keyPairSubject = grc.generateKeyPair();

        String startDateSplit[] = certificatSubjectView.getStartDate().split("-");
        String startDateString = startDateSplit[2] + "-" + startDateSplit[1] + "-" + startDateSplit[0];
        String endDateSplit[] = certificatSubjectView.getEndDate().split("-");
        String endDateString = endDateSplit[2] + "-" + endDateSplit[1] + "-" + endDateSplit[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = simpleDateFormat.parse(startDateString);
        Date endDate = simpleDateFormat.parse(endDateString);

        X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        x500NameBuilder.addRDN(BCStyle.CN, certificatSubjectView.getFullname());
        x500NameBuilder.addRDN(BCStyle.SURNAME, certificatSubjectView.getSurname());
        x500NameBuilder.addRDN(BCStyle.GIVENNAME, certificatSubjectView.getGivenname());
        x500NameBuilder.addRDN(BCStyle.E, certificatSubjectView.getE());
        x500NameBuilder.addRDN(BCStyle.C, certificatSubjectView.getSpeciality());
        x500NameBuilder.addRDN(BCStyle.UID, certificatSubjectView.getUid());

        //Kreiranje random serijskog broja koji ne postoji ni u jednom sertifikatu
        List<X509Certificate> cs = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> cs1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        List<X509Certificate> cs2 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/endEntity.jks", "12345");

        cs.addAll(cs1);
        cs.addAll(cs2);

        String serialNumber_1;
        boolean p;
        SecureRandom rand = new SecureRandom();

        do {
            int newSerialNumberOfSubject = rand.nextInt(1000);
            serialNumber_1 = String.valueOf(newSerialNumberOfSubject);

            p = false;
            for (X509Certificate cer : cs) {
                if ( cer.getSerialNumber() == BigInteger.valueOf(Integer.parseInt(serialNumber_1))) {
                    p = true;
                }
            }
        }while(p);

        SubjectData subjectData = new SubjectData(keyPairSubject.getPublic(), x500NameBuilder.build(), serialNumber_1, startDate, endDate);
        //-----------------------------------------------

        //Pravimo Issuar
        List<X509Certificate> certificates = keyStoreReader.getX509Certificates("./src/main/resources/keystores/root.jks", "12345");
        List<X509Certificate> certificates1 = keyStoreReader.getX509Certificates("./src/main/resources/keystores/CA.jks", "12345");
        certificates.addAll(certificates1);

        X509Certificate x509Certificate = null;
        Certificate cert = null;
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/root.jks"));
        ks.load(in, "12345".toCharArray());

        KeyStore ks1 = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in1 = new BufferedInputStream(new FileInputStream("./src/main/resources/keystores/CA.jks"));
        ks1.load(in1, "12345".toCharArray());

        String alias = "";
        String alias1 = "";

        char[] password ={ '1', '2', '3', '4', '5' };
        IssuerData issuerData = new IssuerData();
        IssuerData issuerData1 = new IssuerData();

        KeyPair kp = grc.generateKeyPair();

        if(!certificatSubjectView.getSpeciality().equals("Root")) {
            String uid = certificatSubjectView.getIssuerData().split("\\(")[1];
            uid = uid.split("\\)")[0];
            for (X509Certificate certificate : certificates)
            {
                if(certificate.getSubjectX500Principal().getName().split(",")[0].equals(uid))
                {
                    x509Certificate = certificate;
                }
            }
            if (x509Certificate != null)
                cert = (X509Certificate) x509Certificate;
            alias = ks.getCertificateAlias(cert);
            alias1 = ks1.getCertificateAlias(cert);
            if(alias == null) {
                alias = alias1;
                ks = ks1;
            }

            try{
                issuerData = keyStoreReader.readIssuerFromStore("./src/main/resources/keystores/root.jks", alias, password, password);
            }
            catch(Exception e){
                issuerData = null;
            }
            try{
                issuerData1 = keyStoreReader.readIssuerFromStore("./src/main/resources/keystores/CA.jks", alias, password, password);
            }
            catch(Exception e){
                issuerData1 = null;
            }

            if(issuerData == null)
                issuerData = issuerData1;

        } else {
            KeyPair keyPair = grc.generateKeyPair();
            X500NameBuilder name = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilder.addRDN(BCStyle.CN, certificatSubjectView.getFullname());
            x500NameBuilder.addRDN(BCStyle.SURNAME, certificatSubjectView.getSurname());
            x500NameBuilder.addRDN(BCStyle.GIVENNAME, certificatSubjectView.getGivenname());
            x500NameBuilder.addRDN(BCStyle.E, certificatSubjectView.getE());
            x500NameBuilder.addRDN(BCStyle.C, certificatSubjectView.getSpeciality());
            x500NameBuilder.addRDN(BCStyle.UID, certificatSubjectView.getUid());
//            issuerData.setPrivateKey(keyPair.getPrivate());
//            issuerData.setX500Name(name.build());

            issuerData = new IssuerData(keyPair.getPrivate(), x500NameBuilder.build());

            kp = keyPair;
        }

        //Pravimo novi sertifikat na osnocu Subject i Issuar
        CertificateGenerator certificateGenerator2 = new CertificateGenerator();
        X509Certificate x509Certificate2 = certificateGenerator2.generateCertificate(subjectData, issuerData);
        //-----------------------------------------------

        //Upisujemo sertifikat u KeyStore fajl
        if(certificatSubjectView.getSpeciality().equals("Root")) {
            PrivateKey privKey = (PrivateKey) ks.getKey(alias, password);
            keyStoreWriter.loadKeyStore("./src/main/resources/keystores/root.jks", password);
            keyStoreWriter.write(certificatSubjectView.getAlias(), kp.getPrivate(), password, x509Certificate2);
            keyStoreWriter.saveKeyStore("./src/main/resources/keystores/root.jks", password);
        } else if(certificatSubjectView.getSpeciality().equals("CA")) {
            PrivateKey privKey = (PrivateKey) ks.getKey(alias, password);
            keyStoreWriter.loadKeyStore("./src/main/resources/keystores/CA.jks", password);
            keyStoreWriter.write(certificatSubjectView.getAlias(), privKey, password, x509Certificate2);
            keyStoreWriter.saveKeyStore("./src/main/resources/keystores/CA.jks", password);
        } else if(certificatSubjectView.getSpeciality().equals("End-entity")) {
            PrivateKey privKey = (PrivateKey) ks.getKey(alias, password);
            keyStoreWriter.loadKeyStore("./src/main/resources/keystores/endEntity.jks", password);
            keyStoreWriter.write(certificatSubjectView.getAlias(), privKey, password, x509Certificate2);
            keyStoreWriter.saveKeyStore("./src/main/resources/keystores/endEntity.jks", password);
        }
        //-----------------------------------------------

        List<Certificate> c = keyStoreReader.getCertificates("./src/main/resources/keystores/root.jks", "12345");
        List<Certificate> c1 = keyStoreReader.getCertificates("./src/main/resources/keystores/CA.jks", "12345");
        List<Certificate> c2 = keyStoreReader.getCertificates("./src/main/resources/keystores/endEntity.jks", "12345");

    }
}
