@echo off
set path= C:\Program Files\Java\jre1.8.0_191\bin\%path%
java -version
java -Xmx4098m -Dprism.maxvram=2000m -jar cetsim_0.9.jar
pause
exit