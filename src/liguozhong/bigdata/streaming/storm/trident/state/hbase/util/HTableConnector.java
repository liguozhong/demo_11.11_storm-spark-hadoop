package liguozhong.bigdata.streaming.storm.trident.state.hbase.util;

import java.io.IOException;
import java.io.Serializable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;

/**
 * hbase连接类
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:14:52
 */
@SuppressWarnings("serial")
public class HTableConnector implements Serializable {
    public HTable getTable() {
        return table;
    }
    public void close() {
        try {
            this.table.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            LOG.error("hbase 关闭失败");
        }
    }
    // ---------------分割线-------------------------------
    private static final Logger LOG = Logger.getLogger(HTableConnector.class);
    private Configuration       conf;
    protected HTable            table;
    private String              tableName;
    public HTableConnector(TupleTableConfig conf) throws IOException {
        this.tableName = conf.getTableName();
        this.conf = HBaseConfiguration.create();// 连接hbase配置项
        this.table = new HTable(this.conf, this.tableName);
        if (conf.isBatch()) this.table.setAutoFlush(false, true);
        if (conf.getWriteBufferSize() > 0) this.table.setWriteBufferSize(conf.getWriteBufferSize());
    }
}
