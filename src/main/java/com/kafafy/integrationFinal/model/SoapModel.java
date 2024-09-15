package com.kafafy.integrationFinal.model;

import lombok.Data;

@Data
public class SoapModel {
    private String statusDesc;
    private String code;
    private String message;
    private String cifNumber;
    private String mobileWithoutPrefix;
    private String statusCode;
}