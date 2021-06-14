' Name: Add location to nopho exports
' Author: Henrik Vestin, Uppsala Biobank
' Date: 2021 05 31
' Version: 0.1 Initial version
'
' Comment: Jira UBB-325, Scrip to add location column to FreezerPro data exports based on a simple
' 		   check-file containing: Hospital name, Country, location/city
'
'
Option Explicit ' Force explicit variable declaration.


Dim wShell
Dim oExec
Dim sFileSelected

Dim SourceFileLocation
Dim CheckFileLocation
Dim FileDestination
Dim DirPath

Dim objFSO
Dim objRead
Dim objWrite
Dim SourceFileContent
Dim CheckFileContent
Dim SourceFileContentToArray
Dim CheckFileContentToArray
Dim x
Dim y

Dim arrSource
Dim arrCheck

Dim HospitalName 
Dim Hospital
Dim Region
Dim i
Dim newLine

Sub OpenFileDialog()
	Set wShell=CreateObject("WScript.Shell")
	Set oExec=wShell.Exec("mshta.exe ""about:<input type=file id=FILE><script>FILE.click();new ActiveXObject('Scripting.FileSystemObject').GetStandardStream(1).WriteLine(FILE.value);close();resizeTo(0,0);</script>""")
	sFileSelected = oExec.StdOut.ReadLine
End Sub


Set objFSO = CreateObject("Scripting.FileSystemObject")

Call OpenFileDialog() 'select location file
CheckFileLocation = sFileSelected

Call OpenFileDialog() 'select source file
SourceFileLocation = sFileSelected
DirPath = sFileSelected
FileDestination = DirPath & "-" & Date & "-export_med_region.csv"

Set objRead = objFSO.OpenTextFile(SourceFileLocation, 1, False)
objRead.SkipLine ' skip first line in file since it contains column headers
SourceFileContent = objRead.ReadAll ' Read file
objRead.Close
SourceFileContentToArray = Split(SourceFileContent, vbLf) 'split creates a one dimensional array

Set objRead = objFSO.OpenTextFile(CheckFileLocation, 1, False)
objRead.SkipLine ' skip first line in file since it contains column headers
CheckFileContent = objRead.ReadAll ' Read file
objRead.Close
CheckFileContentToArray = Split(CheckFileContent, vbCrLf) 'creates a one dimensional array vbCrLf
'wscript.Echo "Checkfile chosen = " & CheckFileLocation
'wscript.Echo "Source filepath = " & DirPath  'path where resultfile is writen to
'wscript.Echo "Outputpath = " & FileDestination

Set objWrite = objFSO.CreateTextFile(FileDestination, True) ' create file
objWrite.WriteLine "UID,Sample Type,(NOPHO) Provnummer,() Provtagningsdatum,(NOPHO) Studie,() Country of Origin,() Hospital,() Location" 'fixa headern i FreezerPro!
For x = 0 to Ubound(SourceFileContentToArray) -1 'iterate array until end, -1 is because the exported csv has an empty line at the end.
	arrSource = Split(SourceFileContentToArray(x), ",")  'split each line into new array at each ","
	HospitalName = arrSource(6) 'fetch Hospital name from current line
	'wscript.Echo HospitalName
	
	For y = 0 to UBound(CheckFileContentToArray) -1
	arrCheck = Split(CheckFileContentToArray(y), ";")
	Hospital = arrCheck(0) 
		If Hospital = HospitalName then
			Region = arrCheck(2)
			'wscript.Echo Hospital
			'wscript.Echo Region
			i = i + 1
			Exit for
		End If
	Next

Redim Preserve arrSource(Ubound(arrSource)+1)
arrSource(7) = Region
newLine = Join(arrSource, ",") 'join array into string
objWrite.WriteLine newLine 'append the string to outputfile
Region = ""

Next

wscript.Echo "Sourcefile array lenght:" & Ubound(SourceFileContentToArray) & ". Number of matches: " & i
	
' End of script
objWrite.Close
