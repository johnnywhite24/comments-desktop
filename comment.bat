@echo off

set "SCRIPT_DIR=%~dp0"

set "LIB_DIR=%SCRIPT_DIR%\out\production\comments-desktop"

java -cp %LIB_DIR% sample.Main %1