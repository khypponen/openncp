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

package org.openhealthtools.openatna.audit.persistence.util;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.AtnaMessageObject;
import org.openhealthtools.openatna.anom.AtnaMessageParticipant;
import org.openhealthtools.openatna.anom.AtnaObject;
import org.openhealthtools.openatna.anom.AtnaObjectDetail;
import org.openhealthtools.openatna.anom.AtnaParticipant;
import org.openhealthtools.openatna.anom.AtnaSource;
import org.openhealthtools.openatna.anom.EventAction;
import org.openhealthtools.openatna.anom.EventOutcome;
import org.openhealthtools.openatna.anom.NetworkAccessPoint;
import org.openhealthtools.openatna.anom.ObjectDataLifecycle;
import org.openhealthtools.openatna.anom.ObjectDescription;
import org.openhealthtools.openatna.anom.ObjectType;
import org.openhealthtools.openatna.anom.ObjectTypeCodeRole;
import org.openhealthtools.openatna.anom.SopClass;
import org.openhealthtools.openatna.audit.persistence.model.DetailTypeEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageSourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDescriptionEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDetailEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.SopClassEntity;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;

/**
 * Converts between ANOM objects and persistable objects.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 6:16:59 PM
 * @date $Date:$ modified by $Author:$
 */

public class EntityConverter {

	public static Logger logger = Logger.getLogger(EntityConverter.class);
	
    private EntityConverter() {
    }

    public static MessageEntity createMessage(AtnaMessage message) {
    	
        CodeEntity code = createCode(message.getEventCode(), CodeEntity.CodeType.EVENT_ID);
        MessageEntity ent = new MessageEntity((EventIdCodeEntity) code,
                new Integer(message.getEventOutcome().value()));
        ent.setEventDateTime(message.getEventDateTime());
        if (message.getSourceAddress() != null) {
            ent.setSourceAddress(message.getSourceAddress());
        }
        if (message.getEventActionCode() != null) {
            ent.setEventActionCode(message.getEventActionCode().value());
        }
        ent.setEventOutcome(new Integer(message.getEventOutcome().value()));
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode atnaCode : codes) {
            EventTypeCodeEntity e = (EventTypeCodeEntity) createCode(atnaCode, CodeEntity.CodeType.EVENT_TYPE);
            ent.addEventTypeCode(e);
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            ent.addMessageObject(createMessageObject(object));
        }
        List<AtnaSource> sources = message.getSources();
        for (AtnaSource source : sources) {
            ent.addMessageSource(createMessageSource(source));
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        for (AtnaMessageParticipant participant : participants) {
            ent.addMessageParticipant(createMessageParticipant(participant));
        }
        if (message.getMessageContent() != null) {
            ent.setMessageContent(message.getMessageContent());
        }        
        return ent;
    }

    public static AtnaMessage createMessage(MessageEntity entity) {
        AtnaCode evtid = createCode(entity.getEventId());
        AtnaMessage msg = new AtnaMessage(evtid, EventOutcome.getOutcome(entity.getEventOutcome()));
        msg.setMessageId(entity.getId());
        msg.setEventDateTime(entity.getEventDateTime());
        if (entity.getSourceAddress() != null) {
            msg.setSourceAddress(entity.getSourceAddress());
        }
        if (entity.getEventActionCode() != null) {
            msg.setEventActionCode(EventAction.getAction(entity.getEventActionCode()));
        }
        Set<EventTypeCodeEntity> codes = entity.getEventTypeCodes();
        for (EventTypeCodeEntity code : codes) {
            AtnaCode ac = createCode(code);
            msg.addEventTypeCode(ac);
        }
        Set<MessageParticipantEntity> ps = entity.getMessageParticipants();
        for (MessageParticipantEntity p : ps) {
            msg.addParticipant(createMessageParticipant(p));
        }
        Set<MessageObjectEntity> os = entity.getMessageObjects();
        for (MessageObjectEntity o : os) {
            msg.addObject(createMessageObject(o));
        }
        Set<MessageSourceEntity> ss = entity.getMessageSources();
        for (MessageSourceEntity s : ss) {
            msg.addSource(createMessageSource(s));
        }
        return msg;
    }

