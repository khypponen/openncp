package epsos.ccd.gnomon.cda.parser.unit;

import epsos.ccd.gnomon.cda.parser.beans.Address;
import epsos.ccd.gnomon.cda.parser.beans.Author;
import epsos.ccd.gnomon.cda.parser.beans.Birthdate;
import epsos.ccd.gnomon.cda.parser.beans.Code;
import epsos.ccd.gnomon.cda.parser.beans.Contact;
import epsos.ccd.gnomon.cda.parser.beans.Custodian;
import epsos.ccd.gnomon.cda.parser.beans.Gender;
import epsos.ccd.gnomon.cda.parser.beans.GenericCode;
import epsos.ccd.gnomon.cda.parser.beans.Identifier;
import epsos.ccd.gnomon.cda.parser.beans.LegalAuthenticator;
import epsos.ccd.gnomon.cda.parser.beans.MedicalComponent;
import epsos.ccd.gnomon.cda.parser.beans.Name;
import epsos.ccd.gnomon.cda.parser.beans.Observation;
import epsos.ccd.gnomon.cda.parser.beans.Organization;
import epsos.ccd.gnomon.cda.parser.beans.Patient;
import epsos.ccd.gnomon.cda.parser.beans.Problem;
import epsos.ccd.gnomon.cda.parser.beans.RelatedDocument;
import epsos.ccd.gnomon.cda.parser.beans.Time;
import epsos.ccd.gnomon.cda.parser.docs.ClinicalDocument;
import epsos.ccd.gnomon.cda.parser.docs.HCEReport;
import epsos.ccd.gnomon.cda.parser.enums.ContactUse;
import epsos.ccd.gnomon.cda.parser.enums.Country;
import epsos.ccd.gnomon.cda.parser.enums.MedicalSection;
import epsos.ccd.gnomon.cda.parser.enums.Sex;
import epsos.ccd.gnomon.cda.parser.enums.URLScheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

/**
 * A simple unit testing class.
 *
 * @author Akis Papadopoulos
 */
public class HCERTester {

    public static void main(String[] args) throws Exception {
        // Creating the patient id
        Identifier pid = new Identifier("48905059995", "1.3.6.1.4.1.28284.6.2.2.1");

        // Creating the name of the patient
        Name pname = new Name("CUUSK", "LY");

        // Creating the gender of the patient
        Gender gender = new Gender(Sex.Female);

        // Creating the dob of the patient
        Birthdate dob = new Birthdate(new Date());

        // Creating the address of the patient
        Address paddress = new Address("Tallinna mnt 23", "Tartu", "50706", Country.Estonia.getCode());
        paddress.setUse("PHYS");

        // Creating the patient
        Patient pat = new Patient(pid, pname, gender, dob);
        pat.setTypeCode("RCT");
        pat.setContextControlCode("OP");
        pat.setClassCode("PAT");
        pat.setAddress(paddress);
        pat.addContact(new Contact(ContactUse.H, URLScheme.TEL, "+8888888888888"));
        pat.addContact(new Contact(ContactUse.H, URLScheme.MAILTO, "ly@cuusk.ee"));

        // Creating an organization
        Organization org = new Organization(new Identifier("90009016", "1.3.6.1.4.1.28284.6.2.4.1"), "Eesti E-Tervise SA");
        org.addContact(new Contact(ContactUse.WP, URLScheme.TEL, "+3726943900"));
        org.addContact(new Contact(ContactUse.WP, URLScheme.TEL, "e-tervis@e-tervis.ee"));

        // Creating the author of the clinical document
        Author author = new Author(pid, new Name("Fetcher", "Patient Summary"), org);
        author.setTime(new Date());
        author.setClassCode("ASSIGNED");

        // Creating a legal authenticator
        LegalAuthenticator legal = new LegalAuthenticator(new Identifier("121312", "1.3.6.1.4.1.28284.6.2.4.32"), null, org);
        legal.setTime(new Date());

        // Creating a custodian
        Custodian cust = new Custodian(org);

        // Creating the document class code
        Code classCode = new GenericCode("34133-9", "Summarization of Episode Note", "2.16.840.1.113883.6.1", "LOINC");

        // Creating the document id
        Identifier did = new Identifier("20120519234435746", "1.3.6.1.4.1.28284.6.2.4.75");

        // Creating a HCER clinical document
        ClinicalDocument cda = new HCEReport(classCode, did, new Date(), new Date(), Country.Estonia);
        cda.setTitle("Health Care Encounter Report");
        cda.setPatient(pat);
        cda.setAuthor(author);
        cda.setAuthenticator(legal);
        cda.setCustodian(cust);

        // Adding related documents
        cda.addRelatedDocument(new RelatedDocument("XFRM", new Identifier("201304291013151234", "1.3.6.1.4.1.28284.6.2.4.75")));
        cda.addRelatedDocument(new RelatedDocument("APND", new Identifier("201304290913154321", "2.16.17.710.820.1000.990.1.1.1")));

        // Creating a diagnosis medical section component
        MedicalComponent diagnoses = new MedicalComponent(MedicalSection.PROBLEM_LIST, "Problem list");

        // Creating a diagnosis code
        Code code1 = new GenericCode("55607006", "Problem", "2.16.840.1.113883.6.96", "SNOMED CT");

        // Creating an effective time
        Time time1 = new Time(new Date(), null);

        // Creating a medical problem
        Problem prob1 = new Problem(code1, time1, "active");
        prob1.setDescription("Acute angle closure glaucoma");
        prob1.setRoot("2.16.470.1.100.1.1.1000.990.1");
        prob1.addObservation(new Observation("H40", "Glaucoma", "1.3.6.1.4.1.12559.11.10.1.3.1.44.2", "ICD-10"));
        
        // Adding the medical entry
        diagnoses.addEntry(prob1);

        // Creating a diagnosis code
        Code code2 = new GenericCode("55607006", "Problem", "2.16.840.1.113883.6.96", "SNOMED CT");

        // Creating an effective time
        Time time2 = new Time(new Date(), new Date());

        // creating a medical problem
        Problem prob2 = new Problem(code2, time2, "completed");
        prob2.setDescription("Diabetic ketoacidosis");
        prob2.setRoot("2.16.470.1.100.1.1.1000.990.1");
        prob2.addObservation(new Observation("E14", "Unspecified diabetes mellitus", "1.3.6.1.4.1.12559.11.10.1.3.1.44.2", "ICD-10"));
        prob2.addObservation(new Observation("XX", "Unspecified", "1.3.6.1.4.1.12559.11.10.1.3.1.44.2", "ICD-10"));
        
        // Adding the medical entry
        diagnoses.addEntry(prob2);
        
        diagnoses.addRoot("2.16.840.1.113883.10.20.1.11");
        diagnoses.addRoot("1.3.6.1.4.1.19376.1.5.3.1.3.6");
        
        // Adding the medical component to the document
        cda.addComponent(diagnoses);

        try {
            // Saving locally the XML patient summary into an XML file
            FileWriter writer = new FileWriter(new File("/home/karkaletsis/Downloads/hcer-report.xml"));
            writer.write(cda.toString());
            writer.flush();

            // Printing a well-formed XML format into the console
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(cda.toString().getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            System.out.println(new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray()));
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
}
