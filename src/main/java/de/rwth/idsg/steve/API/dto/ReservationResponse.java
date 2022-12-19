package de.rwth.idsg.steve.API.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
@Getter
@Setter
@Builder
public class ReservationResponse {
    private int connectorId, reservationId;
    private int transactionId;
    private String ocppIdTag, chargeBoxId, startDatetime, expiryDatetime, status;

    public ReservationResponse(int reservationId, int connectorId, int transactionId, String ocppIdTag, String chargeBoxId, String startDatetime, String expiryDatetime, String status) {
        this.connectorId = connectorId;
        this.transactionId = transactionId;
        this.ocppIdTag = ocppIdTag;
        this.chargeBoxId = chargeBoxId;
        this.startDatetime = startDatetime;
        this.expiryDatetime = expiryDatetime;
        this.status = status;
        this.reservationId=reservationId;
    }

    public ReservationResponse() {
    }
}