    public static MessageParticipantEntity createMessageParticipant(AtnaMessageParticipant participant) {
        MessageParticipantEntity e = new MessageParticipantEntity();
        e.setParticipant(createParticipant(participant.getParticipant()));
        e.setUserIsRequestor(participant.isUserIsRequestor());
        if (participant.getNetworkAccessPointId() != null
                && participant.getNetworkAccessPointType() != null) {
            NetworkAccessPointEntity na = new NetworkAccessPointEntity();
            na.setIdentifier(participant.getNetworkAccessPointId());
            na.setType((short) participant.getNetworkAccessPointType().value());
            e.setNetworkAccessPoint(na);
        }
        return e;
    }

    public static AtnaMessageParticipant createMessageParticipant(MessageParticipantEntity entity) {
        AtnaParticipant ap = createParticipant(entity.getParticipant());
        AtnaMessageParticipant p = new AtnaMessageParticipant(ap);
        if (entity.isUserIsRequestor() != null) {
            p.setUserIsRequestor(entity.isUserIsRequestor());
        }
        if (entity.getNetworkAccessPoint() != null) {
            p.setNetworkAccessPointId(entity.getNetworkAccessPoint().getIdentifier());
            p.setNetworkAccessPointType(NetworkAccessPoint.getAccessPoint(
                    entity.getNetworkAccessPoint().getType()));
        }
        return p;
    }

