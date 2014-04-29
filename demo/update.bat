rmdir src /S /Q
rmdir target /S /Q
del log.roo
del pom.xml

call roo script --file update.roo
pause