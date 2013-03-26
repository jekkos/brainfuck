This application is deveveloped to conduct exeperiments with a self built LED Matrix on in vivo brain cells.

== CONFIGURATION == 

The app will save an XML configuration file in the home directory of the current user.

under linux this will be ~/.LedMatrixApp/settings.xml
under windows this will be %APPDATA%/KULeuven/settings.xml

== BUILDING ==

Application can be built using Maven2 using the following goal

mvn process-classes

== DEPLOYING ==

Application can be built with Maven2 and deployed using the following goal

mvn package

This will run both the goals webstart:jnlp create a .jnlp file in the target directory that will allow you to launch the application using java webstart. Of course you will need to have maven installed and configure in your PATH variable.
