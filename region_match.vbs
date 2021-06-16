' Name: Add location to nopho exports
' Author: Henrik Vestin, Uppsala Biobank
' Date: 2021 05 31
' Version: 0.2 added list of hospital/region as a sub-method instead of having it in a separate file. Not neat but simpler to maintain if changes need to be done.
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

Dim list()
Dim HospitalName 
Dim Hospital
Dim Region
Dim i
Dim newLine


Set objFSO = CreateObject("Scripting.FileSystemObject")
Call land_region_stad_sjukhus()

Call OpenFileDialog() 'select source file
SourceFileLocation = sFileSelected
DirPath = sFileSelected
FileDestination = DirPath & "-" & Date & "-export_med_region.csv"

Set objRead = objFSO.OpenTextFile(SourceFileLocation, 1, False)
objRead.SkipLine ' skip first line in file since it contains column headers
SourceFileContent = objRead.ReadAll ' Read file
objRead.Close
SourceFileContentToArray = Split(SourceFileContent, vbLf) 'split creates a one dimensional array

Set objWrite = objFSO.CreateTextFile(FileDestination, True) ' create file
objWrite.WriteLine "UID,Sample Type,(NOPHO) Provnummer,() Provtagningsdatum,(NOPHO) Studie,() Country of Origin,() Hospital,() Location" 'fixa headern i FreezerPro!
For x = 0 to Ubound(SourceFileContentToArray) -1 'iterate array until end, -1 is because the exported csv has an empty line at the end.
	arrSource = Split(SourceFileContentToArray(x), ",")  'split each line into new array at each ","
	HospitalName = arrSource(6) 'fetch Hospital name from current line
	'wscript.Echo HospitalName
	
	Call land_region_stad_sjukhus()
	'wscript.Echo "List lenght:" & UBound(list)
	'wscript.Echo "Last Entry in list: " & list(55)
	For y = 0 to UBound(list)
		arrCheck = Split(list(y), ";")
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

wscript.Echo "Antal rader i export: " & Ubound(SourceFileContentToArray) & ". Antal rader matchade: " & i
	
' End of script
objWrite.Close


Sub OpenFileDialog()
	Set wShell=CreateObject("WScript.Shell")
	Set oExec=wShell.Exec("mshta.exe ""about:<input type=file id=FILE><script>FILE.click();new ActiveXObject('Scripting.FileSystemObject').GetStandardStream(1).WriteLine(FILE.value);close();resizeTo(0,0);</script>""")
	sFileSelected = oExec.StdOut.ReadLine
End Sub

