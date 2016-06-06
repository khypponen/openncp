package com.gnomon.epsos.service;

import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.model.Ticket;
import com.gnomon.epsos.rest.EpsosRestService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Utils {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Utils");

    public static String getDocumentAsXml(org.w3c.dom.Document doc, boolean header) {
        String resp = "";
        try {
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            String omit = "yes";
            if (header) {
                omit = "no";
            } else {
                omit = "yes";
            }
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit);
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            //transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
            // we want to pretty format the XML output
            // note : this is broken in jdk1.5 beta!
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //
            java.io.StringWriter sw = new java.io.StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            resp = sw.toString();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return resp;
    }

    private static final String password = "dow98u983u29ejoia9832983927jdodj0834804930jdfkfjlsfsjojojfd9";
    private static String salt;
    private static int pswdIterations = 65536;
    private static int keySize = 256;
    private byte[] ivBytes;
    public static final String DATE_TIME_FORMAT_FULL = "ddMMyyyyHHmmss";

    public static boolean verifyTicket(String ticket) {
        String encryptionKey = MyServletContextListener.getEncryptionKey();
        log.info("ENCRYPTION KEY: " + encryptionKey);
        boolean verified = false;
        try {
            log.info("PRE  String to be decrypted is " + ticket);
            ticket = URLDecoder.decode(ticket);
            log.info("POST String to be decrypted is " + ticket);
            String decrypted = "";
            try {
                decrypted = decrypt(ticket, encryptionKey);
            } catch (Exception ex) {
                log.error("An error has occured", ex);
                java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
            log.info("### Decrypted String is " + decrypted);
            if (Validator.isNotNull(decrypted)) {
                Ticket ticket1 = StringToTicket(URLDecoder.decode(decrypted));
                log.info("Date from ticket: " + ticket1.getCreatedDate());
                log.info("Email from ticket: " + ticket1.getEmailAddress());
                DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT_FULL);
                Date createdDate = format.parse(ticket1.getCreatedDate() + "");
                DateTime cd = new DateTime(createdDate);
                DateTime now = new DateTime();
                long msec = Seconds.secondsBetween(cd, now).getSeconds();
                log.info("Seconds between " + now + " and " + cd + " are " + msec);
                if (msec < 100) {
                    verified = true;
                }
            }
        } catch (Exception e) {
            log.error("Problem validating ticket", e);
            log.error("Error verifying ticket");
        }
        return verified;
    }

    public boolean isValidUser(String user) throws Exception {
        Utils utils = new Utils();
        String userId_ = user; //URLDecoder.decode(user);
        int userId = Integer.parseInt(Utils.decrypt(userId_));
        log.info("#### Encrypted userid " + user);
        log.info("#### Decrypted userid " + userId);
        return Validator.isNotNull(UserLocalServiceUtil.getUser(userId));
    }

    public User getLiferayUserUnEncrypted(String user) throws Exception {
        Utils utils = new Utils();
        User liferayUser = UserLocalServiceUtil.getUserByScreenName(EpsosRestService.companyId, user);
        if (Validator.isNotNull(liferayUser)) {
            return liferayUser;
        }
        return null;
    }

    public User getLiferayUser(String user) throws Exception {
        Utils utils = new Utils();
        String userId_ = user; //URLDecoder.decode(user);
        int userId = Integer.parseInt(Utils.decrypt(userId_));
        User liferayUser = UserLocalServiceUtil.getUser(userId);
        if (Validator.isNotNull(liferayUser)) {
            return liferayUser;
        }
        return null;
    }

    public String encrypt_(String plainText) throws Exception {
        log.info("String to be encrypted is " + plainText);
        //get salt
        salt = generateSalt();
        byte[] saltBytes = salt.getBytes("UTF-8");

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        //encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        String ret = new Base64().encodeAsString(encryptedTextBytes);
        return ret;
    }

    @SuppressWarnings("static-access")
    public String decrypt_(String encryptedText) throws Exception {
        log.info("String to be decrypted: " + encryptedText);
        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (BadPaddingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        String ret = new String(decryptedTextBytes);
        return ret;
    }

    private static final String ALGO = "AES";
    private static final byte[] keyValue
            = new byte[]{'T', 'h', '1', 'B', 'e', 's', 't',
                'S', '1', '@', 'r', 'e', '$', 'K', '2', 'y'};

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    private static Key generateKeyWithKey(byte[] encryptionKey) throws Exception {
        Key key = new SecretKeySpec(encryptionKey, ALGO);
        return key;
    }

    public static String decrypt(String encryptedData, String encryptionKey) throws Exception {
        Key key = null;
        if (Validator.isNotNull(encryptionKey)) {
            byte[] bytes = encryptionKey.getBytes();
            key = generateKeyWithKey(bytes);
        } else {
            key = generateKey();
        }
        String decryptedValue = "";
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            //byte[] decordedValue = Base64.decode(encryptedData);
            byte[] decordedValue = Hex.decodeHex(encryptedData.toCharArray());
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (Exception e) {
            log.error("Error decrypting: " + encryptedData + " " + e.getMessage());
        }
        return decryptedValue;
    }

    public static String decrypt(String encryptedData) {
        String decryptedValue = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (Exception e) {
            log.error("Error decrypting: " + encryptedData + " " + e.getMessage());
        }
        return decryptedValue;
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }

    public static Ticket StringToTicket(String key) {
        String[] val = key.split("@@@");
        Ticket ticket = new Ticket();
        ticket.setEmailAddress(val[0]);
        ticket.setCreatedDate(val[1]);
        return ticket;
    }

    public static Ticket createTicket(long userId) throws PortalException, SystemException {
        Ticket ticket = new Ticket();
        Date d = new Date();
        String date = formatDate(d);
        User user = UserLocalServiceUtil.getUser(userId);
        String tick = userId + "@@@" + user.getEmailAddress() + "@@@" + date;

        log.info("String to be encrypted is " + tick);
        String encrypted = "";
        try {
            encrypted = encrypt(tick);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Encrypted String is " + encrypted);
        ticket.setCreatedDate(formatDate(new Date()));
        ticket.setEmailAddress(user.getEmailAddress());
        ticket.setTicket(encrypted);
        ticket.setUserId(userId);
        return ticket;
    }

    public static long getUserFromTicket(String ticket) {
        log.info("String to be decrypted is " + ticket);
        String decrypted = "";
        try {
            decrypted = decrypt(ticket);
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Decrypted String is " + decrypted);
        Ticket ticket1 = StringToTicket(decrypted);

        log.info("Username is " + ticket1.getUserId() + " and date is " + ticket1.getCreatedDate());
        return ticket1.getUserId();
    }

    public static boolean verifyTicket(String ticket, String username) {
        log.info("String to be decrypted is " + ticket);
        String decrypted = "";
        try {
            decrypted = decrypt(ticket);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("Decrypted String is " + decrypted);
        Ticket ticket1 = StringToTicket(decrypted);

        long userId = ticket1.getUserId();
        log.info("USER FROM TICKET IS " + userId + " and user wants to be verified is " + username);
        boolean ret = false;
        try {
            User user = UserLocalServiceUtil.getUser(userId);
            log.info("USER FROM TICKET IS " + user.getScreenName());
            if (user.getScreenName().equalsIgnoreCase(username)) {
                ret = true;
            }
        } catch (Exception e) {
            log.error("Error finding user for user inside ticket");
        }
//            String date = ticket1.getCreatedDate();
//            log.info("Userid is " + userId + " and date is " + date);
//            Date now = new Date();
//            DateTimeFormatter formatter = DateTimeFormat.forPattern(HelperUtil.DATE_TIME_FORMAT);
//            DateTime d1 = formatter.parseDateTime(date);
//            DateTime d2 = new DateTime(now);
//            log.info(d2 + " " + d1);
//            int mins = Minutes.minutesBetween(d1,d2).getMinutes();
//
//            log.info("Minutes between ticket and now is:" + mins);

        //if (userId==user && mins<120) ret=true;
        //if (userId==user) ret=true;
        return ret;
    }
    public static String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    public static String formatDate(Date date) {
        String formatted = "";
        if (date != null) {
            SimpleDateFormat dt1 = new SimpleDateFormat(DATE_TIME_FORMAT);
            dt1.setTimeZone(TimeZone.getTimeZone("EET"));

            formatted = dt1.format(date);
        }
        return formatted;
    }

    public static String transformDomToString(Document doc) throws TransformerConfigurationException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    public static Document readXml(StreamSource is) throws SAXException, IOException,
            ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());
        InputSource is2 = new InputSource();
        is2.setSystemId(is.getSystemId());
        is2.setByteStream(is.getInputStream());
        is2.setCharacterStream(is.getReader());

        return db.parse(is2);
    }
}

class NullResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
            IOException {
        return new InputSource(new StringReader(""));
    }
}
