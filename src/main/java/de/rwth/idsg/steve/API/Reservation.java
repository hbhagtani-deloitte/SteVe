package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.API.dto.ReservationResponse;
import de.rwth.idsg.steve.repository.ReservationRepository;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.service.CentralSystemService16_Service;
import de.rwth.idsg.steve.service.ChargePointService16_Client;
import de.rwth.idsg.steve.web.dto.ReservationQueryForm;
import de.rwth.idsg.steve.web.dto.ocpp.CancelReservationParams;
import de.rwth.idsg.steve.web.dto.ocpp.ReserveNowParams;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.rwth.idsg.steve.ocpp.OcppTransport.JSON;

@RestController
public class Reservation {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChargePointService16_Client chargePointService16_client;


    @PostMapping("/reserveNow")
    public String reservationStart(@RequestBody HashMap<String,String> hashMap){
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
            return "Charger "+hashMap.get("chargerName")+" reservation get Booked";
        }
        catch (Exception e){
            return "Some error occured in booking reservation";
        }
    }

    @PostMapping("/reserveCancel")
    public String reservationCancelled(@RequestBody HashMap<String,String> hashMap){

        try{
            CancelReservationParams cancelReservationParams= new CancelReservationParams();
            ChargePointSelect var1=new ChargePointSelect(JSON, hashMap.get("chargerName"));
            List<ChargePointSelect> var= new ArrayList<>();
            var.add(var1);
            System.out.println(hashMap);
            cancelReservationParams.setChargePointSelectList(var);
            cancelReservationParams.setReservationId(Integer.valueOf(hashMap.get("reservationId")));
            int a=chargePointService16_client.cancelReservation(cancelReservationParams);
            return "Charger "+hashMap.get("chargerName")+" reservation got Cancelled";
        }
        catch (Exception e){
            return "Some error occured in cancel reservation";
        }
    }


    //Get all reservation of a user via OCPP tag
    @GetMapping("/getAllUserReservations")
    public ArrayList<ReservationResponse> getAllReservationByOcppTag(@RequestParam String ocppTag){
        ReservationQueryForm reservationQueryForm= new ReservationQueryForm();
        reservationQueryForm.setOcppIdTag(ocppTag);
        List<de.rwth.idsg.steve.repository.dto.Reservation> var= reservationRepository.getReservations(reservationQueryForm);
        ArrayList<ReservationResponse> responses= new ArrayList<>();
        for (int i=0;i<var.size();i++)
        {
            de.rwth.idsg.steve.repository.dto.Reservation reservation = var.get(i);
            ReservationResponse reservationResponse = new ReservationResponse();
            reservationResponse.setChargeBoxId(reservation.getChargeBoxId());
            reservationResponse.setOcppIdTag(reservation.getOcppIdTag());
            reservationResponse.setConnectorId(reservation.getConnectorId());
            reservationResponse.setStatus(reservation.getStatus());
            reservationResponse.setTransactionId(0);
            reservationResponse.setExpiryDatetime(reservation.getExpiryDatetimeDT().toString());
            reservationResponse.setStartDatetime(reservation.getStartDatetimeDT().toString());
            reservationResponse.setReservationId(reservation.getId());
            responses.add(reservationResponse);
        }
        return responses;
    }
}
