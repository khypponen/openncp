/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.migration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * This class is used to generate a user in the terminology server environment
 * @author bernamp
 */
public class CreateUserInTerminology {

    public static void main(String[] args) {

//        user.setName(username);
//        user.setEmail(mail);
//        user.setIsAdmin(isAdmin);
        // create Salt
        String salt = "eU3Tqw1Z6b4L";//generateRandomSalt();
        System.out.println(salt);

        // create random password if not set
//        String pwd = generateRandomPassword(8);
//        System.out.println(pwd);

        // activate user immediately
//        user.setActivationTime(new Date());
//        user.setEnabled(true);

//        System.out.println(pwd);
        // set hashed and salted password
        String saltedPWD = getSaltedPassword(getMD5("test"), salt, "test", 500);
        System.out.println(saltedPWD);
    }
    
       public static String generateRandomSalt() {
        return generateRandomPassword(12);
    }

    public static String generateRandomPassword(int Length) {
        char[] pw = new char[Length];
        int c = 'A';
        int r1 = 0;
        for (int i = 0; i < Length; i++) {
            r1 = (int) (Math.random() * 3);
            switch (r1) {
                case 0:
                    c = '0' + (int) (Math.random() * 10);
                    break;
                case 1:
                    c = 'a' + (int) (Math.random() * 26);
                    break;
                case 2:
                    c = 'A' + (int) (Math.random() * 26);
                    break;
            }
            pw[i] = (char) c;
        }

        return new String(pw);
    }

    public static String getSaltedPassword(String Password, String Salt, String Username, int IterationCount) {
        if (Salt == null || Salt.length() == 0) {
            return getMD5(Password);
        }

        String md5 = getMD5(Password + Salt + Username);

        for (int i = 0; i < IterationCount; ++i) {
            md5 = getMD5(md5);
        }
        //String md5 = MD5.getMD5(Password);

        return md5;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);             // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
