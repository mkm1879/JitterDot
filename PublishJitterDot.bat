@echo off
e:
cd \EclipseJava\JitterDot\bin

"C:\Program Files\Java\jdk1.7.0_17\bin\jar.exe" -cfm ..\JitterDot.jar ..\JitterDot.MF edu\clemson\lph\jitter\JitterDot.class edu\clemson\lph\controls\GPSTextField.class edu\clemson\lph\jitter\files edu\clemson\lph\jitter\geometry edu\clemson\lph\jitter\logger edu\clemson\lph\jitter\structs edu\clemson\lph\security
