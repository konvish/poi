package cn.sibat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理，动态加载
 * Created by kong on 2016/7/21.
 */
public class ConfigureManager {

    private Properties prop = new Properties();

    public ConfigureManager() {
        try {
            String path = ConfigureManager.class.getClassLoader().getResource("config.properties").getPath();
            InputStream in = new FileInputStream("/C:/kong/poi/src/main/resources/config.properties");
            prop.load(in);
        } catch (Exception e) {
            System.out.println("加载配置文件出错");
        }
    }

    /**
     * 获取配置key对应的value
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 获取对应配置整数类型值
     * 没有对应的key返回0
     * @param key
     * @return
     */
    public Integer getInteger(String key){
        String value = getProperty(key);
        try{
            return Integer.parseInt(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取boolean类型的配置值
     * 没有对应的key返回false
     * @param key
     * @return
     */
    public Boolean getBoolean(String key){
        String value = getProperty(key);
        try{
            return Boolean.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取Long类型的配置值
     * 没有对应的值返回0
     * @param key
     * @return
     */
    public Long getLong(String key){
        String value = getProperty(key);
        try{
            return Long.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取Double类型的配置值
     * 没有对应的key返回0.0
     * @param key
     * @return
     */
    public Double getDouble(String key){
        String value = getProperty(key);
        try{
            return Double.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0.0;
    }

}
