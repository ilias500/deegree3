//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.services.csw.exporthandling;

import static javax.xml.XMLConstants.DEFAULT_NS_PREFIX;
import static javax.xml.XMLConstants.NULL_NS_URI;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.deegree.commons.xml.CommonNamespaces.XSINS;
import static org.deegree.protocol.csw.CSWConstants.CSW_202_DISCOVERY_SCHEMA;
import static org.deegree.protocol.csw.CSWConstants.CSW_202_NS;
import static org.deegree.protocol.csw.CSWConstants.CSW_PREFIX;
import static org.deegree.protocol.csw.CSWConstants.VERSION_202;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.deegree.commons.tom.ows.Version;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.filter.xml.FilterCapabilitiesExporter;
import org.deegree.protocol.csw.CSWConstants;
import org.deegree.protocol.csw.CSWConstants.CSWRequestType;
import org.deegree.protocol.csw.CSWConstants.Sections;
import org.deegree.services.controller.OGCFrontController;
import org.deegree.services.controller.ows.capabilities.OWSCapabilitiesXMLAdapter;
import org.deegree.services.jaxb.controller.DCPType;
import org.deegree.services.jaxb.controller.DeegreeServiceControllerType;
import org.deegree.services.jaxb.metadata.DeegreeServicesMetadataType;
import org.deegree.services.jaxb.metadata.KeywordsType;
import org.deegree.services.jaxb.metadata.LanguageStringType;
import org.deegree.services.jaxb.metadata.ServiceIdentificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Does the exportHandling for the Capabilities. This is a very static handling for explanation.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: thomas $
 * 
 * @version $Revision: $, $Date: $
 */
public class GetCapabilitiesHandler extends OWSCapabilitiesXMLAdapter {

    private static Logger LOG = LoggerFactory.getLogger( GetCapabilitiesHandler.class );

    private static final String OGC_NS = "http://www.opengis.net/ogc";

    private static final String OGC_PREFIX = "ogc";

    private static LinkedList<String> parameterValues;

    private final boolean isTransactionEnabled;

    private final XMLStreamWriter writer;

    private final DeegreeServicesMetadataType mainControllerConf;

    private final DeegreeServiceControllerType mainConf;

    private final Set<Sections> sections;

    private final ServiceIdentificationType identification;

    private final Version version;

    private boolean isSoap;

    private final boolean isEnabledInspireExtension;

    private static List<String> additionalQueryables = new ArrayList<String>();

    /**
     * additional queryable properties in ISO
     */
    private static List<String> isoQueryables = new ArrayList<String>();

    private static LinkedList<String> supportedOperations = new LinkedList<String>();

    private final Map<String, String> varToValue;

    static {
        isoQueryables.add( "Format" );
        isoQueryables.add( "Type" );
        isoQueryables.add( "AnyText" );
        isoQueryables.add( "Modified" );
        isoQueryables.add( "Identifier" );
        isoQueryables.add( "Subject" );
        isoQueryables.add( "Title" );
        isoQueryables.add( "Abstract" );
        isoQueryables.add( "RevisionDate" );
        isoQueryables.add( "AlternateTitle" );
        isoQueryables.add( "CreationDate" );
        isoQueryables.add( "PublicationDate" );
        isoQueryables.add( "OrganisationName" );
        isoQueryables.add( "HasSecurityConstraints" );
        isoQueryables.add( "Language" );
        isoQueryables.add( "ResourceIdentifier" );
        isoQueryables.add( "ParentIdentifier" );
        isoQueryables.add( "KeywordType" );
        isoQueryables.add( "TopicCategory" );
        isoQueryables.add( "ResourceLanguage" );
        isoQueryables.add( "GeographicDescriptionCode" );
        isoQueryables.add( "Denominator" );
        isoQueryables.add( "DistanceValue" );
        isoQueryables.add( "DistanceUOM" );
        isoQueryables.add( "TempExtent_begin" );
        isoQueryables.add( "TempExtent_end" );
        isoQueryables.add( "ServiceType" );
        isoQueryables.add( "ServiceTypeVersion" );
        isoQueryables.add( "Operation" );
        isoQueryables.add( "OperatesOn" );
        isoQueryables.add( "OperatesOnIdentifier" );
        isoQueryables.add( "OperatesOnName" );
        isoQueryables.add( "CouplingType" );

        additionalQueryables.add( "AccessConstraints" );
        additionalQueryables.add( "Classification" );
        additionalQueryables.add( "ConditionApplyingToAccessAndUse" );
        additionalQueryables.add( "Degree" );
        additionalQueryables.add( "Lineage" );
        additionalQueryables.add( "MetadataPointOfContact" );
        additionalQueryables.add( "OtherConstraints" );
        additionalQueryables.add( "SpecificationTitle" );
        additionalQueryables.add( "SpecificationDate" );
        additionalQueryables.add( "SpecificationDateType" );

        supportedOperations.add( CSWRequestType.GetCapabilities.name() );
        supportedOperations.add( CSWRequestType.DescribeRecord.name() );
        supportedOperations.add( CSWRequestType.GetRecords.name() );
        supportedOperations.add( CSWRequestType.GetRecordById.name() );
        // supportedOperations.add( CSWRequestType.Transaction.name() );

    }

