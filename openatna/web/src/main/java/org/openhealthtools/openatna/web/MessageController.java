/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.web;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.all.logging.AuditLoggerPluginManager;
import org.openhealthtools.openatna.anom.Timestamp;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 19, 2010: 9:58:57 AM
 */
public class MessageController extends MultiActionController {
    private static final Log log = LogFactory.getLog("org.openhealthtools.openatna.web.MessageController");

    private AuditLoggerPluginManager auditLoggerPluginManager = null;
    private MessageDao messageDao;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public AuditLoggerPluginManager getAuditLoggerPluginManager() {
    	return auditLoggerPluginManager;
	}

	public void setAuditLoggerPluginManager(AuditLoggerPluginManager auditLoggerPluginManager) {
		this.auditLoggerPluginManager = auditLoggerPluginManager;
	}

	public ModelAndView query(HttpServletRequest request, HttpServletResponse response, QueryBean queryBean) throws Exception {
        try {
	        ModelMap modelMap = new ModelMap();
	        int offset = 0;
	        String start = request.getParameter("start");
	        if (start != null) {
	            try {
	                QueryBean qb = (QueryBean) request.getSession().getAttribute("currBean");
	                if (qb != null) {
	                    queryBean = qb;
	                }
	                offset = Integer.parseInt(start);
	                if (offset < 0) {
	                    offset = 0;
	                }
	
	            } catch (NumberFormatException e) {
	                log.debug("error for start offset value=" + start, e);
	            }
	        }
	        modelMap.addAttribute("offset", offset);
	        queryBean.setStartOffset((offset) * queryBean.getMaxResults());
	        Query q = createQuery(queryBean);
	    	List<? extends MessageEntity> ment = messageDao.getByQuery(q);
	        List<StringifiedMessage> list = new ArrayList<StringifiedMessage>();
	        if (q.hasConditionals()) {
	        	if (ment != null) {
	            	getAuditLoggerPluginManager().handleAuditEvent(request, getBeanMap(queryBean), createIdList(ment));
	                for (MessageEntity ent : ment) {
	                    list.add(new StringifiedMessage(ent));
	                }
	            }
	        }
	        modelMap.addAttribute("messages", list);
	        modelMap.addAttribute("queryBean", queryBean);

            return new ModelAndView("messageForm", modelMap);
        } catch(Exception e) {
        	log.fatal(e.getMessage(), e);
        	ModelMap mm = new ModelMap();
        	mm.addAttribute("errorBean", new ErrorBean(e.getMessage()));
        	return new ModelAndView("errorPage", mm);
        }
    }

	private Map<String, String> getBeanMap(QueryBean qb) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<String, String> fields = BeanUtils.describe(qb);
		Map<String, String> ret = new HashMap<String, String>();
		
		 Iterator<Entry<String, String>> it = fields.entrySet().iterator();
		 while(it.hasNext()) {
			 Entry<String, String> entry = it.next();
			 if(!isEmpty(entry.getKey()) && !isEmpty(entry.getValue())) {
				 if(!"class".equals(entry.getKey())) { // class is a BeanUtils internal entry defining the Bean class
					 ret.put(entry.getKey(), entry.getValue());
				 }
			 }
		 }
		
		return ret;
	}
	
	private boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	private List<Long> createIdList(List<? extends MessageEntity> messageEntities) {
		List<Long> ids = new ArrayList<Long>();
		for(MessageEntity me : messageEntities) {
			ids.add( me.getId() );
		}
		return ids;
	}
	
    private String convertStars(String starred) {
        if (starred.startsWith("*")) {
            starred = "%" + starred.substring(1);
        }
        if (starred.endsWith("*")) {
            starred = starred.substring(0, starred.length() - 1) + "%";
        }
        return starred;
    }

    private Query.Conditional getConditionalForString(String val) {
        if (val.startsWith("*") || val.endsWith("*")) {
            return Query.Conditional.LIKE;
        }
        return Query.Conditional.EQUALS;
    }

    private Query createQuery(QueryBean bean) {
        Query query = new Query();

        if (bean.getEventIdCode() != null && bean.getEventIdCode().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventIdCode(), Query.Target.EVENT_ID_CODE);
        }
        if (bean.getEventOutcome() != null && bean.getEventOutcome().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, Integer.parseInt(bean.getEventOutcome()), Query.Target.EVENT_OUTCOME);
        }
        if (bean.getObjectId() != null && bean.getObjectId().length() > 0) {
            query.addConditional(getConditionalForString(bean.getObjectId()), convertStars(bean.getObjectId()), Query.Target.OBJECT_ID);
        }
        if (bean.getSourceId() != null && bean.getSourceId().length() > 0) {
            query.addConditional(getConditionalForString(bean.getSourceId()), convertStars(bean.getSourceId()), Query.Target.SOURCE_ID);
        }
        if (bean.getParticipantTypeCode() != null && bean.getParticipantTypeCode().length() > 0) {
            query.addConditional(getConditionalForString(bean.getParticipantTypeCode()), convertStars(bean.getParticipantTypeCode()),
                    Query.Target.PARTICIPANT_TYPE_CODE);
        }
        if (bean.getSourceTypeCode() != null && bean.getSourceTypeCode().length() > 0) {
            query.addConditional(getConditionalForString(bean.getSourceTypeCode()), convertStars(bean.getSourceTypeCode()),
                    Query.Target.SOURCE_TYPE_CODE);
        }
        if (bean.getObjectTypeCode() != null && bean.getObjectTypeCode().length() > 0) {
            query.addConditional(getConditionalForString(bean.getObjectTypeCode()), convertStars(bean.getObjectTypeCode()),
                    Query.Target.OBJECT_TYPE_CODE);
        }
        if (bean.getParticipantId() != null && bean.getParticipantId().length() > 0) {
            query.addConditional(getConditionalForString(bean.getParticipantId()), convertStars(bean.getParticipantId()),
                    Query.Target.PARTICIPANT_ID);
        }
        if (bean.getEventAction() != null && bean.getEventAction().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventAction(), Query.Target.EVENT_ACTION);
        }
        if (bean.getEventTime() != null && bean.getEventTime().length() > 0) {
            Date ts = Timestamp.parseToDate(bean.getEventTime());

            if (ts != null) {
                query.addConditional(Query.Conditional.EQUALS, ts, Query.Target.EVENT_TIME);
            }
        }
        Date startDate = null;
        if (bean.getStartDate() != null && bean.getStartDate().length() > 0) {
            String date = bean.getStartDate();
            try {
                startDate = format.parse(date + " " + bean.getStartHour() + ":" + bean.getStartMin());
                query.after(startDate);
            } catch (ParseException e) {

            }

        }
        if (bean.getEndDate() != null && bean.getEndDate().length() > 0) {
            String date = bean.getEndDate();
            try {
                Date dt = format.parse(date + " " + bean.getEndHour() + ":" + bean.getEndMin());
                if (startDate != null) {
                    if (dt.after(startDate)) {
                        query.before(dt);
                    }
                } else {
                    query.before(dt);
                }
            } catch (ParseException e) {

            }

        }
        if (bean.getEventTypeCode() != null && bean.getEventTypeCode().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventTypeCode(), Query.Target.EVENT_TYPE_CODE);
        }
        if (bean.getSourceAddress() != null && bean.getSourceAddress().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getSourceAddress(), Query.Target.SOURCE_ADDRESS);
        }

        query.setMaxResults(bean.getMaxResults());
        query.setStartOffset(bean.getStartOffset());
        query.orderAscending(Query.Target.ID);
        return query;
    }


}
