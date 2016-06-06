/***Licensed to the Apache Software Foundation (ASF) under one
*or more contributor license agreements.  See the NOTICE file
*distributed with this work for additional information
*regarding copyright ownership.  The ASF licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package epsos.ccd.gnomon.auditmanager;

import java.math.BigInteger;

/**
 * Enumeration for populating the EventOutcomeIndicator of the AuditMessage
 * Possible values are: 0:full success,1:partial delivery,4:temporal or
 * recoverable failures,8:permanent failure
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 */
public enum EventOutcomeIndicator {

    FULL_SUCCESS(new BigInteger("0")), PARTIAL_DELIVERY(new BigInteger("1")),
    TEMPORAL_FAILURE(new BigInteger("4")), PERMANENT_FAILURE(new BigInteger("8"));

    private BigInteger code;

    private EventOutcomeIndicator(BigInteger c) {
        code = c;
    }

    public BigInteger getCode() {
        return code;
    }

}
