package domain;

/**
 * Created by yuxiao on 16/4/19.
 */
public class Vehicle {
    // 信息 plate,plate_color,local_code,industry_code,xingzheng_code,yehu_code,now_local_code


    private String plate;
    private int plate_color;
    private int local_code;
    private int industry_code;
    private int xingzheng_code;
    private int yehu_code;
    private int now_local_code;
    private Integer typeself_catalogue;
    private Integer manage_catalogue;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getPlate_color() {
        return plate_color;
    }

    public void setPlate_color(int plate_color) {
        this.plate_color = plate_color;
    }

    public int getLocal_code() {
        return local_code;
    }

    public void setLocal_code(int local_code) {
        this.local_code = local_code;
    }

    public int getIndustry_code() {
        return industry_code;
    }

    public void setIndustry_code(int industry_code) {
        this.industry_code = industry_code;
    }

    public int getXingzheng_code() {
        return xingzheng_code;
    }

    public void setXingzheng_code(int xingzheng_code) {
        this.xingzheng_code = xingzheng_code;
    }

    public int getYehu_code() {
        return yehu_code;
    }

    public void setYehu_code(int yehu_code) {
        this.yehu_code = yehu_code;
    }

    public int getNow_local_code() {
        return now_local_code;
    }

    public void setNow_local_code(int now_local_code) {
        this.now_local_code = now_local_code;
    }

    public Integer getTypeself_catalogue() {
        return typeself_catalogue;
    }

    public void setTypeself_catalogue(Integer typeself_catalogue) {
        this.typeself_catalogue = typeself_catalogue;
    }

    public Integer getManage_catalogue() {
        return manage_catalogue;
    }

    public void setManage_catalogue(Integer manage_catalogue) {
        this.manage_catalogue = manage_catalogue;
    }
}
