/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package epsos.ccd.gnomon.xslt;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EpsosXSLTransformer {

    private static final String MAIN_XSLT = "/resources/cda.xsl";
    private static final String STANDARD_XSLT = "/resources/def_cda.xsl";
    private static final Logger logger = LoggerFactory.getLogger(EpsosXSLTransformer.class);
    private Path path = Paths.get(System.getenv("EPSOS_PROPS_PATH"), "EpsosRepository");

    /**
     * @param xml cda xml
     * @return
     */
    public String transformUsingStandardCDAXsl(String xml) {
        return transform(xml, "en-US", null, path, true, false, STANDARD_XSLT);
    }

    /**
     * @param xml
     * @param lang
     * @param actionpath
     * @param path
     * @param export
     * @param shownarrative
     * @param xsl
     * @return
     */
    private String transform(String xml, String lang, String actionpath, Path path, boolean export,
                             boolean shownarrative, String xsl) {

        String output = "";
        checkLanguageFiles();
        logger.info("Trying to transform xml using action path for dispensation '{}' and repository path '{}'", actionpath, path);

        try {
            URL xslUrl = this.getClass().getResource(xsl);
            //Paths.get(getClass().getClassLoader().getResource("fileTest.txt").toURI());
            //InputStream xslStream = this.getClass().getResourceAsStream(xsl);
            InputStream xslStream = getClass().getClassLoader().getResourceAsStream("classpath*:" + xsl);

            String systemId = xslUrl.toExternalForm();
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            logger.info("XSL: '{}'", xsl);
            logger.info("Main XSL: '{}'", xslUrl);
            logger.info("SystemID: '{}'", systemId);
            logger.info("Path: '{}'", path);
            logger.info("Lang: '{}'", lang);
            logger.info("Show Narrative: '{}'", String.valueOf(shownarrative));

            StreamSource xmlSource = new StreamSource(new StringReader(xml));
            StreamSource xslSource = new StreamSource(xslStream);

            xslSource.setSystemId(systemId);

            //TransformerFactory transformerFactory = TransformerFactory.newInstance();
            TransformerFactory transformerFactory = net.sf.saxon.TransformerFactoryImpl.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslSource);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setParameter("epsosLangDir", path);
            //transformer.setParameter("epsosLangDir", xslUrl);
            transformer.setParameter("userLang", lang);
            transformer.setParameter("shownarrative", String.valueOf(shownarrative));

            if (StringUtils.isNotBlank(actionpath)) {
                transformer.setParameter("actionpath", actionpath);
                transformer.setParameter("allowDispense", "true");
            } else {
                transformer.setParameter("allowDispense", "false");
            }

            File resultFile = File.createTempFile("Streams", ".html");
            Result result = new StreamResult(resultFile);
            logger.info("Temp file goes to : '{}'", resultFile.getAbsolutePath());
            transformer.transform(xmlSource, result);
            output = readFile(resultFile.getAbsolutePath());
            if (!export) {
                logger.debug("Delete temp file '{}'", resultFile.getAbsolutePath());
                resultFile.delete();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return output;
    }

    /**
     * @param xml            the source cda xml file
     * @param lang           the language you want the labels, value set to be displayed
     * @param actionpath     the url that you want to post the dispensation form. Leave it
     *                       empty to not allow dispensation
     * @param repositorypath the path of the epsos repository files
     * @param export         whether to export file to temp folder or not
     * @return the cda document in html format
     */
    private String transform(String xml, String lang, String actionpath, Path path, boolean export) {
        return transform(xml, lang, actionpath, path, export, true, MAIN_XSLT);
    }

    /**
     * @param xml            the source cda xml file
     * @param lang           the language you want the labels, value set to be displayed
     * @param actionpath     the url that you want to post the dispensation form
     * @param repositorypath the path of the epsos repository files
     * @return the cda document in html format
     */
    public String transform(String xml, String lang, String actionpath, Path repositoryPath) {
        return transform(xml, lang, actionpath, repositoryPath, false);
    }

    /**
     * This method uses the epsos repository files from user home directory
     *
     * @param xml        the source cda xml file
     * @param lang       the language you want the labels, value set to be displayed
     * @param actionpath the url that you want to post the dispensation form
     * @return the cda document in html format
     */
    public String transform(String xml, String lang, String actionpath) {
        return transform(xml, lang, actionpath, path, false);
    }

    /* hides links that exist in html */
    public String transformForPDF(String xml, String lang, boolean export) {
        return transform(xml, lang, "", path, export, false, MAIN_XSLT);
    }

    /**
     * This method uses the epsos repository files from user home directory and
     * outputs the transformed xml to the temp file without deleting it
     *
     * @param xml        the source cda xml file
     * @param lang       the language you want the labels, value set to be displayed
     * @param actionpath the url that you want to post the dispensation form
     * @return the cda document in html format
     */
    public String transformWithOutputAndUserHomePath(String xml, String lang, String actionpath) {
        return transform(xml, lang, actionpath, path, true);
    }

    /**
     * This method uses the epsos repository files from user home directory and
     * outputs the transformed xml to the temp file without deleting it
     *
     * @param xml            the source cda xml file
     * @param lang           the language you want the labels, value set to be displayed
     * @param actionpath     the url that you want to post the dispensation form
     * @param repositorypath the path of the epsos repository files
     * @return the cda document in html format
     */
    public String transformWithOutputAndDefinedPath(String xml, String lang, String actionpath, Path repositoryPath) {
        return transform(xml, lang, actionpath, repositoryPath, true);
    }

    public String readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.toURI())));
    }

    private void checkLanguageFiles() {

        final String filesNeeded[] = {"epSOSDisplayLabels.xml", "NullFlavor.xml", "SNOMEDCT.xml",
                "UCUMUnifiedCodeforUnitsofMeasure.xml"};
        // get User Path
        try {
            if (new File(path.toUri()).exists())
                for (int i = 0; i < filesNeeded.length; i++) {
                    if (!new File(Paths.get(path.toString(), filesNeeded[i]).toUri()).exists())
                        throw new Exception("File " + filesNeeded[i] + " doesn't exists");
                }
            else
                throw new Exception("Folder " + path.toString() + " doesn't exists");
        } catch (Exception e) {
            logger.error("FATAL ERROR: " + e.getMessage());
            System.exit(-1);
        }
    }
}
