#ifndef SERIALCONTROL_H
#define SERIALCONTROL_H

#include <Arduino.h>
#include <WString.h>

template<class T> inline Print &operator <<(Print &obj, T arg) { obj.print(arg); return obj; }

#define maxLength 16

/* Command structure:
pos:  01 23 4567 8901 23456
      id cm arg1 arg2
*/

// Corresponding offests:
#define ID  0
#define CMD 2
#define AR1 4
#define AR2 8

// Some variable definitions

extern int ownID; // Adjust according to module ID
extern boolean answer;
                                               
extern String command;
extern boolean commandComplete;               

extern int recID;

// Some function forward declarations

void digitalWriteFunc();

void pinModeFunc();

void analogWriteFunc();

void allFunc();

String getArgument(int arg);

int parseArgument(int argOffset);

#endif