    public static MessageObjectEntity createMessageObject(AtnaMessageObject object) {
        MessageObjectEntity e = new MessageObjectEntity();
        e.setObject(createObject(object.getObject()));
        if (object.getObjectQuery() != null && object.getObjectQuery().length > 0) {
            e.setObjectQuery(object.getObjectQuery());
        }
        if (object.getObjectDataLifeCycle() != null) {
            e.setObjectDataLifeCycle((short) object.getObjectDataLifeCycle().value());
        }
        List<AtnaObjectDetail> details = object.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            ObjectDetailEntity ent = new ObjectDetailEntity(detail.getType(), detail.getValue());
            e.addObjectDetail(ent);
        }
        return e;
    }

    public static AtnaMessageObject createMessageObject(MessageObjectEntity entity) {
        AtnaObject obj = createObject(entity.getObject());
        AtnaMessageObject o = new AtnaMessageObject(obj);
        if (entity.getObjectDataLifeCycle() != null) {
            o.setObjectDataLifeCycle(ObjectDataLifecycle.getLifecycle(entity.getObjectDataLifeCycle()));
        }
        if (entity.getObjectQuery() != null) {
            o.setObjectQuery(entity.getObjectQuery());
        }
        Set<ObjectDetailEntity> pairs = entity.getDetails();
        for (ObjectDetailEntity pair : pairs) {
            AtnaObjectDetail detail = new AtnaObjectDetail();
            detail.setType(pair.getType());
            detail.setValue(pair.getValue());
            o.addObjectDetail(detail);
        }
        return o;
    }

    public static MessageSourceEntity createMessageSource(AtnaSource source) {
        MessageSourceEntity e = new MessageSourceEntity();
        e.setSource(createSource(source));
        return e;
    }

    public static AtnaSource createMessageSource(MessageSourceEntity entity) {
        return createSource(entity.getSource());
    }


    public static ObjectEntity createObject(AtnaObject object) {
        ObjectEntity e = new ObjectEntity();
        e.setObjectId(object.getObjectId());
        e.setObjectName(object.getObjectName());
        e.setObjectSensitivity(object.getObjectSensitivity());
        ObjectIdTypeCodeEntity code = (ObjectIdTypeCodeEntity) createCode(object.getObjectIdTypeCode(),
                CodeEntity.CodeType.PARTICIPANT_OBJECT_ID_TYPE);
        e.setObjectIdTypeCode(code);
        if (object.getObjectTypeCode() != null) {
            e.setObjectTypeCode((short) object.getObjectTypeCode().value());
        }
        if (object.getObjectTypeCodeRole() != null) {
            e.setObjectTypeCodeRole((short) object.getObjectTypeCodeRole().value());
        }
        List<String> detailTypes = object.getObjectDetailTypes();
        for (String s : detailTypes) {
            e.addObjectDetailType(s);
        }
        List<ObjectDescription> descs = object.getDescriptions();
        for (ObjectDescription desc : descs) {
            ObjectDescriptionEntity ode = new ObjectDescriptionEntity();
            List<String> uids = desc.getMppsUids();
            for (String uid : uids) {
                ode.addMppsUid(uid);
            }
            List<String> accNums = desc.getAccessionNumbers();
            for (String accNum : accNums) {
                ode.addAccessionNumber(accNum);
            }
            List<SopClass> sc = desc.getSopClasses();
            for (SopClass sopClass : sc) {
                SopClassEntity sce = new SopClassEntity();
                sce.setSopId(sopClass.getUid());
                sce.setNumberOfInstances(sopClass.getNumberOfInstances());
                List<String> instances = sopClass.getInstanceUids();
                for (String instance : instances) {
                    sce.addInstanceUid(instance);
                }
                ode.getSopClasses().add(sce);
            }
            e.addObjectDescription(ode);
        }
        return e;
    }

    public static AtnaObject createObject(ObjectEntity entity) {
        AtnaCode code = createCode(entity.getObjectIdTypeCode());
        AtnaObject ao = new AtnaObject(entity.getObjectId(), code);
        ao.setObjectName(entity.getObjectName());
        ao.setObjectSensitivity(entity.getObjectSensitivity());
        if (entity.getObjectTypeCode() != null) {
            ao.setObjectTypeCode(ObjectType.getType(entity.getObjectTypeCode()));
        }
        if (entity.getObjectTypeCodeRole() != null) {
            ao.setObjectTypeCodeRole(ObjectTypeCodeRole.getRole(entity.getObjectTypeCodeRole()));
        }
        Set<DetailTypeEntity> types = entity.getObjectDetailTypes();
        for (DetailTypeEntity type : types) {
            ao.addObjectDetailType(type.getType());
        }
        Set<ObjectDescriptionEntity> descs = entity.getObjectDescriptions();
        for (ObjectDescriptionEntity desc : descs) {
            ObjectDescription od = new ObjectDescription();
            List<String> uids = desc.mppsUidsAsList();
            for (String uid : uids) {
                od.addMppsUid(uid);
            }
            List<String> accNums = desc.accessionNumbersAsList();
            for (String accNum : accNums) {
                od.addAccessionNumber(accNum);
            }
            Set<SopClassEntity> sops = desc.getSopClasses();
            for (SopClassEntity sop : sops) {
                SopClass sc = new SopClass();
                sc.setNumberOfInstances(sop.getNumberOfInstances());
                sc.setUid(sop.getSopId());
                List<String> instances = sop.instanceUidsAsList();
                for (String instance : instances) {
                    sc.addInstanceUid(instance);
                }
                od.addSopClass(sc);
            }
            ao.addObjectDescription(od);
        }
        return ao;
    }
