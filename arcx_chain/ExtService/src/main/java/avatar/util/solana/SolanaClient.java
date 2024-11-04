package avatar.util.solana;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**

 */
public class SolanaClient extends WebSocketClient {
 public SolanaClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  
  SolanaDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  
  SolanaDealUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  
  SolanaDealUtil.socketClose();
 }

 @Override
 public void onError(Exception e) {
  
  SolanaDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
