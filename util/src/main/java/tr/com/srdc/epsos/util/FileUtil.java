/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik
 * Tic. Ltd. Sti. <epsos@srdc.com.tr>
 *
 * This file is part of SRDC epSOS NCP.
 *
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SRDC epSOS NCP is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static final String UTF_8 = "UTF-8";

    public static byte[] getBytesFromFile(String fileURI) throws IOException {
        File file = new File(fileURI);
        return getBytes(file);
    }

    private static String readWholeFile(URI uri) throws IOException {
        BufferedReader buf;
        StringBuffer rules = new StringBuffer();
        FileInputStream fis = new FileInputStream(new File(uri));
        InputStreamReader inputStreamReader = new InputStreamReader(fis, UTF_8);
        buf = new BufferedReader(inputStreamReader);
        String temp;
        while ((temp = buf.readLine()) != null) {
            rules.append(temp).append("\n");
        }
        buf.close();
        return rules.toString();
    }

    public static String readWholeFile(String filePath) throws IOException {
        BufferedReader buf;
        StringBuffer rules = new StringBuffer();
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, UTF_8);
        buf = new BufferedReader(inputStreamReader);
        String temp;
        while ((temp = buf.readLine()) != null) {
            rules.append(temp).append("\n");
        }
        buf.close();
        return rules.toString();
    }

    public static List<String> readFileLines(String filePath) throws IOException {

        BufferedReader buf;
        List<String> rules = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, UTF_8);
        buf = new BufferedReader(inputStreamReader);

        String temp;
        while ((temp = buf.readLine()) != null) {
            rules.add(temp);
        }
        buf.close();

        return rules;
    }

    public static String readWholeFile(File file) throws IOException {
        BufferedReader buf;
        StringBuffer rules = new StringBuffer();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, UTF_8);
        buf = new BufferedReader(inputStreamReader);
        String temp;
        while ((temp = buf.readLine()) != null) {
            rules.append(temp).append("\n");
        }
        buf.close();
        return rules.toString();
    }

    public static String readWholeFile(String filePath, String encoding) throws IOException {
        BufferedReader buf;
        StringBuffer rules = new StringBuffer();
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, encoding);
        buf = new BufferedReader(inputStreamReader);
        String temp;
        while ((temp = buf.readLine()) != null) {
            rules.append(temp).append("\n");
        }
        buf.close();
        return rules.toString();
    }

    public static byte[] readFromURI(URI uri) throws IOException {
        if (uri.toString().contains("http:")) {
            URL url = uri.toURL();
            URLConnection urlConnection = url.openConnection();
            int length = urlConnection.getContentLength();
            System.out.println("length of content in URL = " + length);
            if (length > -1) {
                byte[] pureContent = new byte[length];
                DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
                dis.readFully(pureContent, 0, length);
                dis.close();

                return pureContent;
            } else {
                throw new IOException("Unable to determine the content-length of the document pointed at " + url.toString());
            }
        } else {
            return readWholeFile(uri).getBytes(UTF_8);
        }
    }

    private static byte[] getBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);
		//logger.debug("\nFileInputStream is " + file);

        // Get the size of the file
        long length = file.length();
        //logger.debug("Length of " + file + " is " + length + "\n");
        if (length > Integer.MAX_VALUE) {
            System.out.println("File is too large to process");
            return null;
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length)
                && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {

            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    /**
     * Constructs a new file with the given content and filePath. If a file with
     * the same name already exists, simply overwrites the content.
     *
     * @author Gunes
     * @param filePath
     * @param content : expressed as byte[]
     * @throws IOException : [approved by gunes] when the file cannot be
     * created. possible causes: 1) invalid filePath, 2) already existing file
     * cannot be deleted due to read-write locks
     */
    public static void constructNewFile(String filePath, byte[] content) throws IOException {
        File file = new File(filePath);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content);
        fos.close();
    }

    public static void writeToFile(File file, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(fos, UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(outStreamWriter);
        bufferedWriter.write(content);
        bufferedWriter.flush();
        bufferedWriter.close();
        fos.close();
    }
}
