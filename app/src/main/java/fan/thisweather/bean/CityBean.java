package fan.thisweather.bean;

/**
 * Created by fan on 2016/5/16.
 */
public class CityBean {

    private String cityName;
    private String districtName;
    private String oldDistrictName;

    public CityBean(String cityName, String districtName, String oldDistrictName) {
        this.cityName = cityName;
        this.districtName = districtName;
        this.oldDistrictName = oldDistrictName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getOldDistrictName() {
        return oldDistrictName;
    }

    public void setOldDistrictName(String oldDistrictName) {
        this.oldDistrictName = oldDistrictName;
    }
}
