/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.validation.datamodel.saml;

import eu.epsos.validation.datamodel.common.ObjectType;

/**
 * This enumerator gathers all the schematrons used in the Audit Messages Validator
 * at EVS Client.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
public enum AssertionSchematron {

    EPSOS_HCP_IDENTITY_ASSERTION("epSOS - HCP Identity Assertion"),
    EPSOS_TRC_ASSERTION("epSOS - TRC Assertion");
    private String name;

    public static AssertionSchematron checkSchematron(String model) {
        for (AssertionSchematron s : AssertionSchematron.values()) {
            if (model.equals(s.toString())) {
                return s;
            }
        }
        return null;
    }

    private AssertionSchematron(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }

    public ObjectType getObjectType() {
        return ObjectType.ASSERTION;
    }
}
