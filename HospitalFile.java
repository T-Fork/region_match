
public class HospitalFile 
{

        private String country;
        private String hospital;
        private String region;    
        
    /**
     * Create a new instance of this class
    * @param region
    * @param country
    * @param hospital
     */
    public HospitalFile(String hospital, String country, String region)
    {
       this.region = region;
       this.country = country;
       this.hospital = hospital;
    }

    /**
     * get Region
     * @return 
     */
    public String getRegion()
    {
        return region;
    }/**
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
