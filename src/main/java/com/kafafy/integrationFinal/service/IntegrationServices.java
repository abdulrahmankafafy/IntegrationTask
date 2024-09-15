package com.kafafy.integrationFinal.service;

import com.kafafy.integrationFinal.entity.InputEntity;
import com.kafafy.integrationFinal.model.FinalOutputModel;
import com.kafafy.integrationFinal.model.RestModel;
import com.kafafy.integrationFinal.model.SoapModel;
import com.kafafy.integrationFinal.exception.SpecificException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class IntegrationServices {

    public FinalOutputModel processInput(InputEntity inputEntity) {
        FinalOutputModel finalOutput = new FinalOutputModel();

        String mobile = inputEntity.getMobile();
        String sms = inputEntity.getSms();
        String nid = inputEntity.getNid();

        if (!isValidMobile(mobile)) {
            throw new SpecificException("Invalid mobile number. The number should be 11 digits.", "400");
        }

        String smsMessage = calculateSmsLength(sms);
        finalOutput.setSmsMessage(smsMessage);

        try {
            SoapModel soapModel = getSoapModelFromService(mobile, sms, nid);

            finalOutput.updateFromSoapModel(soapModel);

            // Check statusCode to decide whether to call REST service
            if ("0".equals(soapModel.getStatusCode())) {
                RestModel restModel = callRestMockService(mobile, sms);
                restModel.setSmsMessage(smsMessage);
                finalOutput.updateFromRestModel(restModel);
            }

        } catch (Exception e) {
            throw new SpecificException("Error during service execution", "500");
        }

        return finalOutput;
    }

    private boolean isValidMobile(String mobile) {
        return mobile.length() == 11;
    }

    private String calculateSmsLength(String sms) {
        int length = sms != null ? sms.length() : 0;
        return length > 20 ? "this message will be sent as 2 sms" : "this message will be sent as 1 sms";
    }

    private SoapModel getSoapModelFromService(String mobile, String sms, String nid) {
        SoapModel soapModel = new SoapModel();
        try {
            URL url = new URL("http://localhost:8088/s");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

            String soapRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsdl=\"http://example.com/wsdl\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<wsdl:ValidateUserRequest>" +
                    "<mobile>002" + mobile + "</mobile>" +  // Add prefix when sending to SOAP
                    "<sms>" + sms + "</sms>" +
                    "<nid>" + nid + "</nid>" +
                    "</wsdl:ValidateUserRequest>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";

            OutputStream out = httpConn.getOutputStream();
            out.write(soapRequest.getBytes());
            out.close();

            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(httpConn.getInputStream());

                soapModel.setCifNumber(getTagValue(document, "cifNumber"));
                System.out.println("CifNum: " + soapModel.getCifNumber());
                soapModel.setStatusDesc("Success");
                soapModel.setCode(getTagValue(document, "code"));
                soapModel.setMessage(getTagValue(document, "message"));
                String mobileFromSoap = getTagValue(document, "mobile").trim(); // Retrieve mobile from SOAP response
                String mobileWithoutPrefixFromSoap = mobileFromSoap.startsWith("002") ? mobileFromSoap.substring(3) : mobileFromSoap;  // Strip prefix from SOAP response
                soapModel.setMobileWithoutPrefix(mobileWithoutPrefixFromSoap); // Store stripped mobile number
                System.out.println("Mobile without prefix from SOAP response: " + mobileWithoutPrefixFromSoap);

                // Compare mobile numbers without the prefix
                if (mobileWithoutPrefixFromSoap.equals(mobile)) {
                    // Mobile numbers match
                    soapModel.setStatusDesc("Mobile numberss match.");
                } else {
                    soapModel.setStatusDesc("Mobile numbers do not match.");
                    soapModel.setMessage("The provided mobile number does not match the one in the SOAP response.");
                    soapModel.setStatusCode("1");
                }

            } else {
                throw new SpecificException("Failed to connect to SOAP service. HTTP response code: " + responseCode, "500");
            }
        } catch (Exception e) {
            throw new SpecificException("Error during SOAP request or response processing: " + e.getMessage(), "500");
        }
        return soapModel;
    }


    private String getTagValue(Document document, String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    public RestModel callRestMockService(String mobile, String sms) {
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = "http://localhost:8080/rest";

        Map<String, String> request = new HashMap<>();
        request.put("mobile", mobile);
        request.put("sms", sms);

        Map<String, String> response = restTemplate.postForObject(restUrl, request, Map.class);
        RestModel restModel = new RestModel();
        restModel.setRestStatus(response.get("status"));
        restModel.setSmsMessage(response.get("smsMessage"));
        restModel.setDesc(response.get("desc"));

        return restModel;
    }
}
