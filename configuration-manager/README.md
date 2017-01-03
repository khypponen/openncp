# Getting started with the Dynamic Discovery Client for SMP

----------

## INDEX

    1. Introduction
    1.1. Purpose of this document
    2. Project
    2.1. Artifacts
    2.2. Repository
    2.3. Source Code
    3. Setting up parameters
    3.1. Interfaces and Implementations
    3.2. Proxy Configuration
    4. How to use the Dynamic Discovery Client
    5. References
    6. License
    7. Contact Information


##  1. INTRODUCTION

This Dynamic Discovery Client is a web service client designed to retrieve metadata of Participants hosted under any compliant Service Metadata Publishers (SMP). This web service client can be used with SMPs that implement the e-SENS SMP profile based on the OASIS BDX SMP specification and by any business domain.

## 1.1. PURPOSE OF THIS DOCUMENT

This page provides a brief description of how to use this web service client to connect to a Service Metadata Publisher using CNAME or NAPTR record to find out and retrieve Receiver Recipient Metadata.


## 2. PROJECT

## 2.1. ARTIFACTS

![enter image description here](https://ec.europa.eu/cefdigital/wiki/download/attachments/35215791/Dynamic%20Discovery%20Client.PNG?version=1&modificationDate=1476951474175&api=v2)


## 2.2. REPOSITORY

All versions of Dynamic Discovery Client can be found on:

Releases

https://ec.europa.eu/cefdigital/artifact/content/repositories/eDelivery/eu/europa/ec/dynamic-discovery/

Snapshots

https://ec.europa.eu/cefdigital/artifact/content/repositories/eDelivery-snapshots/eu/europa/ec/dynamic-discovery/


## 2.3. SOURCE CODE

Please find the GIT repository on https://ec.europa.eu/cefdigital/code/scm/edelivery/dynamic-discovery-client.git

Source code: https://ec.europa.eu/cefdigital/code/projects/EDELIVERY/repos/dynamic-discovery-client/browse


## 3. SETTING UP PARAMETERS

The DynamicDiscoveryBuilder class provides 6 interfaces and theirs default implementations, however, the user is free to use customized implementations to fit other needs.


## 3.1. INTERFACES AND IMPLEMENTATIONS

**IMetadataLocator**

This interface is responsible for managing the lookup algorithm and providing the URL or URI for the request. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.locator.impl.DefaultBDXRLocator** for looking up NAPTR or CNAME records.

**IMetadataFetcher**

This interface is responsible for fetching the response from the request. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher.**

**IDNSLookup**

This interface is responsible for looking up the participant identifier. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.locator.dns.impl.DefaultDNSLookup.**

**IMetadataProvider**

This interface is responsible for resolving URIs for participant identifier metadata. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.provider.impl.DefaultProvider.**

**IMetadataReader**

This interface is responsible for parsing the response according to the XSD. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.reader.impl.DefaultBDXRReader.**

**ISignatureValidator**

This interface is responsible for verifying the signature of the response according to the XSD. By default it provides the implementation **eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultSignatureValidator.**


## 3.2. PROXY CONFIGURATION

By default **eu.europa.ec.dynamicdiscovery.core.fetcher.impl.DefaultURLFetcher** does not use proxy configuration. In order to use proxy, an instance of the class **eu.europa.ec.dynamicdiscovery.core.security.impl.DefaultProxy** must be configured and passed by parameter as follows:

Example:

    DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance().fetcher(new DefaultURLFetcher(new DefaultProxy("127.0.0.1", 8000, "user", "password"))).build();


## 4. HOW TO USE THE DYNAMIC DISCOVERY CLIENT

The DynamicDiscoveryBuilder is responsible for creating a new instance of the Dynamic Discovery Client. By default, there are implementations for all core services provided by the tool however new customized implementations can be used by the user.

Default implementation example:

      DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
                     .locator(new DefaultBDXRLocator("ehealth.acc.edelivery.tech.ec.europa.eu"))
                     .build();

      ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");

      List<DocumentIdentifier> documentIdentifiers = smpClient.getDocumentIdentifiers(participantIdentifier);
      ServiceMetadata sm = smpClient.getServiceMetadata(participantIdentifier, new DocumentIdentifier("urn::epsos:services## epsos-21", "epsos-docid-qns"));


Customized implementation example:

Please replace classes starting with **"Customized"** by implementations extended from the aforementioned interfaces.It is not mandatory to have implementations for all the components (Locator, Reader, DNSLookup, Provider, Fetcher)

    DynamicDiscovery smpClient = DynamicDiscoveryBuilder.newInstance()
            .locator(new CustomizedBDXRLocator("acc.edelivery.tech.ec.europa.eu", new CustomizedDNSLookup()))
            .reader(new CustomizedBdxrReader(new CustomizedSignatureValidator()))
            .provider(new CustomizedProvider())
            .fetcher(new CustomizedURLFetcher(new CustomizedProxyConfiguration("127.0.0.1", 8000, "user", "password")))
            .build();
    ParticipantIdentifier participantIdentifier = new ParticipantIdentifier("9925:0367302178", "iso6523-actorid-upis");
    List<DocumentIdentifier> documents = smpClient.getDocumentIdentifiers(participantIdentifier );
    ServiceMetadata sm = smpClient.getServiceMetadata(participantIdentifier ,  new DocumentIdentifier("urn::epsos:services## epsos-21", "epsos-docid-qns"));


## 5. REFERENCES

e-SENS/PR-BDXL - ipi.gr/display/ESENS/PR+-+BDXL

PEPPOL BUSDOX - https://joinup.ec.europa.eu/community/epractice/document/eu-peppol-project-deliverable-busdox-specifications-v10

Service Metadata Publisher (SMP) - http://docs.oasis-open.org/bdxr/bdx-smp/v1.0/bdx-smp-v1.0.html

SMP XML schema - http://docs.oasis-open.org/bdxr/bdx-smp/v1.0/cs03/schemas/bdx-smp-201605.xsd


## 6. LICENSE

Dynamic Discovery Client is under license LGPL-2.1.

More Information:

https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt


## 7. CONTACT INFORMATION

CEF Support Team

By email: CEF-EDELIVERY-SUPPORT@ec.europa.eu

By phone: +32 2 299 09 09

Standard Service: 8am to 6pm (Normal EC working Days)

Standby Service**: 6pm to 8am (Commission and Public Holidays, Weekends)

** Only for critical and urgent incidents and only by phone