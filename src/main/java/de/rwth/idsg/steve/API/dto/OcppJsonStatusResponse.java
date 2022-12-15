/*
 * SteVe - SteckdosenVerwaltung - https://github.com/steve-community/steve
 * Copyright (C) 2013-2019 RWTH Aachen University - Information Systems - Intelligent Distributed Systems Group (IDSG).
 * All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.rwth.idsg.steve.API.dto;

import de.rwth.idsg.steve.ocpp.OcppVersion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * @author Sevket Goekay <sevketgokay@gmail.com>
 * @since 25.03.2015
 */
@Getter
@Setter
@Builder
@ToString
public final class OcppJsonStatusResponse {
    private  int chargeBoxPk;
    private  String chargeBoxId, connectedSince;
    private  String connectionDuration;
    private  OcppVersion version;
    private  String connectedSinceDT;

    public OcppJsonStatusResponse() {
    }

    public OcppJsonStatusResponse(int chargeBoxPk, String chargeBoxId, String connectedSince, String connectionDuration, OcppVersion version, String connectedSinceDT) {
        this.chargeBoxPk = chargeBoxPk;
        this.chargeBoxId = chargeBoxId;
        this.connectedSince = connectedSince;
        this.connectionDuration = connectionDuration;
        this.version = version;
        this.connectedSinceDT = connectedSinceDT;
    }
}
