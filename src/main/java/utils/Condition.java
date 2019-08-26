package utils;

public enum Condition {

    TORNADO(0, "tornado"),
    THUNDERSTORMS(4, "thunderstorms"),
    RAIN(12, "rain"),
    SUNNY(32,"sunny"),
    MIXED_RAIN_SNOW(5, "mixed rain and snow"),
    MIXED_RAIN_HAIL(35, "mixed rain and hail"),
    HEAVY_RAIN(40, "heavy rain"),
    SCATTERED_THUNDERSHOWERS(47, "scattered thundershowers");

    private int code;
    private String desc;

    Condition(int code, String description){
        this.code = code;
        this.desc = description;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
