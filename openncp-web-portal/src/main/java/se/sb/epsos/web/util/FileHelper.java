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
package se.sb.epsos.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreas
 */
public class FileHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
    
    public void saveToFile(String filename, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            fos.write(bytes);
        } catch (FileNotFoundException ex) {
            LOGGER.error("Failed to write to file",ex);
        } catch (IOException ex) {
            LOGGER.error("Failed to write to file",ex);
        } finally {
            try {
                LOGGER.debug("Wrote "+bytes.length + " bytes to file: "+filename);
                fos.close();
            } catch (IOException ex) {
                LOGGER.error("Failed to close file output stream",ex);
            }
        }
    }
    
    public static List<File> getAllFilesDirStartingWith(String dir, String startsWith) {
		File file = new File(dir);
		File[] children = file.listFiles();
		List<File> list = new ArrayList<File>();
		for (int i=0; i<children.length;i++) {
			if (children[i].getName().startsWith(startsWith)) {
				LOGGER.info(children[i].getName());
				list.add(children[i]);
			}
		}    	
    	return list;
    }
    
	public static boolean fileExists(String path) {
		if(path == null) {
			return false;
		}
		File f = new File(path);
		return f.isFile() && f.exists();
	}

	public static InputStream getResourceFromClassPathOrFileSystem(String path) throws FileNotFoundException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if(is != null) {
			return is;
		} else {
			return new FileInputStream(path);
		}
	}
}