/*

    public static List<String> mppsUidsAsList(ObjectDescriptionEntity entity) {
        String uids = entity.getMppsUids();
        String[] vals = uids.split(" ");
        List<String> ret = new ArrayList<String>();
        for (String val : vals) {
            if (val.length() > 0) {
                ret.add(val);
            }
        }
        return ret;
    }


    public static List<String> getInstanceUidsAsList(SopClassEntity entity) {
        String uids = entity.getInstanceUids();
        String[] vals = uids.split(" ");
        List<String> ret = new ArrayList<String>();
        for (String val : vals) {
            if (val.length() > 0) {
                ret.add(val);
            }
        }
        return ret;
    }*/


    public static SourceEntity createSource(AtnaSource source) {
        SourceEntity e = new SourceEntity();
        e.setSourceId(source.getSourceId());
        e.setEnterpriseSiteId(source.getEnterpriseSiteId());
        List<AtnaCode> codes = source.getSourceTypeCodes();
        for (AtnaCode code : codes) {
            SourceCodeEntity ent = (SourceCodeEntity) createCode(code,
                    CodeEntity.CodeType.AUDIT_SOURCE);
            e.addSourceTypeCode(ent);
        }
        return e;
    }

    public static AtnaSource createSource(SourceEntity entity) {
        AtnaSource as = new AtnaSource(entity.getSourceId());
        as.setEnterpriseSiteId(entity.getEnterpriseSiteId());
        Set<SourceCodeEntity> codes = entity.getSourceTypeCodes();
        for (SourceCodeEntity code : codes) {
            as.addSourceTypeCode(createCode(code));
        }
        return as;
    }

    public static ParticipantEntity createParticipant(AtnaParticipant participant) {
        ParticipantEntity e = new ParticipantEntity();
        e.setUserId(participant.getUserId());
        e.setUserName(participant.getUserName());
        e.setAlternativeUserId(participant.getAlternativeUserId());
        List<AtnaCode> codes = participant.getRoleIDCodes();
        for (AtnaCode code : codes) {
            ParticipantCodeEntity ent = (ParticipantCodeEntity) createCode(code,
                    CodeEntity.CodeType.ACTIVE_PARTICIPANT);
            e.addParticipantTypeCode(ent);
        }
        return e;
    }

    public static AtnaParticipant createParticipant(ParticipantEntity entity) {
        AtnaParticipant ap = new AtnaParticipant(entity.getUserId());
        ap.setUserName(entity.getUserName());
        ap.setAlternativeUserId(entity.getAlternativeUserId());
        Set<ParticipantCodeEntity> codes = entity.getParticipantTypeCodes();
        for (ParticipantCodeEntity code : codes) {
            ap.addRoleIDCode(createCode(code));
        }
        return ap;
    }

    public static CodeEntity createCode(AtnaCode code, CodeEntity.CodeType type) {
        switch (type) {
            case EVENT_TYPE:
                return new EventTypeCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case EVENT_ID:
                return new EventIdCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case ACTIVE_PARTICIPANT:
                return new ParticipantCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case AUDIT_SOURCE:
                return new SourceCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case PARTICIPANT_OBJECT_ID_TYPE:
                return new ObjectIdTypeCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            default:
                return null;
        }
    }

    public static AtnaCode createCode(CodeEntity code) {
        String type = AtnaCode.EVENT_ID;
        if (code instanceof EventTypeCodeEntity) {
            type = AtnaCode.EVENT_TYPE;
        } else if (code instanceof ParticipantCodeEntity) {
            type = AtnaCode.PARTICIPANT_ROLE_TYPE;
        } else if (code instanceof SourceCodeEntity) {
            type = AtnaCode.SOURCE_TYPE;
        } else if (code instanceof ObjectIdTypeCodeEntity) {
            type = AtnaCode.OBJECT_ID_TYPE;
        }
        AtnaCode ac = new AtnaCode(type, code.getCode(),
                code.getCodeSystem(),
                code.getCodeSystemName(),
                code.getDisplayName(),
                code.getOriginalText());
        return ac;
    }

    public static CodeEntity.CodeType getCodeType(AtnaCode code) {
        String type = code.getCodeType();
        if (type.equals(AtnaCode.EVENT_ID)) {
            return CodeEntity.CodeType.EVENT_ID;
        } else if (type.equals(AtnaCode.EVENT_TYPE)) {
            return CodeEntity.CodeType.EVENT_TYPE;
        } else if (type.equals(AtnaCode.OBJECT_ID_TYPE)) {
            return CodeEntity.CodeType.PARTICIPANT_OBJECT_ID_TYPE;
        } else if (type.equals(AtnaCode.PARTICIPANT_ROLE_TYPE)) {
            return CodeEntity.CodeType.ACTIVE_PARTICIPANT;
        } else if (type.equals(AtnaCode.SOURCE_TYPE)) {
            return CodeEntity.CodeType.AUDIT_SOURCE;
        }
        return null;
    }


}
