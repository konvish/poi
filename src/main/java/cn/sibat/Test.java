package cn.sibat;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by kong on 2016/9/29.
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        int index = 0;
        while (ConfigureStatic.getProperty("ip").split(",").length > index+1){
            Document proxy = PoiCollect.proxy("http://www.ifeng.com/",index);
            if (proxy == null){
                System.out.println("err:"+index);
            }else
                System.out.println("success:"+index);
            index++;
        }
    }
}
