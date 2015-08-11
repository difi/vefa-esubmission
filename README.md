# vefa-innlevering

```

# 
java -jar vefa-innlevering.jar -o melding42.asice -m trdm090.xml -a brochure.pdf \
    -keystore keystore.jks -ksp keystore_password -pkp private_key_password
    
# Iterates all XML files in directory_name and creates corresponding messages 
java -jar vefa-innlevering.jar -d directory_name \
     -keystore keystore.jks -ksp keystore_password -pkp private_key_password