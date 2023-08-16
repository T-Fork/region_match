
public class MatchedFile 
{
    //private String UID;
    private int UID;
    private String sampletype;
    private String samplenumber;
    private String sampledate;
    private String country;
    private String hospital;
    private String region;

    /**
     * Create a new instance of this class
    * @param UID
    * @param sampletype
    * @param samplenumber
    * @param sampledate
    * @param country
    * @param hospital
    * @param region
     */
public MatchedFile(int UID, String sampletype, String samplenumber, String sampledate, String country, String hospital, String region)
{
    this.UID = UID;
    this.sampletype = sampletype;
    this.samplenumber = samplenumber;
    this.sampledate = sampledate;
    this.country = country;
    this.hospital = hospital;
    this.region = region;
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

/**
 * @return
 */
public String getRegion()
{
    return region;
}
}
