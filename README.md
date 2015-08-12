# vefa-innlevering - shared component for creating ASiC-based eSubmission messages

The purpose of this component is to create valid eSubmission messages from PEPPOL/BIS XML documents.

The process of creating an eSubmission message in ASiC archives goes like this:

1. Create ASiC (ZIP) file
1. Copy all files into ASiC archive, while calculating the checksums as we go along.
1. Create the `META-INF/asicmanifest.xml` file, which describes each file added
1. Sign the `asicmanifest.xml` file using the private key and certificate supplied.
1. Close the ASiC (ZIP) file.

## Example command lines

The examples below assumes that the Java jar file `vefa-innlevering.jar`, holding the complete distribution, i.e. with
dependencies, is available in the current directory.

Furthermore, the `java` command must be available.

### Creating a single ASiC archive using the supplied test resources
`vefa-innlevering` comes with some test files and a test certificate and henceforth, we courteously supply you
with the ability to create test messages:
```
# Creates ASiC archive named 'vefa-innlevering.asice' in current directory
java -jar vefa-innlevering.jar -bis test -keystore test
```

### Creating a single ASiC archive
This is how you create an ASiC archive from the file `trdm090.xml`:
```
# Input files: trdm090 (main document) and brochure.pdf (attachment)
# Output: message42.asice

java -jar vefa-innlevering.jar -o message42.asice -bis trdm090.xml -a brochure.pdf \
    -keystore keystore.jks -ksp keystore_password -pkp private_key_password
```
### Creating multiple ASiC archives
A directory is scanned, and for each XML file found, an ASiC archive is created.

Note! This feature has not yet been implemented.
```
### Iterates all XML files in directory_name and creates corresponding messages
java -jar vefa-innlevering.jar -d directory_name -out directory_name \
     -keystore keystore.jks -ksp keystore_password -pkp private_key_password
```
