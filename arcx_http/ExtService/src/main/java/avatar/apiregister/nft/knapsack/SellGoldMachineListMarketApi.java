package avatar.apiregister.nft.knapsack;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.NftHttpCmdName;
import avatar.net.session.Session;
import avatar.service.nft.NftKnapsackService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class SellGoldMachineListMarketApi extends SystemEventHttpHandler<Session> {
    protected SellGoldMachineListMarketApi() {
        super(NftHttpCmdName.SELL_GOLD_MACHINE_LIST_MARKET);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.sellGoldMachineListMarket(map, session));
        cachedPool.shutdown();
    }
}
