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

package org.openhealthtools.openatna.syslog;

/**
 * Syslog constants
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:30:03 AM
 * @date $Date:$ modified by $Author:$
 */

public class Constants {


    public static final char VERSION = '1';
    public static final String ENC_UTF8 = "UTF-8";
    public static final String ENC_UTF16LE = "UTF-16LE";
    public static final String ENC_UTF16BE = "UTF-16BE";
    public static final String ENC_UTF32LE = "UTF-32LE";
    public static final String ENC_UTF32BE = "UTF-32BE";
    public static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    public static final byte[] UTF16LE_BOM = {(byte) 0xFF, (byte) 0xFE};
    public static final byte[] UTF16BE_BOM = {(byte) 0xFE, (byte) 0xFF};
    public static final byte[] UTF32LE_BOM = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
    public static final byte[] UTF32BE_BOM = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
}
