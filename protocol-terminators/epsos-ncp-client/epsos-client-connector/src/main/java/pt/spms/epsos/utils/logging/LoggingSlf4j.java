/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * Contact email: luis.pinto@iuz.pt or marcelo.fonseca@iuz.pt
 */
package pt.spms.epsos.utils.logging;

import org.slf4j.Logger;

/**
 * Provides auxiliary logging methods.
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class LoggingSlf4j {

    public static final String START = "Start";
    public static final String END = "End";
    public static final String ERROR = "Error";

    public static void start(Logger logger, String methodName) {
        logger.debug(methodName + " | " + START);
    }

    public static void start(Logger logger, String methodName, String msg) {
        logger.debug(methodName + " | " + START + " : " + msg);
    }

    public static void end(Logger logger, String methodName) {
        logger.debug(methodName + " | " + END);
    }

    public static void end(Logger logger, String methodName, String msg) {
        logger.debug(methodName + " | " + END + " : " + msg);
    }

    public static void error(Logger logger, String methodName) {
        logger.debug(methodName + " | " + ERROR);
    }

    public static void errorMsg(Logger logger, String methodName, String msg) {
        logger.debug(methodName + " | " + ERROR + " : " + msg);
    }

    private LoggingSlf4j() {
    }
}
