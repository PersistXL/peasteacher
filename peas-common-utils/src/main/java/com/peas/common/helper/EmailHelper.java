//package com.peas.common.helper;
//
//import com.google.common.collect.Maps;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.util.Map;
//
///**
// * Email辅助
// * Created by duan on 2015/8/31.
// */
//public class EmailHelper {
//
//    private String emailTplPath;
//
//    private Document document;
//
//    private Map<String, String> values = Maps.newHashMap();
//
//
//    private static final String CONFIG = "config";
//    /**
//     * CSS与图片
//     */
//    private static final String STYPE = "stype";
//
//    /**
//     * 附件
//     */
//    private static final String FILE = "file";
//
//
//    private EmailHelper(String emailTplPath) {
//        this.emailTplPath = emailTplPath;
//    }
//
//    public String create() {
//        document = createDocument();
//        return null;
//    }
//
//    public EmailHelper set(String key, String value) {
//        document.select("[data-name='"+key+"']")
//    }
//
//    private Document createDocument() {
//        return Jsoup.parse(emailTplPath, "utf-8");
//    }
//
////    private void dealConfig() {
////        Elements element = document.select(CONFIG);
////        Elements styles = element.select("[type='" + STYPE + "']");
////        for (Element e : styles) {
////            String cid = e.attr("cid");
////            String path = emailTplPath + "/" + e.text();
////        }
////    }
//
//    public static String parseContent(String emailTplPath) {
//        return new EmailHelper(emailTplPath).create();
//    }
//}
