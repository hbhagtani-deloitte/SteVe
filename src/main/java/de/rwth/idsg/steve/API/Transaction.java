package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.repository.TransactionRepository;
import de.rwth.idsg.steve.web.dto.TransactionQueryForm;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class Transaction {

    private String API_URL="http://127.0.0.1:8080/";

    public void sendStartTransactionNotifiactiontoCSMS(String chargeBoxIdentity, Integer connectorId, String idTag, DateTime timestamp,Integer MeterStart,Integer reservationId, Integer statusOfTransaction){
        HashMap<String, String> store= new HashMap<>();
        store.put("timestamp", String.valueOf(timestamp));
        store.put("chargeBoxId", String.valueOf(chargeBoxIdentity));
        store.put("reservationId", String.valueOf(reservationId));
        store.put("connectorId", String.valueOf(connectorId));
        store.put("idTag", String.valueOf(idTag));
        store.put("MeterStart", String.valueOf(MeterStart));
        store.put("statusOfTransaction", String.valueOf(statusOfTransaction));
        String url = API_URL+"/getStartTransaction";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String result = new RestTemplate().postForObject(url, store, String.class);
//        System.out.println(result);
    }

    public void sendStopTransactionNotifiactiontoCSMS(String chargeBoxIdentity, Integer transactionId, DateTime timestamp,Integer MeterStop,String stopReason, Integer statusOfTransaction, String idTag){
        HashMap<String, String> store= new HashMap<>();
        store.put("timestamp", String.valueOf(timestamp));
        store.put("chargeBoxId", String.valueOf(chargeBoxIdentity));
        store.put("transactionId", String.valueOf(transactionId));
        store.put("stopReason", String.valueOf(stopReason));
        store.put("MeterStop", String.valueOf(MeterStop));
        store.put("statusOfTransaction", String.valueOf(statusOfTransaction));
        store.put("idTag", String.valueOf(idTag));
        String url = API_URL+"/getStopTransaction";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String result = new RestTemplate().postForObject(url, store, String.class);
//        System.out.println(result);
    }
}