    public GetCapabilitiesHandler( XMLStreamWriter writer, DeegreeServicesMetadataType mainControllerConf,
                                   DeegreeServiceControllerType mainConf, Set<Sections> sections,
                                   ServiceIdentificationType identification, Version version,
                                   boolean isTransactionEnabled, boolean isEnabledInspireExtension, boolean isSoap ) {
        this.writer = writer;
        this.mainControllerConf = mainControllerConf;
        this.mainConf = mainConf;
        this.sections = sections;
        this.identification = identification;
        this.version = version;
        this.isSoap = isSoap;
        this.isTransactionEnabled = isTransactionEnabled;
        this.isEnabledInspireExtension = isEnabledInspireExtension;

        this.varToValue = new HashMap<String, String>();
        String serverAddress = OGCFrontController.getHttpGetURL();
        String systemStartDate = "2010-11-16";
        String organizationName = null;
        String emailAddress = null;
        try {
            organizationName = mainControllerConf.getServiceProvider().getProviderName();
            List<String> emailAddresses = mainControllerConf.getServiceProvider().getServiceContact().getElectronicMailAddress();
            if ( !emailAddresses.isEmpty() ) {
                emailAddress = emailAddresses.get( 0 ).trim();
            }
            varToValue.put( "${SERVER_ADDRESS}", serverAddress );
            varToValue.put( "${SYSTEM_START_DATE}", systemStartDate );
            varToValue.put( "${ORGANIZATION_NAME}", organizationName );
            varToValue.put( "${CI_EMAIL_ADDRESS}", emailAddress );
        } catch ( NullPointerException e ) {
            String msg = "There is somewhere a null?!?";
            LOG.debug( msg );
            throw new NullPointerException( msg );
        }

    }

    /**
     * Prepocessing for the xml export. Checks which version is requested and delegates it to the right versionexport.
     * In this case, version 2.0.2 of CSW is leaned on the 1.0.0 of the OGC specification.
     * 
     * @param writer
     * @param mainControllerConf
     * @param mainConf
     * @param sections
     * @param identification
     * @param version
     * @param isSoap
     * @throws XMLStreamException
     */
    public void export()
                            throws XMLStreamException {

        if ( VERSION_202.equals( version ) ) {
            export202( writer, sections, identification, mainControllerConf, mainConf, isSoap );
        } else {
            throw new InvalidParameterValueException( "Supported versions are: '" + VERSION_202 + "'. Version '"
                                                      + version + "' instead is not supported." );
        }
    }

