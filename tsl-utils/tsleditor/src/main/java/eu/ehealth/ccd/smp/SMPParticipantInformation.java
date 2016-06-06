/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.smp;

import java.util.List;

/**
 * This class contains the percent-encoded URLs for a Participant ServiceGroup
 * and each one of its SignedServiceMetadata
 *
 * @author jgoncalves
 */
public class SMPParticipantInformation {
    private String serviceGroupURL;
    private List<String> serviceMetadataURLs;

    public SMPParticipantInformation() {
    }

    public SMPParticipantInformation(String serviceGroupURL, List<String> serviceMetadataURLs) {
        this.serviceGroupURL = serviceGroupURL;
        this.serviceMetadataURLs = serviceMetadataURLs;
    }

    public String getServiceGroupURL() {
        return serviceGroupURL;
    }

    public void setServiceGroupURL(String serviceGroupURL) {
        this.serviceGroupURL = serviceGroupURL;
    }

    public List<String> getServiceMetadataURLs() {
        return serviceMetadataURLs;
    }

    public void setServiceMetadataURLs(List<String> serviceMetadataURLs) {
        this.serviceMetadataURLs = serviceMetadataURLs;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceGroup: ");
        sb.append(serviceGroupURL);
        sb.append("\nSignedServiceMetadata:\n");
        for (String url : serviceMetadataURLs) {
            sb.append(url);
            sb.append("\n");
        }
        return sb.toString();
    }
}
