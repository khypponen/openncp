/*
 * Copyright (c) 2009-2010 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageSourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Feb 6, 2010: 7:11:49 PM
 */

public class StringifiedMessage {

	public static Logger logger = Logger.getLogger(StringifiedMessage.class);
	
    private Long id;

    private Set<MessageParticipantEntity> messageParticipants = new HashSet<MessageParticipantEntity>();
    private Set<MessageSourceEntity> messageSources = new HashSet<MessageSourceEntity>();
    private Set<MessageObjectEntity> messageObjects = new HashSet<MessageObjectEntity>();
    private EventIdCodeEntity eventId;
    private Set<EventTypeCodeEntity> eventTypeCodes = new HashSet<EventTypeCodeEntity>();
    private String eventActionCode = "";
    private Date eventDateTime;
    private Integer eventOutcome;
    private String sourceAddress = "";
    private String messageContent = "";
    
    public StringifiedMessage(MessageEntity messageEntity) {
    	
        this.id = messageEntity.getId();
        
        if (messageEntity.getMessageContent() != null) {
            try {
                this.messageContent = new String(messageEntity.getMessageContent(), "UTF-8");
                this.messageContent = this.messageContent.replaceAll("<", "&lt;");
            } catch (UnsupportedEncodingException e) {

            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        this.messageParticipants = messageEntity.getMessageParticipants();
        this.messageSources = messageEntity.getMessageSources();
        this.messageObjects = messageEntity.getMessageObjects();
        this.eventId = messageEntity.getEventId();
        this.eventTypeCodes = messageEntity.getEventTypeCodes();
        this.eventActionCode = messageEntity.getEventActionCode();
        this.eventDateTime = messageEntity.getEventDateTime();
        this.eventOutcome = messageEntity.getEventOutcome();
        this.sourceAddress = messageEntity.getSourceAddress();
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Set<MessageParticipantEntity> getMessageParticipants() {
		return messageParticipants;
	}


	public void setMessageParticipants(
			Set<MessageParticipantEntity> messageParticipants) {
		this.messageParticipants = messageParticipants;
	}


	public Set<MessageSourceEntity> getMessageSources() {
		return messageSources;
	}


	public void setMessageSources(Set<MessageSourceEntity> messageSources) {
		this.messageSources = messageSources;
	}


	public Set<MessageObjectEntity> getMessageObjects() {
		return messageObjects;
	}


	public void setMessageObjects(Set<MessageObjectEntity> messageObjects) {
		this.messageObjects = messageObjects;
	}


	public EventIdCodeEntity getEventId() {
		return eventId;
	}


	public void setEventId(EventIdCodeEntity eventId) {
		this.eventId = eventId;
	}


	public Set<EventTypeCodeEntity> getEventTypeCodes() {
		return eventTypeCodes;
	}


	public void setEventTypeCodes(Set<EventTypeCodeEntity> eventTypeCodes) {
		this.eventTypeCodes = eventTypeCodes;
	}


	public String getEventActionCode() {
		return eventActionCode;
	}


	public void setEventActionCode(String eventActionCode) {
		this.eventActionCode = eventActionCode;
	}


	public Date getEventDateTime() {
		return eventDateTime;
	}


	public void setEventDateTime(Date eventDateTime) {
		this.eventDateTime = eventDateTime;
	}


	public Integer getEventOutcome() {
		return eventOutcome;
	}


	public void setEventOutcome(Integer eventOutcome) {
		this.eventOutcome = eventOutcome;
	}


	public String getSourceAddress() {
		return sourceAddress;
	}


	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}


	public String getMessageContent() {
		return messageContent;
	}


	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}   
}
