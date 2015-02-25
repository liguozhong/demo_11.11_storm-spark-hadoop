package liguozhong.bigdata.streaming.storm.trident.state.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import liguozhong.bigdata.streaming.storm.trident.state.hbase.util.HTableConnector;
import liguozhong.bigdata.streaming.storm.trident.state.hbase.util.TridentConfig;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import storm.trident.state.OpaqueValue;
import storm.trident.state.Serializer;
import storm.trident.state.StateFactory;
import storm.trident.state.StateType;
import storm.trident.state.TransactionalValue;
import storm.trident.state.map.IBackingMap;

/**
 * hbase state
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:14:52
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class HBaseAggState<T> implements IBackingMap<T> {
    @Override
    public List<T> multiGet(List<List<Object>> keys) {
        List<Get> gets = new ArrayList<Get>(keys.size());
        byte[] rk;
        byte[] cf;
        byte[] cq;
        for (List<Object> k : keys) {
            rk = Bytes.toBytes((String) k.get(0));
            cf = Bytes.toBytes((String) k.get(1));
            cq = Bytes.toBytes((String) k.get(2));
            Get g = new Get(rk);
            gets.add(g.addColumn(cf, cq));
        }
        Result[] results = null;
        try {
            results = connector.getTable().get(gets);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        List<T> rtn = new ArrayList<T>(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            cf = Bytes.toBytes((String) keys.get(i).get(1));
            cq = Bytes.toBytes((String) keys.get(i).get(2));
            Result r = results[i];
            if (r.isEmpty()) {
                rtn.add(null);
            } else {
                rtn.add((T) serializer.deserialize(r.getValue(cf, cq)));
            }
        }
        return rtn;
    }
    @Override
    public void multiPut(List<List<Object>> keys, List<T> vals) {
        List<Put> puts = new ArrayList<Put>();
        for (int i = 0; i < keys.size(); i++) {
            byte[] rk = Bytes.toBytes((String) keys.get(i).get(0));
            byte[] cf = Bytes.toBytes((String) keys.get(i).get(1));
            byte[] cq = Bytes.toBytes((String) keys.get(i).get(2));
            byte[] cv = serializer.serialize(vals.get(i));
            Put p = new Put(rk);
            puts.add(p.add(cf, cq, cv));
        }
        try {
            connector.getTable().put(puts);
            connector.getTable().flushCommits();
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }
    // -----------------------分割线--------------------------------------
    public static StateFactory opaque(TridentConfig<OpaqueValue> config) {
        return new HBaseAggFactory(config, StateType.OPAQUE);
    }
    public static StateFactory transactional(TridentConfig<TransactionalValue> config) {
        return new HBaseAggFactory(config, StateType.TRANSACTIONAL);
    }
    public static StateFactory nonTransactional(TridentConfig<TransactionalValue> config) {
        return new HBaseAggFactory(config, StateType.NON_TRANSACTIONAL);
    }
    private HTableConnector connector;
    private Serializer      serializer;
    public HBaseAggState(TridentConfig config) {
        this.serializer = config.getStateSerializer();
        try {
            this.connector = new HTableConnector(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}