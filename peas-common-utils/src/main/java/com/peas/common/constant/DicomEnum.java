package com.peas.common.constant;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * Created by duan on 2015/12/16.
 */
public enum DicomEnum
{
    UNKNOWN("", 0), CT("CT", 1), MR("MR", 2), PT("PT", 4), CR("CR", 8), DX("DX", 16), XA("XA", 32), MG("MG",
        64), PX("PX", 128), US("US", 256), DSA("DSA", 512), FC("FC", 1024), OCT("OCT", 2048),
    眼底照相机("眼底照相机", 4096),
    裂隙灯("裂隙灯", 8192),
    角膜地形图("角膜地形图", 16384);

    private int value;

    @Getter
    private String name;

    DicomEnum(String name, int value)
    {
        this.value = value;
        this.name = name;
    }

    public int intValue()
    {
        return this.value;
    }

    public static DicomEnum fromName(String name)
    {
        DicomEnum[] enums = values();
        for (int i = 0; i < enums.length; i++)
        {
            if (enums[i].name().equals(name))
            {
                return enums[i];
            }
        }
        return UNKNOWN;
    }

    public static List<String> parseIntValueToNames(int value)
    {
        List<String> names = Lists.newLinkedList();
        DicomEnum[] enums = values();
        for (int i = 1; i < enums.length; i++)
        {
            if ((enums[i].intValue() & value) == enums[i].intValue())
            {
                names.add(enums[i].getName());
            }
        }
        return names;
    }

    public static int parseNamesToIntValue(List<String> names)
    {
        int r = 0;
        for (int i = 0, size = names.size(); i < size; i++)
        {
            DicomEnum e = DicomEnum.fromName(names.get(i));
            if (e != null)
            {
                r = r | e.intValue();
            }
        }
        return r;
    }

    public static DicomEnum fromIntValue(int value)
    {
        DicomEnum[] enums = values();
        for (int i = 0; i < enums.length; i++)
        {
            if (enums[i].intValue() == value)
            {
                return enums[i];
            }
        }
        return UNKNOWN;
    }

    public static int getALLIntValue()
    {
        int result = 0;
        DicomEnum[] enums = values();
        for (int i = 0; i < enums.length; i++)
        {
            int v = enums[i].intValue();
            if (v == -1)
            {
                continue;
            }
            result = result | v;
        }
        return result;
    }
}
