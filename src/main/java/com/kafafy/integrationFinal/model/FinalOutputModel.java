package com.kafafy.integrationFinal.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
public class FinalOutputModel {
    private String statusDesc;
    private String code;
    private String message;
    private String cifNumber;
    private String mobileWithoutPrefix;
    private String smsMessage;

    public void updateFromSoapModel(SoapModel soapModel) {
        this.statusDesc = soapModel.getStatusDesc();
        this.cifNumber = soapModel.getCifNumber();
        this.mobileWithoutPrefix = soapModel.getMobileWithoutPrefix();
    }

    public void updateFromRestModel(RestModel restModel) {
        this.smsMessage = restModel.getSmsMessage();
    }
}
