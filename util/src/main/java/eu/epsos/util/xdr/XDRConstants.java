/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012  SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.util.xdr;

import eu.epsos.util.IheConstants;

/**
 * Holds all the fixed properties used in the XDR Profile transactions.
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 * @author Luís Pinto<code> - luis.pinto@iuz.pt</code>
 */
public class XDRConstants {

    public static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    public static final String REGREP_LCM = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0";
    public static final String REGREP_EXT_IDENT = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier";
    public static final String REGREP_HAS_MEMBER = "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember";
    public static final String PROVIDE_AND_REGISTER_DOCUMENT_SET_REQ_STR = "ProvideAndRegisterDocumentSetRequest";
    public static final String REGISTRY_RESPONSE_STR = "RegistryResponse";
    public static final String DOCUMENT_RECIPIENT_SERVICE_STR = "DocumentRecipient_Service";
    public static final String DOC_RCP_PRVDANDRGSTDOCSETB_STR = "documentRecipient_ProvideAndRegisterDocumentSetB";
    public static final String SUBMISSION_SET_STATUS_STR = "SubmissionSetStatus";
    public static final String ORIGINAL_STR = "Original";

    public class SOAP_HEADERS {

        public static final String REQUEST_ACTION = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";
        public static final String NAMESPACE_REQUEST_LOCAL_PART = "documentRecipient_ProvideAndRegisterDocumentSetB";
        public static final String OM_NAMESPACE = "http://www.w3.org/2005/08/addressing";
        public static final String SECURITY_XSD = IheConstants.SOAP_HEADERS.SECURITY_XSD;
        public static final String ACTION_STR = "Action";
        public static final String MESSAGEID_STR = "MessageID";
        public static final String SECURITY_STR = "Security";
        public static final String MUST_UNDERSTAND_STR = "mustUnderstand";
    }

    public class LOG {

        /* ProvideAndRegister */
        public static final String OUTGOING_XDR_PROVIDEANDREGISTER_MESSAGE = "Outgoing XDR request message to NCP-A:";
        public static final String INCOMING_XDR_PROVIDEANDREGISTER_MESSAGE = "Incoming XDR response message from NCP-A:";
    }

    public class EXCEPTIONS {

        public static final String ERROR_JAXB_MARSHALLING = "Error in JAXB marshalling";
        public static final String UNABLE_CREATE_JAXB_CONTEXT = "Unable to create JAXBContext:";
    }

    public class EXTRINSIC_OBJECT {

        public static final String DATE_FORMAT = "yyyyMMddhhmmss";
        public static final String CREATION_TIME = "creationTime";
        /* Language Code */
        public static final String LANGUAGE_CODE_STR = "languageCode";
        public static final String LANGUAGE_CODE_VALUE = "en";
        /* Source Patient */
        public static final String SOURCE_PATIENT_ID = "sourcePatientId";
        /* HealthCareFacility Code */
        public static final String HEALTHCAREFACILITY_CODE_SCHEME = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
        public static final String HEALTHCAREFACILITY_CODE_VALUE = "1.0.3166.1";
        /* PracticeSetting Code */
        public static final String PRACTICE_SETTING_CODE_SCHEME = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
        public static final String PRACTICE_SETTING_CODE_NODEREPR = "epSOS Practice Setting Codes-Not Used";
        /* Confidentiality Code */
        public static final String CONFIDENTIALITY_CODE_SCHEME = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
        public static final String CONFIDENTIALITY_CODE_NODEREPR = "N";
        public static final String CONFIDENTIALITY_CODE_VALUE = "2.16.840.1.113883.5.25";
        public static final String CONFIDENTIALITY_CODE_STR = "Normal";
        /* Class Code */
        public static final String CLASS_CODE_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
        public static final String CLASS_CODE_VALUE = "2.16.840.1.113883.6.1";
        public static final String CLASS_CODE_ED_STR = "ePrescription";
        public static final String CLASS_CODE_CONS_STR = "Privacy Policy Acknowledgement Document";
        public static final String CLASS_CODE_HCER_STR = "Summarization of Episode Note";
        /* Event Code */
        public static final String EVENT_CODE_SCHEME = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
        public static final String EVENT_CODE_VALUE = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1";
        public static final String EVENT_CODING_SCHEME = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1";
        public static final String EVENT_CODE_NODE_REPRESENTATION_OPT_IN = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1.1";
        public static final String EVENT_CODE_NODE_REPRESENTATION_OPT_OUT = "1.3.6.1.4.1.12559.11.10.1.3.2.4.1.2";
        public static final String EVENT_CODE_NODE_NAME_OPT_IN = "Opt-in";
        public static final String EVENT_CODE_NODE_NAME_OPT_OUT = "Opt-out";
        
        /* XDSDocumentEntry.PatientID */
        public static final String XDSDOCENTRY_PATID_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
        public static final String XDSDOCENTRY_PATID_STR = "XDSDocumentEntry.patientId";
        /* XDSDocumentEntry.UniqueID */
        public static final String XDSDOC_UNIQUEID_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
        public static final String XDSDOC_UNIQUEID_STR = "XDSDocumentEntry.uniqueId";
        public static final String VERSION_INFO = "1.1";
        public static final String SRC_PATIENT_INFO_STR = "sourcePatientInfo";
        /* Type Code */
        public final class TypeCode {

