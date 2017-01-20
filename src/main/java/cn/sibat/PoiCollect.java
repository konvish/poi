package cn.sibat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;

/**
 * poi爬虫
 * Created by kong on 2016/9/28.
 */
public class PoiCollect {
    private static int index = 0;

    public static void main(String[] args) {
        final String url = "http://www.poi86.com/poi/district/";
////        for (int i = 0; i < 24; i++) {
////            final int finalI = i;
////            Thread thread = new Thread(new Runnable() {
////                public void run() {
////                    int index = 2912+finalI;
////                    dealPage(url + index + "/", finalI);
////                }
////            });
////            thread.run();
////        }
        for (int i = 2917; i < 2919; i++) {
            dealPage(url + i + "/");
        }
//        for (int i = 68; i < 288; i++) {
//            try {
//                crawl("http://www.poi86.com/poi/district/2916/" + i + ".html");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /**
     * 获取总页数
     * @param url
     */
    private static void dealPage(String url) {
        try {
            Document document = resultDOM(url + "1.html");
            Elements elements = document.getElementsByClass("panel-body");
            Elements li = elements.select("li");
            String total = "1";
            if (li.size() > 0) {
                total = li.get(li.size() - 1).text().split("/")[1];
            }
            for (int i = 1; i <= Integer.parseInt(total); i++) {
                crawl(url + i + ".html");
            }
        } catch (Exception e) {
            System.out.println("地址不存在");
        }
    }

    /**
     * 爬取一页的数据
     * @param url
     * @throws IOException
     */
    private static void crawl(String url) throws IOException {
        Document document = resultDOM(url);

        Elements elements = document.getElementsByClass("panel-body");
        for (Element element : elements) {
            Elements tr = element.select("tr");
            for (Element td : tr) {
                Elements td1 = td.select("td");
                StringBuilder sb = new StringBuilder();
                int count = 0;
                for (Element el : td1) {
                    count++;
                    sb.append(el.text()).append(",");
                    String href = el.select("a").attr("href");
                    if (href != "" && count < 3)
                        sb.append(getPosition(href)).append(",");
                }
                writeFile(sb.toString());
            }
        }


    }

    /**
     * 获取地点的经纬度
     * @param url
     * @return
     */
    private static String getPosition(String url) {
        String uri = "http://www.poi86.com";
        String data = "";
        try {
            Document document = resultDOM(uri + url);
            Elements elements = document.getElementsByClass("list-group");
            if (elements.size() > 0) {
                Element element = elements.get(0);
                Elements li = element.select("li");
                data = li.get(li.size() - 1).text();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 写文件
     * @param data
     */
    private static void writeFile(String data) {
        try {
            File file = new File("c:/kong/testData/poi/poiMO.txt");
            FileWriter fw = new FileWriter(file, true);
            fw.write(data + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            System.out.println("write fail");
        }
    }

    /**
     * 代理ip，使用resultDOM()
     * @param url
     * @return
     */
    @Deprecated
    private static Document proxyIp(String url) {
        String[] ips = ConfigureStatic.getProperty("ip").split(",");
        Document document = null;
        Random ran = new Random();
        int length = ips.length;
        int i = ran.nextInt(length);
        String[] split = ips[i].split(":");
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1])));
            Connection connect = Jsoup.connect(url);
            connect.proxy(proxy);
            document = connect.get();
        } catch (Exception e) {
            System.out.println("连接失败:ip" + split[0] + " port：" + split[1]);
        }
        //重试次数
        int count = 0;
        while (document == null) {
            document = proxyIp(url);
            count++;
            if (count > 20)
                break;
        }
        return document;
    }

    /**
     * 代理ip，不会轮询ip，使用resultDOM
     * @param url
     * @return
     */
    @Deprecated
    private static Document proxy(String url) {
        String[] ips = ConfigureStatic.getProperty("ip").split(",");
        Document document = null;
        int length = ips.length;
        if (index + 1 >= length) {
            index = 0;
        }
        String[] split = ips[index].split(":");
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1])));
            Connection connect = Jsoup.connect(url);
            connect.proxy(proxy);
            connect.timeout(12000);
            document = connect.get();
        } catch (Exception e) {
            index++;
            System.out.println("连接失败:ip" + split[0] + " port：" + split[1] + ",index" + index);
        }

        return document;
    }

    /**
     * 轮询ip
     * @param url
     * @return
     */
    private static Document resultDOM(String url){
        String[] ips = ConfigureStatic.getProperty("ip").split(",");
        Document document = null;
        int length = ips.length;
        while (document == null){
            document = proxy(url,index);
            index++;
            if (index + 1 >= length) {
                index = 0;
            }
        }
        return document;
    }

    /**
     * ip代理
     * @param url
     * @param in
     * @return
     */
    static Document proxy(String url,int in) {
        String[] ips = ConfigureStatic.getProperty("ip").split(",");
        Document document = null;
        String[] split = ips[in].split(":");
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1])));
            Connection connect = Jsoup.connect(url);
            connect.proxy(proxy);
            connect.timeout(12000);
            document = connect.get();
        } catch (Exception e) {
            System.out.println("连接失败:ip" + split[0] + " port：" + split[1] + ",index" + in);
        }

        return document;
    }
}
