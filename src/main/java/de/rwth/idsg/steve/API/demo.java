package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.ocpp.OcppProtocol;
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
import ocpp.cs._2015._10.BootNotificationRequest;
import ocpp.cs._2015._10.BootNotificationResponse;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PostMapping("/hello")
    public String helloFunction(){
        return "Budget ggwp";
    }

    @PostMapping("/chargePointAddition")
    public String chargePointsAddition(@RequestBody ChargePointForm form){
        chargePointsController.add(form);
        return "Done";
    }

    @PostMapping("/bootNotification")
    public Hashtable<String, String> addBootNotification(@RequestBody BootNotificationRequest parameters, @RequestParam String chargeBoxIdentity, @RequestParam OcppProtocol ocppProtocol)
    {
        BootNotificationResponse b= (BootNotificationResponse) centralSystemService16_service.bootNotification(parameters,chargeBoxIdentity , ocppProtocol);
        String a = b.toString();
        Hashtable<String, String> json= stringToJson(a, "BootNotificationResponse");
            return  json;
    }

    @GetMapping("/connectedCS")
    public ArrayList<Hashtable<String, String>> getConnectedChargeBox() {
        List<OcppJsonStatus> a = chargePointHelperService.getOcppJsonStatus();
        ArrayList<Hashtable<String, String>> finalArray= new ArrayList();
        for(int i=0; i<a.size();i++) {
            Hashtable<String, String> json= stringToJson(String.valueOf(a.get(i)), "OcppJsonStatus");
            finalArray.add(json);
        }
        return finalArray;
    }

//    @PostMapping("/reserveNow")
//    public String reservationStart(@RequestBody ReserveNowParams reserveNowParams){
//        try{
//            reserveNowParams.setChargePointSelectList(reserveNowParams.getChargePointSelectList());
//            reserveNowParams.setExpiry(reserveNowParams.getExpiry());
//            reserveNowParams.setIdTag(reserveNowParams.getIdTag());
//            reserveNowParams.setConnectorId(reserveNowParams.getConnectorId());
//        return "Done";}
//        catch (Exception e){
//            return String.valueOf(e);
//        }
//    }

    @PostMapping("/reserveNow")
    public String reservationStart(@RequestParam String connectorId, @RequestParam String expiry, @RequestParam String idTag, @RequestParam String chargePoints){
        try{
            ReserveNowParams reserveNowParams=new ReserveNowParams();
            ChargePointSelect var1=new ChargePointSelect(JSON, chargePoints);
            List<ChargePointSelect> var= new ArrayList<>();
            var.add(var1);
            reserveNowParams.setChargePointSelectList(var);
            reserveNowParams.setExpiry(LocalDateTime.parse(expiry));
            reserveNowParams.setIdTag(idTag);
            reserveNowParams.setConnectorId(Integer.valueOf(connectorId));
            int a=chargePointService16_client.reserveNow(reserveNowParams);
            System.out.println(a);
            return "Done";}
        catch (Exception e){
            return String.valueOf(e);
        }
    }
    @PostMapping("/reserveCancel")
    public String reservationCancelled(@RequestParam String reservationId, @RequestParam String chargePoints){
        try{
//            (@RequestBody CancelReservationParams cancelReservationParams){
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
        }}

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

    @GetMapping("/getAllConnectors")
    public List<Integer> getNonZeroConnectorIds(@RequestParam String chargeBoxIdentity){
        return chargePointRepository.getNonZeroConnectorIds(chargeBoxIdentity);
    }

    // add user for ocpp tag
    // cancel reservation




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