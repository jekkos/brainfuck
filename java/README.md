This application is deveveloped to conduct exeperiments with a self built LED Matrix on in vivo brain cells.

CONFIGURATION
-------------

The app will save an XML configuration file in the home directory of the current user.

under linux this will be `~/.LedMatrixApp/settings.xml`
under windows this will be i`%APPDATA%/KULeuven/settings.xml`

The config file is hand editable, and will be used to create the domain models at startup time.

BUILDING
--------

If you want to find all the necessary dependencies, you will need to install one maven dependency in your local repository manually. 
This is the enhanced version of RXTX for serial communication, which wasnt available from the maven central. 
nrserialJava can be downloaded at http://code.google.com/p/nrjavaserial/. Subsequently issue the following maven command

    mvn install:install-file -DartifactId=nrjavaserial -Dversion=3.8.8 -DgroupId=gnu.io -Dpackaging=jar -Dfile=nrjavaserial-3.8.8.jar -DgeneratePom=true

Application can be built using Maven2 using the following goal

    mvn process-classes

DEPLOYING
---------

Application can be built with Maven2 and deployed using the following goal

    mvn package

This will run both the goals webstart:jnlp create a .jnlp file in the target directory that will allow you to launch the application using java webstart. 
Of course you will need to have maven installed and configure in your PATH variable.
