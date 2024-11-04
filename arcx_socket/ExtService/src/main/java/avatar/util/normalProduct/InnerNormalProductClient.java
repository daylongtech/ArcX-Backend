package avatar.util.normalProduct;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**

 */
public class InnerNormalProductClient extends WebSocketClient {
 public InnerNormalProductClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  
  InnerNormalProductDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  
  InnerNormalProductUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  
  InnerNormalProductDealUtil.socketClose(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onError(Exception e) {
  
  InnerNormalProductDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
