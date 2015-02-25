package liguozhong.bigdata.streaming.storm.trident.state.hbase;

import java.util.Map;

import liguozhong.bigdata.streaming.storm.trident.state.hbase.util.TridentConfig;
import org.apache.log4j.Logger;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.StateType;
import storm.trident.state.map.CachedMap;
import storm.trident.state.map.MapState;
import storm.trident.state.map.NonTransactionalMap;
import storm.trident.state.map.OpaqueMap;
import storm.trident.state.map.SnapshottableMap;
import storm.trident.state.map.TransactionalMap;
import backtype.storm.task.IMetricsContext;
import backtype.storm.tuple.Values;

/**
 * Hbase factory
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:14:52
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class HBaseAggFactory implements StateFactory {
    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        HBaseAggState state = new HBaseAggState(config);
        CachedMap c = new CachedMap(state, config.getStateCacheSize());
        MapState ms;
        if (type == StateType.NON_TRANSACTIONAL) {
            ms = NonTransactionalMap.build(c);
        } else if (type == StateType.OPAQUE) {
            ms = OpaqueMap.build(c);
        } else if (type == StateType.TRANSACTIONAL) {
            ms = TransactionalMap.build(c);
        } else {
            throw new RuntimeException("指定的state类型错误: " + type);
        }
        return new SnapshottableMap(ms, new Values("$GLOBAL$"));
    }
    // -----------------------分割线---------------------------------
    private static final Logger LOG = Logger.getLogger(HBaseAggFactory.class);
    private StateType           type;
    private TridentConfig       config;
    public HBaseAggFactory(final TridentConfig config, final StateType type) {
        this.config = config;
        this.type = type;
        if (config.getStateSerializer() == null) {
            config.setStateSerializer(TridentConfig.DEFAULT_SERIALZERS.get(type));
            if (config.getStateSerializer() == null) LOG.error("HBaseAggFactory序列化失败: " + type);
        }
    }
}