    private void export202( XMLStreamWriter writer, Set<Sections> sections, ServiceIdentificationType identification,
                            DeegreeServicesMetadataType mainControllerConf, DeegreeServiceControllerType mainConf,
                            boolean isSoap )
                            throws XMLStreamException {

        writer.writeStartElement( CSW_PREFIX, "Capabilities", CSW_202_NS );
        writer.writeNamespace( "ows", OWS_NS );
        writer.writeNamespace( OGC_PREFIX, OGC_NS );
        writer.writeNamespace( "xlink", XLN_NS );
        writer.writeAttribute( "version", "2.0.2" );
        writer.writeAttribute( "xsi", XSINS, "schemaLocation", CSW_202_NS + " " + CSW_202_DISCOVERY_SCHEMA );

        // ows:ServiceIdentification
        if ( sections.isEmpty() || sections.contains( Sections.ServiceIdentification ) ) {
            exportServiceIdentification( writer, identification );

        }

        // ows:ServiceProvider
        if ( sections.isEmpty() || sections.contains( Sections.ServiceProvider ) ) {
            exportServiceProvider100( writer, mainControllerConf.getServiceProvider() );
        }

        // ows:OperationsMetadata
        if ( sections.isEmpty() || sections.contains( Sections.OperationsMetadata ) ) {

            exportOperationsMetadata( writer, mainConf.getDCP(), OWS_NS );
        }

        // mandatory
        FilterCapabilitiesExporter.export110( writer );

        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void exportOperationsMetadata( XMLStreamWriter writer, DCPType dcp, String owsNS )
                            throws XMLStreamException {
        writer.writeStartElement( owsNS, "OperationsMetadata" );

        if ( isTransactionEnabled && !supportedOperations.contains( CSWRequestType.Transaction.name() ) ) {
            supportedOperations.add( CSWRequestType.Transaction.name() );
        }

        for ( String name : supportedOperations ) {
            writer.writeStartElement( owsNS, "Operation" );
            writer.writeAttribute( "name", name );
            exportDCP( writer, dcp, owsNS );

            if ( name.equals( CSWRequestType.GetCapabilities.name() ) ) {

                writeGetCapabilities( writer, owsNS );

                writer.writeEndElement();// Operation
                continue;
            } else if ( name.equals( CSWRequestType.DescribeRecord.name() ) ) {

                writeDescribeRecord( writer, owsNS );

                writer.writeEndElement();// Operation
                continue;
            } else if ( name.equals( CSWRequestType.GetRecords.name() ) ) {

                writeGetRecords( writer, owsNS );

                writer.writeEndElement();// Operation
                continue;
            } else if ( name.equals( CSWRequestType.GetRecordById.name() ) ) {

                writeGetRecordById( writer, owsNS );

                writer.writeEndElement();// Operation
                continue;
            } else if ( name.equals( CSWRequestType.Transaction.name() ) ) {
                // because there is the same output like for GetRecordById
                writeGetRecordById( writer, owsNS );

                writer.writeEndElement();// Operation
                continue;
            }

        }

        // if xPathQueryables are allowed than this should be set
        // writer.writeStartElement( owsNS, "Constraint" );
        // writer.writeAttribute( "name", "XPathQueryables" );
        //
        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( "allowed" );
        // writer.writeEndElement();// Value
        //
        // writer.writeEndElement();// Constraint

        writer.writeStartElement( owsNS, "Constraint" );
        writer.writeAttribute( "name", "IsoProfiles" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSWConstants.GMD_NS );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Constraint

        // if XML and/or SOAP is supported
        writer.writeStartElement( owsNS, "Constraint" );
        writer.writeAttribute( "name", "PostEncoding" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "XML" );
        writer.writeEndElement();// Value
        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "SOAP" );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Constraint
        // additional inspire queryables
        if ( this.isEnabledInspireExtension ) {
            exportExtendedCapabilities( writer, owsNS );
        }
        writer.writeEndElement();// OperationsMetadata

    }

