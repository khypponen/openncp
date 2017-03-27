package com.gnomon.epsos.filter;

import com.gnomon.epsos.service.Utils;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class AuthenticationService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("AuthenticationService");

    public boolean authenticate(String authCredentials) throws UnsupportedEncodingException, ParseException {
        logger.info("try to authencticate with: " + authCredentials);
        if (null == authCredentials) {
            return false;
        }
        //long auth = HelperUtil.getUserFromTicket(authCredentials);
        boolean verifyTicket = Utils.verifyTicket(authCredentials);
        logger.info("VERIFY TICKET: " + verifyTicket);
        return verifyTicket;
    }
}
