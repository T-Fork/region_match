/**
 Name: Add location to nopho exports
@author: Henrik Vestin, Uppsala Biobank
Date: 2021 05 31, v0.3 2022 03 01
History: 	- 0.2 added the list of hospitals per region as a sub-method instead of having it in a separate file. Not neat but simpler to maintain if changes need to be done.
			- 0.3 rewritten in java to hopefully speed things up. Reverted back to a separate hospital file so the users can maintain that list instead of contacting Biobank IT-support.
            - 0.4 Exports no longer contained study-column and hospital, country of origin and region no longer have a leading "()". Code revised to reflect these changes.
            ToDo: progressbar or indicator if still necessary.

            Comment: Jira UBB-325, Script to add location column to FreezerPro data exports based on a simple
		   list containing: Hospital, Country, Region
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class RegionMatch 
{
    //Fields
    private HashMap<Integer,SourceFile> sourcefiles;
    private HashMap<String,HospitalFile> hospitalfiles;
    private HashMap<Integer,MatchedFile> matchedfiles;
    private static String gFilePath;
    private static String SaveFilePath;
    private HashMap<String, MutableInt> storevalueIU;

    // Main, needs to getset filepaths to source- and hosptial-file. 
    public static void main(String[] args) throws Exception
    {
        RegionMatch RM = new RegionMatch();

        RM.getHFpath();
        RM.readFromFile(gFilePath);
        RM.getSFpath();
        RM.readFromFile(gFilePath);
        RM.getSaveFilePath(gFilePath);
        RM.HospitalfromUID();
        RM.MatchedFileContent();
        System.exit(0);       
    }
    
    public RegionMatch() //Create the HashMaps used store the objects from the files.
    {
        sourcefiles = new HashMap<Integer,SourceFile>();
        hospitalfiles = new HashMap<String,HospitalFile>();
        matchedfiles = new HashMap<Integer,MatchedFile>();
        storevalueIU = new HashMap<String, MutableInt>();
    }

    // Methods below

    /**  
    * @param gFilePath
    * 
    * @throws FileNotFoundException
    * @return
    */
    public void readFromFile(String gFilePath) throws IOException
    {
        String file = gFilePath;
        String line = "";
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252")))
        {
            br.readLine();
            while ((line = br.readLine()) != null)
            {
                String str[] = line.split(";");
                int stl = str.length; //get length of array
                if(stl==3)
                    {
                    HospitalFile h = new HospitalFile(str[0], str[1], str[2]);
                    hospitalfiles.putIfAbsent(str[0], h); //put values in hashmap with hospital as key
                    }
                else
                    {
                    if(stl<6) //neater way would be catching the "ArrayIndexOutOfBoundsException"
                        {
                            String[] tempStr = new String[6];
                            System.arraycopy(str, 0, tempStr, 0, stl);
                            str = tempStr;
                            for(int arrayExpand = stl; arrayExpand < 6; arrayExpand++)
                            {
                                str[arrayExpand] = "";
                            }
                        }
                    String strToint = str[0]; //grab UID string from array
                    int i = Integer.parseInt(strToint); //convert string to int
                    SourceFile s = new SourceFile(i, str[1], str[2], str[3], str[4], str[5]);
                    sourcefiles.putIfAbsent(i, s); //put values in hashmap with UID as key
                    }
            }
            br.close();
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("File not found!");
        }
        
        return;
    }

    /**
    * @return gFilePath
    */
    public String getHFpath()
    {
        String dTitle = "Select file containing hospital and region";
        JFilechooser(dTitle);
        return gFilePath;
    }

    /**
    * @return gFilePath
    */   
    public String getSFpath()
    {
        String dTitle = "Select the FreezerPro export to process";
        JFilechooser(dTitle);
        return gFilePath;
        
    }
    
    /**  
     * @param dTitle
     * 
     * @return gFilePath
     * @throws 
     */    
    public String JFilechooser(String dTitle)
    {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // Create filechooser
    jfc.setDialogTitle(dTitle);
    jfc.setAcceptAllFileFilterUsed(false);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
    jfc.addChoosableFileFilter(filter);
    int returnValue = jfc.showOpenDialog(null); // open the open file dialog
    if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = jfc.getSelectedFile();
            gFilePath = selectedFile.getAbsolutePath();
        }
        else
        {
            String usermess = "No file selected! \nExiting program.";
            userMessage(usermess);
            System.exit(0);
        }
        return gFilePath;
    }

    /**
     * @param usermess
     */
    public void userMessage(String usermess)
    {
        JFrame frame;
        frame = new JFrame();
        JOptionPane.showMessageDialog(frame, usermess);
        return;
    }

    /**
    * @param gFilePath
    * @return SaveFilePath
    */  
    public String getSaveFilePath(String gFilePath)
    {
        SaveFilePath = gFilePath;
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String date = dateObj.format(dateformatter);
        LocalTime timeObj = LocalTime.now();
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("_HHmm");
        String time = timeObj.format(timeformatter);
        String bort = SaveFilePath.replace(".csv","_");
        SaveFilePath = bort.concat(date + time + ".csv");
        //System.out.println(SaveFilePath);
        return SaveFilePath;
    }

    public void HospitalfromUID()
    {
        /**int progressCounter = 1;
        float fp;
        fp = sourcefiles.size();
        float fp1;
        float fp2;**/
        MutableInt yepp = new MutableInt();
        MutableInt nope = new MutableInt();
        //String taskMessage = "Finding regions.";
        //PMonitor pm = new PMonitor();
        //pm.initPM(taskMessage);
        //pm.start();
        for(Entry<Integer, SourceFile> uid:sourcefiles.entrySet())
        {
            int iUID = uid.getKey();
            searchforRegion(iUID, yepp, nope);
            /**while(progressCounter <= fp)
            {
            fp1 = progressCounter / fp;
            fp2 = fp1 * 100;
            int xp = Math.round(fp2);
            System.out.println(xp);
            pm.updatePM(xp);
            }
            progressCounter++;
            **/
        }
        storevalueIU.put("Matched", yepp);
        storevalueIU.put("No match", nope);
        return;
        
    }

    /**
    * @param sUID
    * @param yepp
    * @param nope
    * @return
    * @throws NosuchElementException
    *
    */
    public MutableInt searchforRegion(int iUID, MutableInt yepp, MutableInt nope)
        throws NoSuchElementException
        {
            SourceFile shospital = sourcefiles.get(iUID);
            String SFhospital = shospital.getHospital();
            int UID = shospital.getUID();
            String sampletype = shospital.getsampleType();
            String samplenumber = shospital.getsampleNumber();
            String sampledate = shospital.getsampleDate();
            //String study = shospital.getStudy();
            String country = shospital.getCountry();
            String hospital = SFhospital;
            if(hospitalfiles.containsKey(SFhospital))
            {
                HospitalFile r = hospitalfiles.get(SFhospital);
                String region = r.getRegion();
                //MatchedFile matchedfile = new MatchedFile(UID, sampletype, samplenumber, sampledate, study, country, hospital, region);
                MatchedFile matchedfile = new MatchedFile(UID, sampletype, samplenumber, sampledate, country, hospital, region);
                matchedfiles.put(UID, matchedfile);
                yepp.increment();
                return yepp;
                
            }
            else
            {
                String region = "N/A";
                //MatchedFile matchedfile = new MatchedFile(UID, sampletype, samplenumber, sampledate, study, country, hospital, region);
                MatchedFile matchedfile = new MatchedFile(UID, sampletype, samplenumber, sampledate, country, hospital, region);
                matchedfiles.put(UID, matchedfile);
                nope.increment();
                return nope;
            }
        }
        

    public void MatchedFileContent() throws Exception
    {
        //String feedString = "UID;Sample Type;(NOPHO) Provnummer;() Provtagningsdatum;(NOPHO) Studie;() Country of Origin;() Hospital;()Region\n";
        String feedString = "UID;Sample Type;(NOPHO) Provnummer;Provtagningsdatum;Country of Origin;Hospital;Region\n";
        writeToFile(SaveFilePath, feedString);
        TreeMap<Integer, MatchedFile> sorted = new TreeMap<>();
        sorted.putAll(matchedfiles);
        for(Entry<Integer, MatchedFile> muid:sorted.entrySet())
        {
            MatchedFile m = muid.getValue();
            feedString = m.getUID()+";"+m.getsampleType()+";"+m.getsampleNumber()+";"+m.getsampleDate()+";"+m.getCountry()+";"+m.getHospital()+";"+m.getRegion()+"\n";
            writeToFile(SaveFilePath, feedString);
        }
                
        String usermess = "Number of lines in export: " + sourcefiles.size() + "\n\n" + storevalueIU.get("Matched") + " matches. " + storevalueIU.get("No match") + " unmatched.";
        userMessage(usermess);
        return;
    }
        
    /**  
    * @param SaveFilePath
    * @param feedString
    * 
    * @throws Exception
    * @return
    */
    public void writeToFile(String SaveFilePath, String feedString) throws Exception
    {
        
        File file = new File(SaveFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true)))
            {
                if (!file.exists())
                {
                    file.createNewFile();
                }
                writer.write(feedString);
                writer.close();
            }   
        catch (Exception e)
        {
            e.printStackTrace();
            //System.err.println("Could not write file!");
        }
       return;
    }
 
// End of file
}

