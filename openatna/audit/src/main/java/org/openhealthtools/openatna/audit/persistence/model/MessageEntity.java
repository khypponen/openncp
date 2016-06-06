/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */


package org.openhealthtools.openatna.audit.persistence.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;


@Entity
@Table(name = "messages")
public class MessageEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private static DateFormat format = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:SS");

    private Long id;

    private Set<MessageParticipantEntity> messageParticipants = new HashSet<MessageParticipantEntity>();

    private Set<MessageSourceEntity> messageSources = new HashSet<MessageSourceEntity>();

    private Set<MessageObjectEntity> messageObjects = new HashSet<MessageObjectEntity>();

    private EventIdCodeEntity eventId;
    private Set<EventTypeCodeEntity> eventTypeCodes = new HashSet<EventTypeCodeEntity>();
    private String eventActionCode;
    private Date eventDateTime;
    private Integer eventOutcome;
    private String sourceAddress;
    private byte[] messageContent = new byte[0];

    public MessageEntity() {
    }

    public MessageEntity(EventIdCodeEntity code, Integer eventOutcome) {
        this.eventId = code;
        this.eventOutcome = eventOutcome;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_types_codes")
    public Set<EventTypeCodeEntity> getEventTypeCodes() {
        return eventTypeCodes;
    }

    public void addEventTypeCode(EventTypeCodeEntity code) {
        getEventTypeCodes().add(code);
    }

    public void setEventTypeCodes(Set<EventTypeCodeEntity> eventTypeCodeEntities) {
        this.eventTypeCodes = eventTypeCodeEntities;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public EventIdCodeEntity getEventId() {
        return eventId;
    }

    public void setEventId(EventIdCodeEntity eventId) {
        this.eventId = eventId;
    }

    public String getEventActionCode() {
        return eventActionCode;
    }

    public void setEventActionCode(String eventActionCode) {
        this.eventActionCode = eventActionCode;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @Temporal(TemporalType.TIMESTAMP)
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_mparticipants")
    public Set<MessageParticipantEntity> getMessageParticipants() {
        return messageParticipants;
    }

    public void setMessageParticipants(Set<MessageParticipantEntity> messageParticipants) {
        this.messageParticipants = messageParticipants;
    }

    public void addMessageParticipant(MessageParticipantEntity entity) {
        getMessageParticipants().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_msources")
    public Set<MessageSourceEntity> getMessageSources() {
        return messageSources;
    }

    public void setMessageSources(Set<MessageSourceEntity> messageSources) {
        this.messageSources = messageSources;
    }

    public void addMessageSource(MessageSourceEntity entity) {
        getMessageSources().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_mobjects")
    public Set<MessageObjectEntity> getMessageObjects() {
        return messageObjects;
    }

    public void setMessageObjects(Set<MessageObjectEntity> messageObjects) {
        this.messageObjects = messageObjects;
    }

    public void addMessageObject(MessageObjectEntity entity) {
        getMessageObjects().add(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageEntity)) {
            return false;
        }

        MessageEntity that = (MessageEntity) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (eventActionCode != null ? !eventActionCode.equals(that.eventActionCode) : that.eventActionCode != null) {
            return false;
        }
        if (eventDateTime != null ? !eventDateTime.equals(that.eventDateTime) : that.eventDateTime != null) {
            return false;
        }
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) {
            return false;
        }
        if (eventOutcome != null ? !eventOutcome.equals(that.eventOutcome) : that.eventOutcome != null) {
            return false;
        }
        if (getEventTypeCodes() != null ? !getEventTypeCodes().equals(that.getEventTypeCodes())
                : that.getEventTypeCodes() != null) {
            return false;
        }
        if (getMessageObjects() != null ? !getMessageObjects().equals(that.getMessageObjects())
                : that.getMessageObjects() != null) {
            return false;
        }
        if (getMessageParticipants() != null ? !getMessageParticipants().equals(that.getMessageParticipants())
                : that.getMessageParticipants() != null) {
            return false;
        }
        if (getMessageSources() != null ? !getMessageSources().equals(that.getMessageSources())
                : that.getMessageSources() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getMessageParticipants() != null ? getMessageParticipants().hashCode() : 0;
        result = 31 * result + (getMessageSources() != null ? getMessageSources().hashCode() : 0);
        result = 31 * result + (getMessageObjects() != null ? getMessageObjects().hashCode() : 0);
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (getEventTypeCodes() != null ? getEventTypeCodes().hashCode() : 0);
        result = 31 * result + (eventActionCode != null ? eventActionCode.hashCode() : 0);
        result = 31 * result + (eventDateTime != null ? eventDateTime.hashCode() : 0);
        result = 31 * result + (eventOutcome != null ? eventOutcome.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", event id:")
                .append(getEventId())
                .append(", action=")
                .append(getEventActionCode())
                .append(", outcome=")
                .append(getEventOutcome())
                .append(", time stamp=")
                .append(format.format(getEventDateTime()))
                .append(", event types=")
                .append(getEventTypeCodes())
                .append(", audit sources=")
                .append(getMessageSources())
                .append(", active participants=")
                .append(getMessageParticipants())
                .append(", participant objects=")
                .append(getMessageObjects())
                .append("]")
                .toString();
    }

    @Column(length=65535)
	public byte[] getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(byte[] messageContent) {
		this.messageContent = messageContent;
	}


}
