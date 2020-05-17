import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VcardFileServer {

		static SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
	 	public static void main(String[] arg) throws Exception {
	        HttpServer server = HttpServer.create(new InetSocketAddress(10009), 0);
	        server.createContext("/api/upload", new TestHandler());
	        server.start();
	    }

	    static  class TestHandler implements HttpHandler{
	        @Override
	        public void handle(HttpExchange exchange) throws IOException {
	        	new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try{
	                        String response = "hello world";
	                        //获得查询字符串(get)
	                        String queryString =  exchange.getRequestURI().getQuery();
	                        Map<String,String> queryStringInfo = formData2Dic(queryString);
	                        FileUploadUtil.fileUpload(exchange);
	                        System.out.println(queryStringInfo);
	                        String c = queryStringInfo.get("C");
	                        if(c != null) {
		                        System.out.println("Record Temperature:"+ Float.parseFloat(c)/100 +"℃");
	                        }
	                        System.out.println("Record Time:"+ sdf1.format(new Date(Long.parseLong(queryStringInfo.get("time"))*1000)));
	                        System.out.println("Device:"+ queryStringInfo.get("sn"));
	                        
	                        exchange.sendResponseHeaders(200,0);
	                        OutputStream os = exchange.getResponseBody();
	                        os.write(response.getBytes());
	                        os.close();
	                    } catch (IOException ie) {
	                        ie.printStackTrace();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }).start();
	        }
	    }
	    public static Map<String,String> formData2Dic(String formData ) {
            Map<String,String> result = new HashMap<>();
            if(formData== null || formData.trim().length() == 0) {
                return result;
            }
            final String[] items = formData.split("&");
            Arrays.stream(items).forEach(item ->{
                final String[] keyAndVal = item.split("=");
                if( keyAndVal.length == 2) {
                    try{
                        final String key = URLDecoder.decode( keyAndVal[0],"utf8");
                        final String val = URLDecoder.decode( keyAndVal[1],"utf8");
                        result.put(key,val);
                    }catch (UnsupportedEncodingException e) {}
                }
            });
            return result;
    }
}
