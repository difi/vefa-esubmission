# vefa-innlevering

This is how you create an ASiC archive from the file `trdm090.xml`:

```
    java -jar vefa-innlevering.jar -o melding42.asice -bis trdm090.xml -a brochure.pdf \
    -keystore keystore.jks -ksp keystore_password -pkp private_key_password
```

# Iterates all XML files in directory_name and creates corresponding messages 
java -jar vefa-innlevering.jar -d directory_name \
     -keystore keystore.jks -ksp keystore_password -pkp private_key_password