Sub land_region_stad_sjukhus()
ReDim list(55) ' När man skall lägga till ett ny region/sjukhus behöver man öka siffran i Redim list(xx) till (xy)
list(0) = "AALBORG SYGEHUS NORD;Denmark;Aalborg"
list(1) = "AARHUS UNIVERSITETSHOSPITAL;Denmark;Aarhus"
list(2) = "ASTRID LINDGRENS BARNSJUKHUS;Sweden;Stockholm"
list(3) = "BERGENS UNIVERSITETSSJUKHUS;Norway;Bergen"
list(4) = "DROTTNING SILVIAS BARNSJUKHUS GÖTEBORG;Sweden;Gothenburg"
list(5) = "HELSINGFORS UNIVERSITETSSJUKHUS;Finland;Helsingfors"
list(6) = "KAROLINSKA UNIVERSITETS SJUKHUSET HUDDINGE;Sweden;Stockholm"
list(7) = "KUOPIO UNIVERSITY HOSPITAL;Finland;Kuopio"
list(8) = "LINKÖPINGS UNIVERSITETSSJUKHUS;Sweden;Linköping"
list(9) = "LUNDS UNIVERSITETSSJUKHUS;Sweden;Lund"
list(10) = "NORRLANDS UNIVERSITETSSJUKHUS UMEÅ;Sweden;Umeå"
list(11) = "ODENSE UNIVERSITETSHOSPITAL;Denmark;Odense"
list(12) = "REYKJAVIK CHILDRENS HOSPITAL;Iceland;Reykjavik"
list(13) = "RIKSHOSPITALET KÖPENHAMN;Denmark;Copenhagen"
list(14) = "RIKSHOSPITALET OSLO;Norway;Oslo"
list(15) = "SKÅNES UNIVERSITETSSJUKHUS MALMÖ;Sweden;Lund"
list(16) = "TAMMERFORS UNIVERSITETSSJUKHUS;Finland;Tammerfors"
list(17) = "TROMSÖ UNIVERSITETSSJUKHUS;Norway;Tromsö"
list(18) = "TRONDHEIM UNIVERSITETSSJUKHUS;Norway;Trondheim"
list(19) = "TURKU UNIVERSITY HOSPITAL;Finland;Turku"
list(20) = "ULEÅBORG UNIVERSITETSSJUKHUS;Finland;Uleåborg"
list(21) = "ULLEVÅL UNIVERSITETSSJUKHUS;Norway;Oslo"
list(22) = "UPPSALA AKADEMISKA SJUKHUS;Sweden;Uppsala"
list(23) = "BLEKINGESJUKHUSET (KARLSKRONA);Sweden;Lund"
list(24) = "CENTRALLASARETTET VÄXJÖ;Sweden;Lund"
list(25) = "CENTRALSJUKHUSET KRISTIANSTAD;Sweden;Lund"
list(26) = "CENTRALSJUKHUSET KARLSTAD;Sweden;Gothenburg"
list(27) = "FALU LASARETT (FALUN);Sweden;Uppsala"
list(28) = "GÄVLE SJUKHUS;Sweden;Uppsala"
list(29) = "GÄLLIVARE SJUKHUS;Sweden;Umeå"
list(30) = "HALLANDS SJUKHUS  HALMSTAD;Sweden;Gothenburg"
list(31) = "HELSINGBORGS LASARETT;Sweden;Lund"
list(32) = "HUDIKSVALLS SJUKHUS;Sweden;Uppsala"
list(33) = "KALIX SJUKHUS;Sweden;Umeå"
list(34) = "KIRUNA SJUKHUS;Sweden;Umeå"
list(35) = "LYCKSELE LASARETT;Sweden;Umeå"
list(36) = "LÄNSSJUKHUSET KALMAR;Sweden;Linköping"
list(37) = "LÄNSSJUKHUSET RYHOV (JÖNKÖPING);Sweden;Gothenburg"
list(38) = "MÄLARSJUKHUSET (ESKILSTUNA);Sweden;Uppsala"
list(39) = "NORRA ÄLVSBORGS LÄNSSJUKHUS (TROLLHÄTTAN);Sweden;Gothenburg"
list(40) = "NYKÖPINGS LASARETT;Sweden;Uppsala"
list(41) = "PITEÅ SJUKHUS;Sweden;Umeå"
list(42) = "SKARABORGS SJUKHUS (SKÖVDE);Sweden;Gothenburg"
list(43) = "SKELLEFTEÅ LASARETT;Sweden;Umeå"
list(44) = "SOLLEFTEÅ SJUKHUS;Sweden;Umeå"
list(45) = "SUNDERBY SJUKHUS (LULEÅ);Sweden;Umeå"
list(46) = "SUNDSVALLS SJUKHUS;Sweden;Umeå"
list(47) = "SÖDRA ÄLVSBORGS SJUKHUS (BORÅS);Sweden;Gothenburg"
list(48) = "UNIVERSITETSSJUKHUSET ÖREBRO;Sweden;Uppsala"
list(49) = "VISBY LASARETT;Sweden;Stockholm"
list(50) = "VRINNEVISJUKHUSET NORRKÖPING;Sweden;Linköping"
list(51) = "VÄSTERVIKS SJUKHUS;Sweden;Linköping"
list(52) = "VÄSTMANLANDS SJUKHUS VÄSTERÅS;Sweden;Uppsala"
list(53) = "ÅLANDS CENTRALSJUKHUS MARIEHAMN;Åland;Åland"
list(54) = "ÖRNSKÖLDSVIKS SJUKHUS;Sweden;Umeå"
list(55) = "ÖSTERSUNDS SJUKHUS;Sweden;Umeå"
'list(xx) = ' Var noga med formateringen när ett nytt sjukhus läggs till och glöm inte att ändra xx till nästkommande i turordningen.
End Sub
