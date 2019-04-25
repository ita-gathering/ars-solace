package com.approval;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An example that performs GETs from multiple threads.
 *
 */
public class ClientMultiThreadedExecution {

    public static void main(String[] args) throws Exception {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            String[] userNames = new String[]{"ee","admin","uu"};

            GetThread[] threads = new GetThread[userNames.length];
            for (int i = 0; i < threads.length; i++) {
                HttpPatch httpPatch = new HttpPatch("http://liangoc-w10:8080/activity/5cc1ce8b3b2dc43c3c86f16a");
                StringEntity entity = new StringEntity("{\"userName\": \"" + userNames[i] + "\"}",
                        ContentType.create("application/json", Consts.UTF_8));
                httpPatch.setEntity(entity);
                threads[i] = new GetThread(httpclient, httpPatch, i + 1);
            }

            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }

        } finally {
            httpclient.close();
        }
    }

    /**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {
        final String SETTIME = "2019-04-25 23:46:00";
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpPatch httpget;
        private final int id;

        public GetThread(CloseableHttpClient httpClient, HttpPatch httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
            Timestamp currentTimestamp;
            try {
                System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response;
                Timestamp setTime = new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(SETTIME).getTime());
                System.out.println("set time " + setTime);
                while (true) {
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date now = calendar.getTime();
                    currentTimestamp = new java.sql.Timestamp(now.getTime());

                    if (currentTimestamp.after(setTime)) {
                        now = calendar.getTime();
                        currentTimestamp = new java.sql.Timestamp(now.getTime());
                        response = httpClient.execute(httpget, context);
                        break;
                    }

                }
                System.out.println("request send time " + currentTimestamp);
                try {
                    System.out.println(id + " - get executed");
                    // get the response body as an array of bytes
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String bytes = EntityUtils.toString(entity);
                        System.out.println("================================");
                        System.out.println(id + " - " + bytes + " bytes read");
                        System.out.println("================================");
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }

}