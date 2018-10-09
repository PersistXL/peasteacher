package com.peas.common.base.handler;

import com.peas.common.vo.KeyValuePair;

/**
 * @author dyh
 */
public interface KeyValueHandler<K, V> {
    KeyValuePair<K, V> handler(K key, V value);
}
