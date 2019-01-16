# MQAdminTool

A Java GUI application for managing IBM MQ objects and messages

This application is written in Java 1.8 and using NetBeans IDE 8.0.2.

Currently using IBM MQ client lib 8.0.



# SSL parameters to pass

Add the below SSL properties as your VM arguments

-Djavax.net.ssl.trustStore=<<truststore_path>> -Djavax.net.ssl.keyStore=<<keystore_path>> -Djavax.net.ssl.keyStorePassword=<<password>> -Djavax.net.ssl.trustStorePassword=<<password>> -Dcom.ibm.mq.cfg.useIBMCipherMappings=false -DCertificateLabel=<<yours>>
  
  Note - -Dcom.ibm.mq.cfg.useIBMCipherMappings=false to be set only if you are using non-IBM JREs.
