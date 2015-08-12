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

Furthermore, the `java` command is available.

This is how you create an ASiC archive from the file `trdm090.xml`:
```
    java -jar vefa-innlevering.jar -o melding42.asice -bis trdm090.xml -a brochure.pdf \
    -keystore keystore.jks -ksp keystore_password -pkp private_key_password
```

# Iterates all XML files in directory_name and creates corresponding messages 
java -jar vefa-innlevering.jar -d directory_name \
     -keystore keystore.jks -ksp keystore_password -pkp private_key_password