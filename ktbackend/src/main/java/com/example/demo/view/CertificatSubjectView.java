package com.example.demo.view;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatSubjectView {

    private String fullname;
    private String surname;
    private String givenname;
    private String e;
    private String uid;
    private String issuerData;
    private String startDate;
    private String endDate;
    private String alias;
    private String speciality;
}
