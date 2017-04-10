package csuft.ppx.indoorlocation.beacon.utils;

/**
 * Default值，静态
 *
 * Created by zf on 2017/4/4.
 */

public class DefaultStaticValues {
    /**
     * String和byte转换的格式
     */
    public static final String STRING_BYTES_CONVERT = "ISO-8859-1";
    /**
     * Beacon的identifier
     */
    public static final String BEACON_REGION_IDENTIFIER = "Beacon";
    /**
     * 默认的identifier
     */
    public static final String DEFAULT_BEACON_REGION_IDENTIFIER = "csuft.ppx.indoorlocation";
    /**
     * 默认的硬件版本号
     */
    public static final int DEFAULT_BEACON_HARDWARE_VERSION = -1;
    /**
     * 默认的软件版本号：主
     */
    public static final int DEFAULT_BEACON_FIRMWARE_VERSION_MAJOR = -1;
    /**
     * 默认的软件版本号：次
     */
    public static final int DEFAULT_BEACON_FIRMWARE_VERSION_MINOR = -1;
    /**
     * 默认的错误mac地址
     */
    public static final String DEFAULT_BEACON_DEVICE_ADDRESS = "FALSE_DEVICE_ADDRESS";
    /**
     * 默认的错误mac地址
     */
    public static final long DEFAULT_BEACON_DEVICE_ADDRESS_LONG = 0;
    /**
     * 默认的设备名称
     */
    public static final String DEFAULT_BEACON_DEVICE_NAME = "Unknown";
    /**
     * 默认的错误proximity uuid
     */
    public static final String DEFAULT_BEACON_PROXIMITY_UUID = "FALSE_PROXIMITY_UUID";
    /**
     * 默认的uuid替代名称
     */
    public static final String DEFAULT_BEACON_UUID_REPLACE_NAME = "Unknown";
    /**
     * 默认的错误major
     */
    public static final int DEFAULT_BEACON_MAJOR_FALSE = -1;
    /**
     * 默认的错误minor
     */
    public static final int DEFAULT_BEACON_MINOR_FALSE = -1;
    /**
     * 默认的measuredPower
     */
    public static final int DEFAULT_BEACON_MEASURED_POWER_FALSE = 100;
    /**
     * 默认的rssi
     */
    public static final int DEFAULT_BEACON_RSSI_FALSE = 100;
    /**
     * 默认的距离
     */
    public static final double DEFAULT_BEACON_DISTANCE_FALSE = -1;
    /**
     * 默认的电量
     */
    public static final int DEFAULT_BEACON_BATTERY_FALSE = -1;
    /**
     * 默认的温度
     */
    public static final int DEFAULT_BEACON_TEMPERATURE_FALSE = 100;
    /**
     * 默认的温度更新频率
     */
    public static final int DEFAULT_BEACON_TEMPERATURE_UPDATE_FALSE = -1;
    /**
     * 默认的发送间隔
     */
    public static final int DEFAULT_BEACON_INTERVAL_MILLISECOND_FALSE = -1;
    /**
     * 默认的发送功率
     */
    public static final int DEFAULT_SKY_BEACON_TXPOWER_FALSE = 0x7F;
    /**
     * 默认的是否防篡改
     */
    public static final int DEFAULT_SKY_BEACON_IS_LOCKED_FALSE = -1;
    /**
     * 默认的是否防蹭用
     */
    public static final int DEFAULT_SKY_BEACON_IS_ENCRYPTED_FALSE = -1;
    /**
     * 默认的LED状态
     */
    public static final int DEFAULT_SKY_BEACON_LED_STATE = -1;
    /**
     * 默认的是否seekcy的iBeacon
     */
    public static final int DEFAULT_SKY_BEACON_IS_SEEKCY_IBEACON = -1;
    /**
     * 默认的locked key
     */
    public static final String DEFAULT_SKY_BEACON_LOCKED_KEY_FALSE = "";
    /**
     * 默认的格子使用数量
     */
    public static final int DEFAULT_SKY_BEACON_CELLS_USED_NUM = 0;
    /**
     * 默认的时间戳，ms
     */
    public static final int DEFAULT_SKY_BEACON_TIMESTAMP_MILLISECOND = 0;
    /**
     * 默认光感开关状态
     */
    public static final int DEFAULT_SKY_BEACON_LIGHT_SENSOR_STATE = -1;
    /**
     * 默认光感黑暗门限
     */
    public static final int DEFAULT_SKY_BEACON_LIGHT_SENSOR_DARK_THRESHOLD = 0xFF;
    /**
     * 默认光感值:Lex
     */
    public static final int DEFAULT_SKY_BEACON_LIGHT_SENSOR_VALUE = -1;
    /**
     * 默认光感更新频率：s
     */
    public static final int DEFAULT_SKY_BEACON_LIGHT_SENSOR_UPDATE_SENCOND = -1;
    /**
     * 默认黑暗状态下更新频率：ms
     */
    public static final int DEFAULT_SKY_BEACON_DARK_INTERVAL_MILLISECOND = -1;
    /**
     * 推送权重：最高为1
     */
    public static final float DEFAULT_SKY_BEACON_PUSH_WEIGHT = 0;
}
