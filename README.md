Brainfuck Led Matrix project
============================
This is my git repository containing everything related to the brainfuck project. Initiially the project consisted out of three different modules which were merged in three subfolders in this repository. 

This application was developed to be used with a custom built high power driver and led matrix to perform medium-throughput screening assays in light-sensitive neuronal cells using optogenetic tools. By using viral vector-mediated delivery of ChannelRhodopsin2 it is possible to test for readouts of neuronal activity in well-defined pathlogoical conditions. 

Project structure
-----------------
* arduino - contains the microcontroller code for MOSFET switching
* java - contains the java code for the GUI part of the application
* thorlabs - contains the VC++ code for the steering the Thorlabs LED driver
* docs - contains datasheets for the used components in this project
* images - contains some images of the hardware part of the project

Interesting Links
----------------
* good primer on using [mosfets as switches](http://www.embeddedrelated.com/showarticle/98.php) in high power applications

Project Movie
-------------
A short movie of an end-to-end test can be seen on [YouTube](http://www.youtube.com/watch?v=n9LuOjYpKvs)
