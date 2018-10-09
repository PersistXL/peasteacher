package com.peas.common.base;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 迭代器小工具
 *
 * @author duanyihui
 */
public class Eachs {
    /**
     * 私有构造器
     */
    private Eachs() {
    }

    /**
     * 迭代集合
     *
     * @param es 需要迭代的集合
     * @return 迭代器
     */
    public static <V> Each<Integer, V> on(Iterable<V> es) {
        Preconditions.checkArgument(es != null, "argument is null");
        return new EachIterable<V>(es);
    }

    /**
     * 迭代Map
     *
     * @param es 需要迭代的Map
     * @return
     */
    public static <K, V> Each<K, V> on(Map<K, V> es) {
        Preconditions.checkArgument(es != null, "argument is null");
        return new EachMap<K, V>(es);
    }

    /**
     * 迭代器 <p>detailed comment
     *
     * @param <K> 索引
     * @param <V> 值
     * @author duanyihui
     * @see
     * @since 1.0
     */
    public static abstract class Each<K, V> {
        /**
         * 迭代元素回调
         */
        protected Item<K, V> item;

        /**
         * 迭代方法抽象
         *
         * @param item 迭代回调处理器
         * @return 迭代器
         */
        public abstract Each<K, V> each(Item<K, V> item);

        /**
         * 获取迭代器中的值
         *
         * @param index 迭代处理出的值
         * @return 结果获取
         */
        public Object result(int index) {
            Object[] args = this.item.getArguments();
            if (args == null || index > args.length - 1) {
                return null;
            }
            return args[index];
        }
    }

    /**
     * 迭代回调
     *
     * @param <K>
     * @param <V>
     * @author duanyihui
     * @see
     * @since 1.0
     */
    public static abstract class Item<K, V> {
        /**
         * 参数
         */
        protected Object[] arguments;

        /**
         * 是否跳出
         */
        private boolean isbreak = false;

        /**
         * 不定参数
         *
         * @param arguments
         */
        public Item(Object... arguments) {
            this.arguments = arguments;
        }

        /**
         * 循环回调
         *
         * @param k 索引
         * @param v 值
         */
        public abstract void item(K k, V v);

        /**
         * 跳出循环
         */
        protected void breakout() {
            this.isbreak = true;
        }

        private boolean isBreak() {
            return isbreak;
        }

        /**
         * 获取传入的数据Collect
         *
         * @return 数据
         */
        private Object[] getArguments() {
            return this.arguments;
        }
    }

    /**
     * 迭代集合
     *
     * @param <V> 值
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    private static class EachIterable<V> extends Each<Integer, V> {
        /**
         * 集合
         */
        private Iterable<V> it;

        /**
         * 构造器
         *
         * @param it 需要迭代的集合
         */
        public EachIterable(Iterable<V> it) {
            this.it = it;
        }

        /**
         * 进行迭代
         */
        @Override
        public Each<Integer, V> each(Item<Integer, V> item) {
            this.item = item;
            int i = 0;
            for (V v : it) {
                item.item(i, v);
                if (item.isBreak()) {
                    break;
                }
                i++;
            }
            return this;
        }
    }

    /**
     * Map迭代器
     *
     * @param <K> 索引
     * @param <V> 值
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    private static class EachMap<K, V> extends Each<K, V> {
        /**
         * 需要迭代的Map
         */
        private Map<K, V> maps;

        /**
         * 构造器
         *
         * @param maps 需要迭代的map
         */
        public EachMap(Map<K, V> maps) {
            this.maps = maps;
        }

        /**
         * 进行迭代
         */
        @Override
        public Each<K, V> each(Item<K, V> item) {
            this.item = item;
            Set<Entry<K, V>> sets = maps.entrySet();
            for (Entry<K, V> entry : sets) {
                item.item(entry.getKey(), entry.getValue());
                if (item.isBreak()) {
                    break;
                }
            }
            return this;
        }
    }
}
