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
**/package epsos.ccd.gnomon.tsleditor.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class TSLNamespacePrefixMapper extends NamespacePrefixMapper {

	private static final Log LOG = LogFactory
			.getLog(TSLNamespacePrefixMapper.class);

	private static final Map<String, String> prefixes = new HashMap<String, String>();

	static {
		prefixes.put("http://uri.etsi.org/02231/v2#", "tsl");
		prefixes.put("http://www.w3.org/2000/09/xmldsig#", "ds");
		prefixes
				.put(
						"http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#",
						"ecc");
		prefixes.put("http://uri.etsi.org/01903/v1.3.2#", "xades");
		prefixes.put("http://uri.etsi.org/02231/v2/additionaltypes#", "tslx");
	}

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion,
			boolean requirePrefix) {
		LOG.debug("get preferred prefix: " + namespaceUri);
		LOG.debug("suggestion: " + suggestion);
		String prefix = prefixes.get(namespaceUri);
		if (null != prefix) {
			return prefix;
		}
		return suggestion;
	}
}
