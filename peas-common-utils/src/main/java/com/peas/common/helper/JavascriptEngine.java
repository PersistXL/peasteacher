package com.peas.common.helper;

import com.alibaba.fastjson.JSON;
import com.peas.common.base.Resources;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by duanyihui on 2016/7/25.
 */
public class JavascriptEngine {

    public static final NashornScriptEngine engine = Inner.engine;

    public static boolean debugger = false;

    public static void loadJs(String js) {
        if (!debugger) {
            loadJs(Resources.getRootPath(), js);
        } else {
            String rootPath = Resources.getRootPath();
            rootPath = rootPath.replace("target/classes", "src/main/resources");
            loadJs(rootPath, js);
        }
    }

    public static void loadJs(String root, String js) {
        engine.put("rootPath", root);
        eval(String.format("load('%s/%s')", root, js));
    }

    public static Object eval(String js) {
        try {
            return engine.eval(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeFunction(String functionName, Object... values) {
        try {
            return engine.invokeFunction(functionName, values);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object call(String method, Object... args) {
        String tpl = method + ".call(this,";
        for (Object arg : args) {
            if (arg != null) {
                tpl += JSON.toJSONString(arg) + ",";
            } else {
                tpl += "null,";
            }
        }
        tpl = tpl.substring(0, tpl.length() - 1);
        tpl += ");";
        return eval(tpl);
    }

    private static class Inner {
        private static ScriptEngineManager manager = new ScriptEngineManager();
        private static NashornScriptEngine engine = (NashornScriptEngine) manager.getEngineByName("Nashorn");

        static {
            try {
                engine.eval("var global=this;");
                engine.eval("load('nashorn:mozilla_compat.js');");
                engine.eval("this.importScripts = function (src) {return load(rootPath + \"/\" + src);}");
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
    }
}