    private void exportExtendedCapabilities( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {

        writer.writeStartElement( owsNS, "ExtendedCapabilities" );

        InputStream in = GetCapabilitiesHandler.class.getResourceAsStream( "extendedCapInspire.xml" );
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( in );
        reader.nextTag();
        writeTemplateElement( writer, reader );
        writer.writeEndElement();
    }

    private void writeTemplateElement( XMLStreamWriter writer, XMLStreamReader inStream )
                            throws XMLStreamException {

        if ( inStream.getEventType() != XMLStreamConstants.START_ELEMENT ) {
            throw new XMLStreamException( "Input stream does not point to a START_ELEMENT event." );
        }
        int openElements = 0;
        boolean firstRun = true;
        while ( firstRun || openElements > 0 ) {
            firstRun = false;
            int eventType = inStream.getEventType();

            switch ( eventType ) {
            case CDATA: {
                writer.writeCData( inStream.getText() );
                break;
            }
            case CHARACTERS: {
                String s = new String( inStream.getTextCharacters(), inStream.getTextStart(), inStream.getTextLength() );
                // TODO optimize
                for ( String param : varToValue.keySet() ) {
                    String value = varToValue.get( param );
                    s = s.replace( param, value );
                }
                writer.writeCharacters( s );

                break;
            }
            case END_ELEMENT: {
                writer.writeEndElement();
                openElements--;
                break;
            }
            case START_ELEMENT: {
                if ( inStream.getNamespaceURI() == NULL_NS_URI || inStream.getPrefix() == DEFAULT_NS_PREFIX
                     || inStream.getPrefix() == null ) {
                    writer.writeStartElement( inStream.getLocalName() );
                } else {
                    if ( writer.getNamespaceContext().getPrefix( inStream.getPrefix() ) == XMLConstants.NULL_NS_URI ) {
                        // TODO handle special cases for prefix binding, see
                        // http://download.oracle.com/docs/cd/E17409_01/javase/6/docs/api/javax/xml/namespace/NamespaceContext.html#getNamespaceURI(java.lang.String)
                        writer.setPrefix( inStream.getPrefix(), inStream.getNamespaceURI() );
                    }
                    writer.writeStartElement( inStream.getPrefix(), inStream.getLocalName(), inStream.getNamespaceURI() );
                }
                // copy all namespace bindings
                for ( int i = 0; i < inStream.getNamespaceCount(); i++ ) {
                    String nsPrefix = inStream.getNamespacePrefix( i );
                    String nsURI = inStream.getNamespaceURI( i );
                    writer.writeNamespace( nsPrefix, nsURI );
                }

                // copy all attributes
                for ( int i = 0; i < inStream.getAttributeCount(); i++ ) {
                    String localName = inStream.getAttributeLocalName( i );
                    String nsPrefix = inStream.getAttributePrefix( i );
                    String value = inStream.getAttributeValue( i );
                    String nsURI = inStream.getAttributeNamespace( i );
                    if ( nsURI == null ) {
                        writer.writeAttribute( localName, value );
                    } else {
                        writer.writeAttribute( nsPrefix, nsURI, localName, value );
                    }
                }

                openElements++;
                break;
            }
            default: {
                break;
            }
            }
            if ( openElements > 0 ) {
                inStream.next();
            }
        }
    }

    /*
     * private static void writeVersionAndUpdateSequence( XMLStreamWriter writer, int updateSequence ) throws
     * XMLStreamException { writer.writeAttribute( "version", VERSION_202.toString() ); writer.writeAttribute(
     * "updateSequence", Integer.toString( updateSequence ) ); }
     */

    private void exportServiceIdentification( XMLStreamWriter writer, ServiceIdentificationType identification )
                            throws XMLStreamException {
        writer.writeStartElement( "http://www.opengis.net/ows", Sections.ServiceIdentification.toString() );

        for ( String oneTitle : identification.getTitle() ) {
            writeElement( writer, "http://www.opengis.net/ows", "Title", oneTitle );
        }

        for ( String oneAbstract : identification.getAbstract() ) {
            writeElement( writer, "http://www.opengis.net/ows", "Abstract", oneAbstract );
        }
        String fees = "NONE";

        // keywords [0,n]
        exportKeywords( writer, identification.getKeywords() );

        writeElement( writer, "http://www.opengis.net/ows", "ServiceType", "CSW" );
        writeElement( writer, "http://www.opengis.net/ows", "ServiceTypeVersion", "2.0.2" );

        // fees [1]
        fees = identification.getFees();
        if ( isEmpty( fees ) ) {
            identification.setFees( "NONE" );
        }
        fees = identification.getFees();

        // fees = fees.replaceAll( "\\W", " " );
        writeElement( writer, "http://www.opengis.net/ows", "Fees", fees );

        // accessConstraints [0,n]
        exportAccessConstraints( writer, identification );

        writer.writeEndElement();
    }

    private final boolean isEmpty( String value ) {
        return value == null || "".equals( value );
    }

    /**
     * write a list of keywords in csw 2.0.2 style.
     * 
     * @param writer
     * @param keywords
     * @throws XMLStreamException
     */
    private void exportKeywords( XMLStreamWriter writer, List<KeywordsType> keywords )
                            throws XMLStreamException {
        if ( !keywords.isEmpty() ) {
            for ( KeywordsType kwt : keywords ) {
                if ( kwt != null ) {
                    writer.writeStartElement( "http://www.opengis.net/ows", "Keywords" );
                    List<LanguageStringType> keyword = kwt.getKeyword();
                    for ( LanguageStringType lst : keyword ) {
                        exportKeyword( writer, lst );
                        // -> keyword [1, n]
                    }
                    // -> type [0,1]
                    // exportCodeType( writer, kwt.getType() );
                    writer.writeEndElement();// WCS_100_NS, "keywords" );
                }
            }

        }

    }

    /**
     * @param writer
     * @param lst
     * @throws XMLStreamException
     */
    private void exportKeyword( XMLStreamWriter writer, LanguageStringType lst )
                            throws XMLStreamException {
        if ( lst != null ) {
            writeElement( writer, "http://www.opengis.net/ows", "Keyword", lst.getValue() );
        }
    }

    private void exportAccessConstraints( XMLStreamWriter writer, ServiceIdentificationType identification )
                            throws XMLStreamException {
        List<String> accessConstraints = identification.getAccessConstraints();

        if ( accessConstraints.isEmpty() ) {
            accessConstraints.add( "NONE" );

        } else {
            for ( String ac : accessConstraints ) {
                if ( !ac.isEmpty() ) {
                    writeElement( writer, "http://www.opengis.net/ows", "AccessConstraints", ac );
                }
            }
        }

    }

    /**
     * Writes the parameter and attributes for the mandatory GetCapabilities operation to the output.
     * 
     * @param writer
     *            to write the output
     * @param owsNS
     *            the OWS namespace
     * @throws XMLStreamException
     */
    private void writeGetCapabilities( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "sections" );

        parameterValues = new LinkedList<String>();

        parameterValues.add( "ServiceIdentification" );
        parameterValues.add( "ServiceProvider" );
        parameterValues.add( "OperationsMetadata" );
        parameterValues.add( "Filter_Capabilities" );

        for ( String value : parameterValues ) {
            writer.writeStartElement( owsNS, "Value" );
            writer.writeCharacters( value );
            writer.writeEndElement();// Value
        }
        writer.writeEndElement();// Parameter

        // Constraints...
    }

