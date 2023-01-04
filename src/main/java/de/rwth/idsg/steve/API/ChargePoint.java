package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.API.dto.OcppJsonStatusResponse;
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.web.controller.ChargePointsController;
import de.rwth.idsg.steve.web.dto.ChargePointForm;
import de.rwth.idsg.steve.web.dto.OcppJsonStatus;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChargePoint {

    @Autowired
    private ChargePointsController chargePointsController;

    @Autowired
    private ChargePointHelperService chargePointHelperService;

    @Autowired
    private ChargePointRepository chargePointRepository;

    private String API_URL="http://127.0.0.1:8080/";

    //Adding a new charge ponint to server
    @PostMapping("/chargePointAddition")
    public String chargePointsAddition(@RequestBody ChargePointForm form){
        chargePointsController.add(form);
        return "Done";
    }

    //Get all Active  Charge point
    @GetMapping("/connectedChargePoint")
    public ArrayList<OcppJsonStatusResponse> getConnectedChargeBox() {
        List<OcppJsonStatus> list = chargePointHelperService.getOcppJsonStatus();
        ArrayList<OcppJsonStatusResponse> responses = new ArrayList<>();

        for (int i=0;i<list.size();i++)
        {
            OcppJsonStatus ocppJsonStatus = list.get(i);
            OcppJsonStatusResponse ocppJsonStatusResponse = new OcppJsonStatusResponse();
            ocppJsonStatusResponse.setChargeBoxId(ocppJsonStatus.getChargeBoxId());
            ocppJsonStatusResponse.setConnectedSince(ocppJsonStatus.getConnectedSince());
            ocppJsonStatusResponse.setVersion(ocppJsonStatus.getVersion());
            ocppJsonStatusResponse.setChargeBoxPk(ocppJsonStatus.getChargeBoxPk());
            ocppJsonStatusResponse.setConnectionDuration(ocppJsonStatus.getConnectionDuration());
            ocppJsonStatusResponse.setConnectedSinceDT(ocppJsonStatus.getConnectedSinceDT().toString());
            responses.add(ocppJsonStatusResponse);
        }
        return responses;
    }

    //Get ALl active Connector of chargers
    @GetMapping("/getAllConnectors")
    public ArrayList<Integer> getNonZeroConnectorIds(@RequestParam String chargeBoxIdentity){
        return (ArrayList<Integer>) chargePointRepository.getNonZeroConnectorIds(chargeBoxIdentity);
    }

    public void sendStatusNotifiactiontoCSMS(DateTime timestamp , String chargeBoxId , String errorCode, int connectorId){
        HashMap<String, String> store= new HashMap<>();
        store.put("timestamp", String.valueOf(timestamp));
        store.put("chargeBoxId", String.valueOf(chargeBoxId));
        store.put("errorCode", String.valueOf(errorCode));
        store.put("connectorId", String.valueOf(connectorId));
        String url = API_URL+"/statusNotification";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String result = new RestTemplate().postForObject(url, store, String.class);
        System.out.println(result);
    }
}
