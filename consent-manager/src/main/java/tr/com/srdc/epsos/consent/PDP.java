/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.consent;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.consent.db.PatientDBConnector;
import urn.oasis.names.tc.xacml3.AllOfType;
import urn.oasis.names.tc.xacml3.AnyOfType;
import urn.oasis.names.tc.xacml3.AttributeType;
import urn.oasis.names.tc.xacml3.AttributesType;
import urn.oasis.names.tc.xacml3.DecisionType;
import urn.oasis.names.tc.xacml3.EffectType;
import urn.oasis.names.tc.xacml3.MatchType;
import urn.oasis.names.tc.xacml3.PolicyType;
import urn.oasis.names.tc.xacml3.RequestType;
import urn.oasis.names.tc.xacml3.ResponseType;
import urn.oasis.names.tc.xacml3.ResultType;
import urn.oasis.names.tc.xacml3.RuleType;
import urn.oasis.names.tc.xacml3.TargetType;

public class PDP {
	
	private static Logger logger = LoggerFactory.getLogger(PDP.class);

	public static ResponseType getDecision(RequestType request){
		ResponseType response = new ResponseType();
		ResultType result = new ResultType();
		response.getResult().add(result);
		result.setDecision(DecisionType.DENY);
		String patientId = "";
		for(AttributesType attributes : request.getAttributes()){
			for(AttributeType attribute : attributes.getAttribute()){
				if(attribute.getAttributeId().equals("patient-id")){
					patientId = (String) attribute.getAttributeValue().get(0).getContent().get(0);
				}
			}
		}
		
		ResultSet rs = null;
		try {			
			rs = PatientDBConnector.getStatement().executeQuery("SELECT * FROM consent WHERE patientId = '" + patientId + "'");
			if(rs.next()){
				int isGranted = rs.getInt("granted");
				if(isGranted == 0){
					return response;
				}
			} 
			else {
				return response;
			}
			
			String xacmlDocument = rs.getString("xacmlDocument");
			ByteArrayInputStream bais = new ByteArrayInputStream (xacmlDocument.getBytes());

			JAXBContext jc = JAXBContext.newInstance( "urn.oasis.names.tc.xacml3" );
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			PolicyType policy = (PolicyType) unmarshaller.unmarshal(bais);

			RuleType rule =  (RuleType) policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().get(0);
			TargetType target = rule.getTarget();
			AnyOfType anyOf = target.getAnyOf().get(0);
			for(AllOfType allOf : anyOf.getAllOf()){
				int i = 0;
				for(i = 0;i < allOf.getMatch().size();i++){
					MatchType match = allOf.getMatch().get(i);
					String matchId = match.getMatchId();
					String attrValue = (String)match.getAttributeValue().getContent().get(0);
					String attrDesignatorAttrId = match.getAttributeDesignator().getAttributeId();
					Object attrDesignatorValue = null;
					if(attrDesignatorAttrId.equals("urn:oasis:names:tc:xacml:1.0:environment:current-dateTime")){
						attrDesignatorValue = new Date();
					}else{
						for(AttributeType attributeType : request.getAttributes().get(0).getAttribute()){
							if(attributeType.getAttributeId().equals(attrDesignatorAttrId)){
								attrDesignatorValue = attributeType.getAttributeValue().get(0).getContent().get(0);
							}
						}
					}
					
					DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					  
					if(matchId.equals("urn:oasis:names:tc:xacml:1.0:function:string-equal")){
						if(!attrValue.equals(attrDesignatorValue)){
							break;
						}
					} 
					else if(matchId.equals("urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than")){
						Date date1 = formatter.parse(attrValue);
						Date date2 = (Date) attrDesignatorValue;
						if(!date1.before(date2)){
							break;
						}
					}
					else if(matchId.equals("urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than")){
						Date date1 = formatter.parse(attrValue);
						Date date2 = (Date) attrDesignatorValue;
						if(!date1.after(date2)){
							break;
						}
					}
				}
				
				if(i == allOf.getMatch().size()){
					if(rule.getEffect().equals(EffectType.DENY)){
						result.setDecision(DecisionType.DENY);
					}else if(rule.getEffect().equals(EffectType.PERMIT)){
						result.setDecision(DecisionType.PERMIT);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("", e);
		} catch (JAXBException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		} finally{
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("", e);
			}
		}		
		
		return response;
	}
}
