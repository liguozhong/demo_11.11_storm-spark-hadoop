package liguozhong.bigdata.streaming.storm.trident.state.hbase.util;

import java.util.HashMap;
import java.util.Map;
import storm.trident.state.JSONNonTransactionalSerializer;
import storm.trident.state.JSONOpaqueSerializer;
import storm.trident.state.JSONTransactionalSerializer;
import storm.trident.state.Serializer;
import storm.trident.state.StateType;

/**
 * 二级配置
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:14:52
 */
@SuppressWarnings("serial")
public class TridentConfig<T> extends TupleTableConfig {
    @SuppressWarnings("rawtypes")
    public static final Map<StateType, Serializer> DEFAULT_SERIALZERS = new HashMap<StateType, Serializer>() {
                                                                          {
                                                                              put(StateType.NON_TRANSACTIONAL, new JSONNonTransactionalSerializer());
                                                                              put(StateType.TRANSACTIONAL, new JSONTransactionalSerializer());
                                                                              put(StateType.OPAQUE, new JSONOpaqueSerializer());
                                                                          }
                                                                      };
    private int                                    stateCacheSize     = 1000;
    private Serializer<T>                          stateSerializer;
    public TridentConfig(String table) {
        super(table);
    }
    public int getStateCacheSize() {
        return stateCacheSize;
    }
    public void setStateCacheSize(int stateCacheSize) {
        this.stateCacheSize = stateCacheSize;
    }
    public Serializer<T> getStateSerializer() {
        return stateSerializer;
    }
    public void setStateSerializer(Serializer<T> stateSerializer) {
        this.stateSerializer = stateSerializer;
    }
}
