package avatar.util.innoMsg;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**

 */
public class SyncInnoClient extends WebSocketClient {
 public SyncInnoClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  
  SyncInnoDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  
  SyncInnoUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  
  SyncInnoDealUtil.socketClose(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onError(Exception e) {
  
  SyncInnoDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
