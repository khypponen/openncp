/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.pages.test;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author andreas
 */
class Coffeay {

    private String id;
    private String type;
    private String size;
    private byte[] bytes;
    private OutputStream nonSerializable;

    public Coffeay(String id, String type, String size, byte[] bytes) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.bytes = bytes;
        this.nonSerializable = new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