    /**
     * Writes the parameter and attributes for the mandatory DescribeRecord operation to the output.
     * 
     * @param writer
     *            to write the output
     * @param owsNS
     *            the OWS namespace
     * @throws XMLStreamException
     */
    private void writeDescribeRecord( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "typeName" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSW_PREFIX + ":Record" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "gmd" + ":MD_Metadata" );
        writer.writeEndElement();// Value

        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( CSW_PREFIX + ":Service" );
        // writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writeOutputFormat( writer, owsNS );

        // writer.writeStartElement( owsNS, "Parameter" );
        // writer.writeAttribute( "name", "schemaLocation" );
        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( "http://www.w3.org/TR/xmlschema-1/" );
        // writer.writeEndElement();// Value
        // writer.writeEndElement();// Parameter

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "schemaLanguage" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "XMLSCHEMA" );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Constraint

        // Constraints...[0..*]
    }

    private void writeOutputFormat( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {
        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "outputFormat" );

        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( acceptFormat );
        // writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "text/xml" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "application/xml" );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

    }

    /**
     * Writes the parameter and attributes for the mandatory GetRecords operation to the output.
     * 
     * @param writer
     *            to write the output
     * @param owsNS
     *            the OWS namespace
     * @throws XMLStreamException
     */
    private void writeGetRecords( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "typeNames" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSW_PREFIX + ":Record" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSWConstants.GMD_PREFIX + ":MD_Metadata" );
        writer.writeEndElement();// Value

        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( CSW_PREFIX + ":Service" );
        // writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writeOutputFormat( writer, owsNS );

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "outputSchema" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSWConstants.CSW_202_NS );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSWConstants.GMD_NS );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "resultType" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "hits" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "results" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "validate" );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "ElementSetName" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "brief" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "summary" );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "full" );
        writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "CONSTRAINTLANGUAGE" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( "Filter" );
        writer.writeEndElement();// Value

        // writer.writeStartElement( owsNS, "Value" );
        // writer.writeCharacters( "CQL_Text" );
        // writer.writeEndElement();// Value

        writer.writeEndElement();// Parameter

        writer.writeStartElement( owsNS, "Constraint" );
        writer.writeAttribute( "name", "SupportedISOQueryables" );
        for ( String s : isoQueryables ) {
            writer.writeStartElement( owsNS, "Value" );
            writer.writeCharacters( s );
            writer.writeEndElement();// Value
        }
        writer.writeEndElement();// Constraint

        writer.writeStartElement( owsNS, "Constraint" );
        writer.writeAttribute( "name", "AdditionalQueryables" );
        for ( String val : additionalQueryables ) {
            writeElement( writer, owsNS, "Value", val );
        }
        writer.writeEndElement();// Constraint

    }

    /**
     * Writes the parameter and attributes for the mandatory GetRecordById operation to the output.<br>
     * In this case the optional transaction operation uses this writing to the output, as well.
     * 
     * @param writer
     *            to write the output
     * @param owsNS
     *            the OWS namespace
     * @throws XMLStreamException
     */
    private void writeGetRecordById( XMLStreamWriter writer, String owsNS )
                            throws XMLStreamException {

        writeOutputFormat( writer, owsNS );

        writer.writeStartElement( owsNS, "Parameter" );
        writer.writeAttribute( "name", "outputSchema" );

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSW_202_NS );
        writer.writeEndElement();// Value

        writer.writeStartElement( owsNS, "Value" );
        writer.writeCharacters( CSWConstants.GMD_NS );
        writer.writeEndElement();// Value

        writer.writeEndElement(); // Parameter

    }

}