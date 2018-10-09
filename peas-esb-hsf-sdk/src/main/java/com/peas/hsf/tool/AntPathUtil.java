package com.peas.hsf.tool;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Created by duanyihui on 2017/3/21.
 */
public class AntPathUtil
{

    private static final HsfAntPathMatcher pathMatcher = new HsfAntPathMatcher();

    public static List<String> getAllMatch(String matcher)
    {
        Set<String> result = Sets.newHashSet();
        try
        {
            URI base = AntPathUtil.class.getResource("/").toURI();
            String newMatcher = base.resolve(matcher).toString();
            Path basePath = Paths.get(base);
            Files.walkFileTree(basePath, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    String path = file.toUri().toString();
                    if (pathMatcher.match(newMatcher, path))
                    {
                        String key = file.toString().replace(basePath.toString(), "").substring(1).replaceAll("\\\\",
                                "/");
                        result.add(key);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Lists.newArrayList(result);
    }

}
