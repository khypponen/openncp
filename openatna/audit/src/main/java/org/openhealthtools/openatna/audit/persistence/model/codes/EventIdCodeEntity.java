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

package org.openhealthtools.openatna.audit.persistence.model.codes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 11:27:32 AM
 * @date $Date:$ modified by $Author:$
 */

@Entity
@DiscriminatorValue("EVENT_ID")
public class EventIdCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public EventIdCodeEntity() {
        super(CodeType.EVENT_ID);
    }

    public EventIdCodeEntity(String code) {
        super(CodeType.EVENT_ID, code);
    }

    public EventIdCodeEntity(String code, String codeSystem) {
        super(CodeType.EVENT_ID, code, codeSystem);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName, displayName);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName,
                             String displayName, String originalText) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName, displayName, originalText);
    }

}

