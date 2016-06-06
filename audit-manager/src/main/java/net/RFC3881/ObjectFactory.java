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
**/package net.RFC3881;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * @author Kostas Karkaletsis
 * @author Organization: Gnomon
 * @author mail:k.karkaletsis@gnomon.com.gr
 * @version 1.0, 2010, 30 Jun
 *
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AuditSourceIdentificationType }
     * 
     */
    public AuditSourceIdentificationType createAuditSourceIdentificationType() {
        return new AuditSourceIdentificationType();
    }

    /**
     * Create an instance of {@link ActiveParticipantType }
     * 
     */
    public ActiveParticipantType createActiveParticipantType() {
        return new ActiveParticipantType();
    }

    /**
     * Create an instance of {@link AuditMessage.ActiveParticipant }
     * 
     */
    public AuditMessage.ActiveParticipant createAuditMessageActiveParticipant() {
        return new AuditMessage.ActiveParticipant();
    }

    /**
     * Create an instance of {@link AuditMessage }
     * 
     */
    public AuditMessage createAuditMessage() {
        return new AuditMessage();
    }

    /**
     * Create an instance of {@link EventIdentificationType }
     * 
     */
    public EventIdentificationType createEventIdentificationType() {
        return new EventIdentificationType();
    }

    /**
     * Create an instance of {@link CodedValueType }
     * 
     */
    public CodedValueType createCodedValueType() {
        return new CodedValueType();
    }

    /**
     * Create an instance of {@link TypeValuePairType }
     * 
     */
    public TypeValuePairType createTypeValuePairType() {
        return new TypeValuePairType();
    }

    /**
     * Create an instance of {@link ParticipantObjectIdentificationType }
     * 
     */
    public ParticipantObjectIdentificationType createParticipantObjectIdentificationType() {
        return new ParticipantObjectIdentificationType();
    }

}
