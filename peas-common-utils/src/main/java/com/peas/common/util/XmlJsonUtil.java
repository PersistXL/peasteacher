package com.peas.common.util;

import java.io.StringReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.peas.common.base.Objects;
import com.peas.hsf.tool.GsonUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * Created by xiekunliang on 2017/3/3.
 *
 * 不支持复杂XML转换
 *
 */
public class XmlJsonUtil {

    public static String xmlToJson(String xml) {
        return new Gson().toJson(xmlToMap(xml));
    }

    public static String jsonToXml(String json) {
        return mapToXml(GsonUtil.parseToMap(json), "xml");
    }

    public static String mapToXml(Map<String, Object> map){
        return mapToXml(map, "xml");
    }

    public static String jsonToXml(String json, String root) {
        return mapToXml(GsonUtil.parseToMap(json), root);
    }


    public static Map<String, Object> xmlToMap(String xml){
        Map<String, Object> map = new HashMap<>();
        try {
            SAXReader sax = new SAXReader();
            Document doc = sax.read(new StringReader(xml));
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element ele : list) {
                map.put(ele.getName(), ele.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String mapToXml(Map<String, Object> value, String root){
        String xml = "";
        try {
            Map<String, Object> map = Maps.newHashMap();
            if(!Objects.isNullOrEmpty(value)){
                map.putAll(value);
            }
            XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            xStream.alias(root, Map.class);
            xStream.registerConverter(new MapEntryConverter());
            xml = xStream.toXML(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }


    private static class MapEntryConverter implements Converter {

        @Override
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(Objects.toString(entry.getKey()));
                writer.setValue(Objects.toString(entry.getValue()));
                writer.endNode();
            }
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Map<String, Object> map = new HashMap<>();
            while(reader.hasMoreChildren()) {
                reader.moveDown();
                map.put(reader.getNodeName(), reader.getValue());
                reader.moveUp();
            }
            return map;
        }

        @Override
        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }
    }

}
