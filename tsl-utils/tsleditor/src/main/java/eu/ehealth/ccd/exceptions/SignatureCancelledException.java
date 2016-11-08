/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.exceptions;

/**
 * Custom exception to be thrown when the scheme operator cancels the
 * process of applying a qualified signature over the SMP files information
 * 
 * @author jgoncalves
 */
public class SignatureCancelledException extends Exception {

    /**
     * Creates a new instance of <code>SignatureCancelledException</code>
     * without detail message.
     */
    public SignatureCancelledException() {
    }

    /**
     * Constructs an instance of <code>SignatureCancelledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SignatureCancelledException(String msg) {
        super(msg);
    }

    public SignatureCancelledException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public SignatureCancelledException(Throwable thrwbl) {
        super(thrwbl);
    }
}
