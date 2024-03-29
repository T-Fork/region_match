
public class SourceFile {

    //private String UID;
    private int UID;
    private String sampletype;
    private String samplenumber;
    private String sampledate;
    private String country;
    private String hospital;

    /**
     * Create a new instance of this class
    * @param UID
    * @param sampletype
    * @param samplenumber
    * @param sampledate
    * @param country
    * @param hospital
     */
public SourceFile(int UID, String sampletype, String samplenumber, String sampledate, String country, String hospital)
{
    this.UID = UID;
    this.sampletype = sampletype;
    this.samplenumber = samplenumber;
    this.sampledate = sampledate;
    this.country = country;
    this.hospital = hospital;
}

/**
 * get 
 * @return 
 */
public int getUID()
{
    return UID;
}

/**
 * get 
 * @return 
 */
public String getsampleType()
{
    return sampletype;
}

/**
 * get 
 * @return 
 */
public String getsampleNumber()
{
    return samplenumber;
}

/**
 * get 
 * @return 
 */
public String getsampleDate()
{
    return sampledate;
}

/**
 * get 
 * @return 
 */
public String getCountry()
{
    return country;
}

/**
 * get hospital
 * @return
 */
public String getHospital()
{
    return hospital;
}
}