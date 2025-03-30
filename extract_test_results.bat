@echo off
setlocal enabledelayedexpansion

set "REPORT_PATH=test-output\emailable-report.html"
set "OUTPUT_FILE=test-summary.txt"

echo Extracting test results from %REPORT_PATH%

for /f "tokens=2 delims=<>" %%a in ('findstr /C:"Total</td><td>" %REPORT_PATH%') do set TOTAL=%%a
for /f "tokens=2 delims=<>" %%a in ('findstr /C:"Pass</td><td>" %REPORT_PATH%') do set PASSED=%%a
for /f "tokens=2 delims=<>" %%a in ('findstr /C:"Fail</td><td>" %REPORT_PATH%') do set FAILED=%%a
for /f "tokens=2 delims=<>" %%a in ('findstr /C:"Skip</td><td>" %REPORT_PATH%') do set SKIPPED=%%a

echo Total: %TOTAL%
echo Passed: %PASSED%
echo Failed: %FAILED%
echo Skipped: %SKIPPED%

echo Test Execution Report: > %OUTPUT_FILE%
echo ---------------------------- >> %OUTPUT_FILE%
echo Total Test Cases: %TOTAL% >> %OUTPUT_FILE%
echo Passed: %PASSED% >> %OUTPUT_FILE%
echo Failed: %FAILED% >> %OUTPUT_FILE%
echo Skipped: %SKIPPED% >> %OUTPUT_FILE%

echo Test summary saved to %OUTPUT_FILE%
