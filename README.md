[![Build Status](https://travis-ci.org/difi/vefa-esubmission.svg?branch=master)](https://travis-ci.org/difi/vefa-esubmission)

# vefa-esubmission - shared component for creating ASiC-based eSubmission messages

The purpose of this component is to create valid eSubmission messages suitable for
the PEPPOL network.

There are three modes of operation:

  1. Create ASiC archive based upon the contents of an SBDH in a XML file. This
     is the recommended way of doing it.

         java -jar vefa-esubmission.jar -sbdh <filename> -ks <keystore> -ksp <keystore password> \
             -pp <private key password>

     You should end up with an ASiC archive looking like this (the order is not important):

          asice-archive:
             |-- mimetype
             |-- sample-readme.txt
             |-- sbdh.xml
             |-- trdm090-submit-tender-sample.xml
             |
             \---META-INF
                 |-- asicmanifest.xml
                 |-- manifest.xml
                 |-- signature-2009bda5-f28e-4302-8350-fb7589b83bd7.p7s

  1. Create ASiC archive manually, specifying each and every file to be included.

        java -jar vefa-esubmission.jar -sbdh <filename> -bis <filename> -a <attachment> \
             -ks <keystorefile> -ksp <keystore password> -pp <private key password>

  1. Scan a directory and create ASiC archives for each SBDH file found.

        Not implemented yet.

The process of creating an eSubmission message in ASiC archives goes like this:

1. Create the ASiC (ZIP) file.
1. Copy all files into ASiC archive, while calculating the checksums as we go along.
1. Create the `META-INF/asicmanifest.xml` file, which describes each file added
1. Sign the `asicmanifest.xml` file using the private key and certificate supplied.
1. Close the ASiC (ZIP) file.


## Example command lines

The examples below assumes that the Java jar file `vefa-esubmission.jar`, holding the complete distribution, i.e. with
dependencies, is available in the current directory.

Furthermore, the `java` command must be available.

### Create ASiC archive based on the contents of SBDH

Creates a sample ASiC archive containing the test documents provided in this implementation. The contents of the
ASiC archive, i.e. the files to be included are specified in the `<Manifest>` element of the SBDH:

```xml
        <Manifest>
            <NumberOfItems>2</NumberOfItems>
            <ManifestItem>
                <MimeTypeQualifierCode>application/xml</MimeTypeQualifierCode>
                <!-- First reference is always the main BIS XML document, which may or may not -->
                <!-- reference attachments -->
                <UniformResourceIdentifier>cid:trdm090-submit-tender-sample.xml</UniformResourceIdentifier>
                <Description>The main UBL XML document</Description>
            </ManifestItem>
            <ManifestItem>
                <!-- Include this file in the ASiC archive as an attachment -->
                <MimeTypeQualifierCode>text/plain</MimeTypeQualifierCode>
                <UniformResourceIdentifier>cid:sample-readme.txt</UniformResourceIdentifier>
                <Description>A sample attachment</Description>
            </ManifestItem>
        </Manifest>
```

The signature is created using the supplied test certificates.

    java-jar vefa-esubmission.jar -sbdh test -keystore test

The sbdh file is included as part of the signed contents of the ASiC archive.


### Creating a single ASiC archive using the supplied test resources
`vefa-esubmission` comes with some test files and a test certificate and henceforth, we courteously supply you
with the ability to create test messages:

    # Creates ASiC archive named 'vefa-esubmission.asice' in current directory
    java -jar vefa-esubmission.jar -bis test -keystore test

The resulting ASiC archive will only containt the sample BIS XML file: `trdm090-submit-tender-sample.xml`. I.e.
no SBDH will be included, nor will any attahcments be included.

### Creating a single ASiC archive
This is how you create an ASiC archive from the file `trdm090.xml`:
    # Input files: trdm090 (main document) and brochure.pdf (attachment)
    # Output: message42.asice

    java -jar vefa-esubmission.jar -o message42.asice -bis trdm090.xml -a brochure.pdf \
         -keystore keystore.jks -ksp keystore_password -pkp private_key_password

The resulting ASiC archive will contain `trdom090.xml` (marked as the Rootfile in `META-INF/asicmanifest.xml`)
 and `brochure.pdf`.

### Wrapping ASiC archive as base64 payload inside StandardBusinessDocument

Given an ASiC archive to be transmitted as base64 payload wrapped inside a <StandardBusinessDocument> element together with 
a SBDH, this command will create a new "outer" SBDH based upon extraction of the ```sbdh.xml``` from the ASiC archive:
 
    java -jar vefa-esubmission.jar -wrap vefa-esubmission-703572024684397217.asice \
        -o sbd-to-be-transmitted.xml

### Unwrapping base64 encoded ASiC payload from StandardBusinessDocument
    
    java -jar vefa-esubmission.jar -unwrap sbd.xml -o message.asice
    
### Creating multiple ASiC archives

A directory is scanned, and for each XML file found, an ASiC archive is created.

Note! This feature has not yet been implemented.


### Iterates all XML files in directory_name and creates corresponding messages

    java -jar vefa-esubmission.jar -d directory_name -out directory_name \
         -keystore keystore.jks -ksp keystore_password -pkp private_key_password

This feature has not been implemented yet.
