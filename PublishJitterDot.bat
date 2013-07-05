@echo off
e:
cd \EclipseJava\JitterDot\bin

"C:\Program Files\Java\jdk1.7.0_25\bin\jar.exe" -cfm ..\JitterDot.jar ..\JitterDot.MF edu\clemson\lph\jitter edu\clemson\lph\security edu\clemson\lph\controls\GPSTextField.class edu\clemson\lph\dialogs 
cd ..
