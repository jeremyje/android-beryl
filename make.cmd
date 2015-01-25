@echo off
set APPDIR=..\..\..\apps
set GAMEDIR=..\..\..\games
call ant

xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\BatteryTest\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\MakeMotivator\MakeMotivator\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\MyLocationRedux\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\NoKeyguard\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\SmartSettings\Common\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\VolumeWidget\libs\
xcopy /Y /S /H jar-sources\org.beryl-src.jar %APPDIR%\WidgetPreview\libs\

xcopy /Y /S /H jar-sources\org.beryl-src.jar %GAMEDIR%\Falldown\libs\