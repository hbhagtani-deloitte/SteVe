package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.API.dto.OcppJsonStatusResponse;
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.repository.OcppTagRepository;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.service.CentralSystemService16_Service;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.service.ChargePointService16_Client;
import de.rwth.idsg.steve.web.controller.ChargePointsController;
import de.rwth.idsg.steve.web.controller.HomeController;
import de.rwth.idsg.steve.web.controller.OcppTagsController;
import de.rwth.idsg.steve.web.dto.ChargePointForm;
import de.rwth.idsg.steve.web.dto.OcppJsonStatus;
import de.rwth.idsg.steve.web.dto.OcppTagForm;
import de.rwth.idsg.steve.web.dto.ocpp.CancelReservationParams;
import de.rwth.idsg.steve.web.dto.ocpp.ReserveNowParams;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static de.rwth.idsg.steve.ocpp.OcppTransport.JSON;

@RestController
public class demo {

    @Autowired
    private ChargePointsController chargePointsController;

    @Autowired
    private CentralSystemService16_Service centralSystemService16_service;

    @Autowired
    private HomeController homeController;

    @Autowired
    private ChargePointHelperService chargePointHelperService;

    @Autowired
    private ChargePointService16_Client chargePointService16_client;

    @Autowired
    private OcppTagsController ocppTagsController;

    @Autowired
    private OcppTagRepository ocppTagRepository;

    @Autowired
    private ChargePointRepository chargePointRepository;

    @GetMapping("/hello")
    public String helloFunction(){
        return "Budget ggwp";
    }

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
    public List<Integer> getNonZeroConnectorIds(@RequestParam String chargeBoxIdentity){
        return chargePointRepository.getNonZeroConnectorIds(chargeBoxIdentity);
    }


    @PostMapping("/reserveNow")
    public String reservationStart(@RequestBody HashMap<String,String> hashMap){
        System.out.println("Get callled by csms");
        try{
            ReserveNowParams reserveNowParams = new ReserveNowParams();

            ChargePointSelect var1 = new ChargePointSelect(JSON, hashMap.get("chargerName"));
            List<ChargePointSelect> var = new ArrayList<>();
            var.add(var1);
            reserveNowParams.setChargePointSelectList(var);
            String expiryTime = hashMap.get("expiryTime");
            reserveNowParams.setExpiry(LocalDateTime.parse(expiryTime));
            reserveNowParams.setIdTag(hashMap.get("customerTag"));
            reserveNowParams.setConnectorId(Integer.valueOf(hashMap.get("connectorId")));
            int a = chargePointService16_client.reserveNow(reserveNowParams);
            System.out.println(a);
            return "Done";
        }
        catch (Exception e){
            return String.valueOf(e);
        }
    }


    @GetMapping("/ocppTags")
    public String findOcppTags(@RequestParam String idTag,@RequestParam String parentIdTag,@RequestParam String expiryDate, @RequestParam String maxActiveTransactionCount, @RequestParam String note){
        OcppTagForm params= new OcppTagForm();
        params.setIdTag(idTag);
        params.setParentIdTag(parentIdTag);
        params.setExpiryDate(LocalDateTime.parse(expiryDate));
        params.setNote(note);
        params.setMaxActiveTransactionCount(Integer.valueOf(maxActiveTransactionCount));
        ocppTagsController.add(params);
        return "Done";
    }

    @PostMapping("/reserveCancel")
    public String reservationCancelled(@RequestParam String reservationId, @RequestParam String chargePoints){
        try{
            CancelReservationParams cancelReservationParams= new CancelReservationParams();
            ChargePointSelect var1=new ChargePointSelect(JSON, chargePoints);
            List<ChargePointSelect> var= new ArrayList<>();
            var.add(var1);
            cancelReservationParams.setChargePointSelectList(var);
            cancelReservationParams.setReservationId(Integer.valueOf(reservationId));
            System.out.println(reservationId);
            int a=chargePointService16_client.cancelReservation(cancelReservationParams);
            System.out.println(a);
            return "Done";
        }
        catch (Exception e){
            return "Not";
        }
    }



    private Hashtable<String, String> stringToJson(String a, String b){
        String[] parts = a.split(b);
        String str = parts[1];
        str = str.replace("(", "");
        str = str.replace(")", "");
        String[] newParts = str.split(", ");
        Hashtable<String, String> json = new Hashtable<String, String>();
        for (int i = 0; i < newParts.length; i++) {
            json.put(newParts[i].split("=")[0], newParts[i].split("=")[1]);
        }
        return json;
    }



}