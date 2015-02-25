package liguozhong.bigdata.streaming.storm.trident.state.hbase.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import backtype.storm.tuple.Tuple;

/**
 * 一级配置
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:14:52
 */
@SuppressWarnings("serial")
public class TupleTableConfig implements Serializable {
    public static final long DEFAULT_INCREMENT = 1L;
    private String           tableName;
    private boolean          batch             = true;
    protected boolean        writeToWAL        = true; // 是否开启hbase WAL 日志机制
    private long             writeBufferSize   = 0L;
    // ------------------分割线-------------------------
    public TupleTableConfig(String table) {
        this.tableName = table;
    }
    public String getTableName() {
        return tableName;
    }
    public boolean isBatch() {
        return batch;
    }
    public void setBatch(boolean batch) {
        this.batch = batch;
    }
    public void setWriteToWAL(boolean writeToWAL) {
        this.writeToWAL = writeToWAL;
    }
    public boolean isWriteToWAL() {
        return writeToWAL;
    }
    public void setWriteBufferSize(long writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }
    public long getWriteBufferSize() {
        return writeBufferSize;
    }
}
