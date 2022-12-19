package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.API.dto.OcppJsonStatusResponse;
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.web.controller.ChargePointsController;
import de.rwth.idsg.steve.web.dto.ChargePointForm;
import de.rwth.idsg.steve.web.dto.OcppJsonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChargePoint {

    @Autowired
    private ChargePointsController chargePointsController;

    @Autowired
    private ChargePointHelperService chargePointHelperService;

    @Autowired
    private ChargePointRepository chargePointRepository;


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


}
