/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.com.srdc.epsos.data.model;

/**
 *
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class PatientId {

    private String root = null;
    private String extension = null;

    public PatientId() {
    }

    public PatientId(String root, String extension) {
        setRoot(root);
        setExtension(extension);
    }

    /**
     *
     * @param root
     * @param extension
     * @return root + "^^^&" + extension + "&ISO"
     */
    public static String createFullId(final String root, final String extension) {
        return extension + "^^^&" + root + "&ISO";
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(final String root) {
        this.root = root;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }

    public String getfullId() {
        return createFullId(root, extension);
    }

    @Override
    public String toString() {
        return getfullId();
    }
}
