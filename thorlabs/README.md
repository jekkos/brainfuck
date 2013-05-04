Thorlabs-dc2100-nativelib
=========================

This DLL is needed to communicate with the Thorlabs DC2100 High Power LED driver. Sources were originally published under LGPL and included with the installation CD of the device. However to build and debug the source, I had to setup a Visual Studio project, putting all the bits and pieces together.

Currently the project compiles OK for 64bit windows. Due to the lacking 32bit version of the nivisa.lib object file, 32bit is not there yet.

Project compilation & linkage
-----------------------------
To compile the project, just open the .sln file in Visual Studio and select the x64 Release profile. 
For this project, a dependency on a National Instruments CVI class was replaced by some functions from Windows itself, as the include files were hard to find (and probably didn't allow redistribution).

You need the NI Visa 5.3 Runtime installed on your PC.

Communication with Java
-----------------------
At this moment communication between the Windows DLL and a Java application works. You can generate a JNA library using the excellent [JNAerator](http://code.google.com/p/jnaerator/) project. Don't forget to point your jna.library.path to the DLL folder when configuring your VM. For a correct string conversion between two worlds, you will also need to set -Djna.encoding=UTF8 as a second argument.i

Product information
-------------------
Can be found at http://www.thorlabs.com/NewGroupPage9.cfm?ObjectGroup_ID=4003.