            public static final String TYPE_CODE_SCHEME = IheConstants.TYPE_CODE_SCHEME;

            public final class EDispensation {
                    public static final String DISPLAY_NAME = "eDispensation";
                    public static final String NODE_REPRESENTATION = "60593-1";
                    public static final String CODING_SCHEME = "2.16.840.1.113883.6.1";
            }

            public final class Consent {
                    public static final String DISPLAY_NAME = "Privacy Policy Acknowledgement Document";
                    public static final String NODE_REPRESENTATION = "57016-8";
                    public static final String CODING_SCHEME = "2.16.840.1.113883.6.1";
                }
            
            public final class Hcer {
                    public static final String DISPLAY_NAME = "Summarization of Episode Note";
                    public static final String NODE_REPRESENTATION = "34133-9";
                    public static final String CODING_SCHEME = "2.16.840.1.113883.6.1";
                }
            }

        /* Format Code */
        public final class FormatCode {

            public static final String FORMAT_CODE_SCHEME = IheConstants.FORMAT_CODE_SCHEME;

            public final class EDispensation {

                public final class EpsosPivotCoded {

                    public static final String DISPLAY_NAME = "epSOS coded eDispensation";
                    public static final String NODE_REPRESENTATION = "urn:epSOS:ep:dis:2010";
                    public static final String CODING_SCHEME = "epSOS formatCodes";
                }

                public final class PdfSourceCoded {

                    public static final String DISPLAY_NAME = "PDF/A coded document";
                    public static final String NODE_REPRESENTATION = "urn:ihe:iti:xds-sd:pdf:2008";
                    public static final String CODING_SCHEME = "epSOS formatCodes";
                }
            }

            public final class Consent {

                public final class ScannedDocument {

                    public static final String DISPLAY_NAME = "Consent";
                    public static final String NODE_REPRESENTATION = "urn:ihe:iti:bppc-sd:2007";
                    public static final String CODING_SCHEME = "IHE PCC";
                }

                public final class NotScannedDocument {

                    public static final String DISPLAY_NAME = "Consent";
                    public static final String NODE_REPRESENTATION = "urn:ihe:iti:bppc:2007";
                    public static final String CODING_SCHEME = "IHE PCC";
                }
            }
            
             public final class Hcer {

                public final class EpsosPivotCoded {

                    public static final String DISPLAY_NAME = "epSOS Coded HCER";
                    public static final String NODE_REPRESENTATION = "urn:epSOS:hcer:hcer:2013";
                    public static final String CODING_SCHEME = "epSOS formatCodes";
                }

            }
        }
    }

    public class REGISTRY_PACKAGE {

        public static final String SUBMISSION_SET_STR = "SubmissionSet";
        public static final String AUTHOR_INSTITUTION_STR = "authorInstitution";
        public static final String AUTHOR_PERSON_STR = "authorPerson";
        public static final String OBJECT_TYPE_UUID = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
        public static final String SUBMISSION_TIME_STR = "submissionTime";
        public static final String SUBMISSION_TIME_FORMAT = "yyyyMMddhhmmss";
        public static final String NAME_ED = "eDispensation";
        public static final String NAME_CONSENT = "Consent to Share Information";
        public static final String NAME_HCER = "Summarization of Episode Note";
        public static final String DESCRIPTION_ED = "Description of eDispensation";
        public static final String DESCRIPTION_CONSENT = "Description of Consent to Share Information";
        public static final String DESCRIPTION_HCER = "Description Summarization of Episode Note";
        public static final String AUTHOR_CLASSIFICATION_UUID = "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d";
        /* Coding Scheme */
        public static final String CODING_SCHEME_UUID = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";
        public static final String CODING_SCHEME_VALUE = "2.16.840.1.113883.6.1";
        public static final String CODING_SCHEME_CONS_STR = "Privacy Policy Acknowledgement Document";
        /* XDSSubmissionSet.UniqueId */
        public static final String XDSSUBMSET_UNIQUEID_SCHEME = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
        public static final String XDSSUBMSET_UNIQUEID_VALUE = "1.2.40.0.13.1.1.81.21.242.182.20120421201035552.33123";
        public static final String XDSSUBMSET_UNIQUEID_STR = "XDSSubmissionSet.uniqueId";
        /* XDSSubmissionSet.patientId */
        public static final String XDSSUBMSET_PATIENTID_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
        public static final String XDSSUBMSET_PATIENTID_STR = "XDSSubmissionSet.patientId";
        /* XDSSubmissionSet.sourceId */
        public static final String XDSSUBMSET_SOURCEID_SCHEME = "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";
        public static final String XDSSUBMSET_SOURCEID_VALUE = "1.3.6.1.4.1.21367.2009.1.2.1";
        public static final String XDSSUBMSET_SOURCEID_STR = "XDSSubmissionSet.sourceId";
    }

    public class SUBMISSION_SET_CLASSIFICATION {

        public static final String CLASSIFICATION_NODE_UUID = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
    }
}
