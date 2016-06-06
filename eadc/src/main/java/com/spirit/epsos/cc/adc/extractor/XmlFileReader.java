package com.spirit.epsos.cc.adc.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Utility-class for reading a file into a DOM-structure This class uses the singleton pattern for ensuring, that cached utilized Objects are only initialized once
 * 
 * @author mk
 */
public class XmlFileReader
{
	// Singelton-object
	private static XmlFileReader singletonInstance = null;
	private static Boolean syncVariable = new Boolean( true );
	private static Logger log = Logger.getLogger( XmlFileReader.class );
	// Cached Objects for improving performance on subsequent usage of XmlFileReader
	private DocumentBuilderFactory objDocBuilderFactory = null;
	private DocumentBuilder objDocBuilder = null;
	
	/**
	 * This constructor is private to ensure, that it is only called by the getInstance() method It initializes the static DocumentBuilderFactory object and the static DocumentBuilderFactory object.
	 * 
	 * @throws Exception
	 */
	private XmlFileReader() throws Exception
	{
		log.info( "Trying to initialize the XmlFileReader singleton object" );
		this.objDocBuilderFactory = DocumentBuilderFactory.newInstance();
		this.objDocBuilderFactory.setNamespaceAware( true );
		this.objDocBuilderFactory.setValidating( false );
		try
		{
			this.objDocBuilder = objDocBuilderFactory.newDocumentBuilder();
			log.info( "XmlFileReader singleton object initialized successfully" );
		}
		catch( ParserConfigurationException parserConfigurationException )
		{
			log.error(	"An Error occured during setting up an XML-DocumentBuilder Object",
						parserConfigurationException );
			throw new Exception(	"An Error occured during setting up an XML-DocumentBuilder Object",
									parserConfigurationException );
		}
	}
	/**
	 * Getter-method for retrieving the singleton-object
	 * 
	 * @return
	 * @throws Exception
	 */
	public static XmlFileReader getInstance() throws Exception
	{
		if( XmlFileReader.singletonInstance == null )
		{
			synchronized( XmlFileReader.syncVariable )
			{
				if( XmlFileReader.singletonInstance == null )
				{
					try
					{
						XmlFileReader.singletonInstance = new XmlFileReader();
					}
					catch( Exception exception )
					{
						log.error(	"Unable to initialize the singleton for class " + XmlFileReader.class.getCanonicalName(),
									exception );
						throw new Exception(	"Unable to initialize the singleton for class " + XmlFileReader.class.getCanonicalName(),
												exception );
					}
				}
			}
		}
		log.debug( "Instance of XmlFileReader successfully retrieved" );
		return XmlFileReader.singletonInstance;
	}
	/**
	 * Reds an xml-file and returns the content as a DOM-structure
	 * 
	 * @param strFilePath
	 *            The path to the file to be read
	 * @return The xml-file's content as DOM-structure
	 * @throws Exception
	 */
	public Document readXmlDocumentFromFile( String strFilePath ) throws Exception
	{
		log.debug( "Trying to read the following File into a DOM structure:" + strFilePath );
		// Preparing the FileInputStream
		FileInputStream fileInputStream;
		try
		{
			fileInputStream = new FileInputStream( new File( strFilePath ) );
		}
		catch( FileNotFoundException fileNotFoundException )
		{
			log.error(	"The xml file to parse was not found or can not be opened for reading:" + strFilePath,
						fileNotFoundException );
			throw new Exception(	"The xml file to parse was not found or can not be opened for reading:" + strFilePath,
									fileNotFoundException );
		}
		log.debug( "FileInputStream initialized successfully" );
		// Preparing the xmlDocument
		Document xmlDocument = null;
		try
		{
			xmlDocument = this.objDocBuilder.parse( fileInputStream );
		}
		catch( SAXException saxException )
		{
			log.error(	"Error, when parsing the following xml-document" + strFilePath,
						saxException );
			throw new Exception(	"Error, when parsing the following xml-document" + strFilePath,
									saxException );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Closing the fileInputStream
		try
		{
			fileInputStream.close();
		}
		catch( IOException ioException )
		{
			log.warn(	"Unable to close the fileInputStream for file:" + strFilePath,
						ioException );
		}
		// Post-checking the result's validity
		if( xmlDocument == null )
		{
			log.error( "The resulting XML-Document from reading " + strFilePath
						+ " was null" );
			throw new Exception( "The resulting XML-Document from reading " + strFilePath
									+ " was null" );
		}
		log.debug( "XML Document successfully parsed from File" );
		return xmlDocument;
	}
}
