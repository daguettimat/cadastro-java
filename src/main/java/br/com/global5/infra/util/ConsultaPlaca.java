package br.com.global5.infra.util;

import java.net.*;
import java.io.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.apache.commons.codec.binary.Hex;

public class ConsultaPlaca {

    public static void main(String[] args) {
        String placa = "DJL-7576";
        String token = hmacSha1(placa, placa + "TRwf1iBwvCoSboSscGne");
        StringBuilder soapMessage = new StringBuilder();

        soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>")
                .append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ")
                .append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
                .append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" >")
                .append("<soap:Header>")
                .append("<dispositivo>GT-S1312L</dispositivo>")
                .append("<nomeSO>Android</nomeSO>")
                .append("<versaoAplicativo>1.1.1</versaoAplicativo><versaoSO>4.1.4</versaoSO>")
                .append("<aplicativo>aplicativo</aplicativo><ip>177.206.169.90</ip>")
                .append("<token>").append(token).append("</token>")
                .append("<latitude>-3.6770324</latitude>")
                .append("<longitude>-38.6791411</longitude>")
                .append("</soap:Header>")
                .append("<soap:Body>")
                .append("<webs:getStatus xmlns:webs=\"http://soap.ws.placa.service.sinesp.serpro.gov.br/\">")
                .append("<placa>").append(placa).append("</placa>")
                .append("</webs:getStatus>")
                .append("</soap:Body>")
                .append("</soap:Envelope>");

        metodo2(soapMessage.toString(), "http://sinespcidadao.sinesp.gov.br/sinesp-cidadao/mobile/consultar-placa");
    }

    private static String hmacSha1(String value, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(value.getBytes());

            byte[] hexBytes = new Hex().encode(rawHmac);

            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void metodo2(String envelope, String urlAddress) {
        try {
            MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage message;

            try {
                MimeHeaders header = new MimeHeaders();
                header.addHeader("Content-Type", "application/soap+xml");

                message = factory.createMessage(header, new ByteArrayInputStream(envelope.getBytes()));
                SOAPConnection con = SOAPConnectionFactory.newInstance().createConnection();

                URL url = new URL(urlAddress);
                System.out.println("Message enviada \n" + message);
                SOAPMessage res = con.call(message, url);

                ByteArrayOutputStream in = new ByteArrayOutputStream();
                message.writeTo(in);
                System.out.println("in :\n" + desnormalizar(in.toString()));

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                res.writeTo(out);
                System.out.println("out :\n" + desnormalizar(out.toString()));
            } catch (IOException ex) {
                System.out.println("" + ex.getMessage());
            }
        } catch (SOAPException ex) {
            System.out.println("" + ex.getMessage());
        }
    }

    public static String desnormalizar(String texto) {
        return texto.replace("><", ">\n<");
    }
}