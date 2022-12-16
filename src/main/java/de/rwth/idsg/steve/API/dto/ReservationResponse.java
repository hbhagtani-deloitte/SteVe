package de.rwth.idsg.steve.API.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
@Getter
@Setter
@Builder
public class ReservationResponse {
    private int connectorId;
    private Integer transactionId;
    private String ocppIdTag, chargeBoxId, startDatetime, expiryDatetime, status;

    public ReservationResponse(int connectorId, Integer transactionId, String ocppIdTag, String chargeBoxId, String startDatetime, String expiryDatetime, String status) {
        this.connectorId = connectorId;
        this.transactionId = transactionId;
        this.ocppIdTag = ocppIdTag;
        this.chargeBoxId = chargeBoxId;
        this.startDatetime = startDatetime;
        this.expiryDatetime = expiryDatetime;
        this.status = status;
    }

    public ReservationResponse() {
    }
}
