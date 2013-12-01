This is the arduino (microcontroller) side of the brainfuck project.

Configuration
-------------
The Makefile delegates resetting the board to a short Perl program. You'll need to install Device::SerialPort to use it though. You'll also need the YAML library to run ard-parse-boards.

On Debian or Ubuntu:

    apt-get install libdevice-serial-perl
    apt-get install libyaml-perl
On Fedora:

    yum install perl-Device-SerialPort
    yum install perl-YAML
On OSX: 

    port install p5-device-serialport 
    port install p5-yaml
On other systems:

    cpanm Device::SerialPort
    cpanm YAML

BUILDING
--------
This project can't be built using the regular Arduino IDE, instead of this you can use much more powerful IDE's that feature autocompletion and other fancy stuff.

To build this project, just fire up a terminal, head for the root directory and enter the followig command. Don't forget to change your ARDUINO_PATH variable in the Makefile and point it to your local installation.

    make

Deploying 
---------
Deploying the result .hex file can be done through the included Makefile.
