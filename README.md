# region_match
Script to a add location column to FreezerPro data exports based on a simple check-file containing: Hospital name, Country, location/city.

Code has been rewritten in Java, but I will however leave the VBscript-version.

Upon running RegionMatch the user will be prompted to select a comma-separated file containing "Hospital name; Country; location/city" and then the FreezerPro export. Both selected files need to be ANSI or UTF-8 encoded, the encoding can't differ between the files. 
For the code to work for anyone else they will need to keep this in mind, also the export needs to look something like this "UID; sampletype; samplenumber; sampledate; study; country; hospital".
