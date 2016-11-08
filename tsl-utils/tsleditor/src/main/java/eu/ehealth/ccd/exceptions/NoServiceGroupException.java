/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.exceptions;

/**
 * Custom exception to be thrown when there's no ServiceGroup for a certain
 * country in the SMP server
 * 
 * @author joao.cunha
 */
public class NoServiceGroupException extends Exception {

    /**
     * Creates a new instance of <code>NoServiceGroupException</code> without
     * detail message.
     */
    public NoServiceGroupException() {
    }

    /**
     * Constructs an instance of <code>NoServiceGroupException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoServiceGroupException(String msg) {
        super(msg);
    }

    public NoServiceGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoServiceGroupException(Throwable cause) {
        super(cause);
    }
}
