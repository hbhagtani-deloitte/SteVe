package de.rwth.idsg.steve.API;

import de.rwth.idsg.steve.web.controller.OcppTagsController;
import de.rwth.idsg.steve.web.dto.OcppTagForm;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class User {

    @Autowired
    private OcppTagsController ocppTagsController;

    //add user to the ocpp tag
    @PostMapping("/ocppTags")
    public String findOcppTags(@RequestBody HashMap<String, String> hashMap){
        OcppTagForm params= new OcppTagForm();
        params.setIdTag(hashMap.get("idTag"));
        params.setParentIdTag(hashMap.get("parentIdTag"));
        params.setExpiryDate(LocalDateTime.parse(hashMap.get("expiryDate")));
        params.setNote(hashMap.get("note"));
        params.setMaxActiveTransactionCount(Integer.valueOf(hashMap.get("maxActiveTransactionCount")));
        ocppTagsController.add(params);
        return "OCPP Tag is added";
    }
}
