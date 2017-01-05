/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class DateUtil {

    public static final String TIME_DATE_FORMAT = "yyyyMMddHHmmss.SSSZZZZ";
    public static final String DATE_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final int randLimit = 10000;
    private static Random rand = new Random();
    public static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    // Returns the current time in the local time zone
    public static String getCurrentTimeLocal() {
        String currentTime = "";
        dateFormat.setTimeZone(TimeZone.getDefault());
        currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    // Returns the current time in the GMT
    public static String getCurrentTimeGMT() {
        String currentTime = "";
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    // Returns the current time in given format
    public static String getDateByDateFormat(String dateFormatString) {
        String currentTime = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    // Returns the given date in given format
    public static String getDateByDateFormat(String dateFormatString, Date date) {
        String currentTime = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        currentTime = dateFormat.format(date);
        return currentTime;
    }

    // uses local time zone
    public static String generateUniqueIdExtension() {
        String uniqueIdExt = DateUtil.getCurrentTimeLocal();
        uniqueIdExt += "." + rand.nextInt(randLimit);
        return uniqueIdExt;
    }

    public static Date parseDateFromString(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    public static DateTime GregorianCalendarToJodaTime(XMLGregorianCalendar cal) {
        DateTime dt = new DateTime(cal.toGregorianCalendar().getTime());
        System.out.println(dt);
        return dt;
    }
}
