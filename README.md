# region_match
Script to add a location column to FreezerPro data exports based on a simple check-file containing: Hospital name, Country, location/city.

Code has been rewritten in Java, but I will however include the VBscript-version.

Upon running RegionMatch the user will be prompted to select a comma-separated file containing "Hospital name; Country; location/city" and then the FreezerPro export containing "UID;Sample Type;(NOPHO) Provnummer;Provtagningsdatum;Country of Origin;Hospital". 
Both selected files need to be ANSI or UTF-8 encoded, the encoding can't differ between the files. 

The code will work with a little tinkering to search and match other data.
