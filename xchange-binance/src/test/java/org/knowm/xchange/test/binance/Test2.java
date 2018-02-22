package org.knowm.xchange.test.binance;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/18.
 */
public class Test2 {

    public static void  main(String[] args) throws IOException {
     /*   Binance binance = RestProxyFactory.createProxy(Binance.class, "https://api.binance.com");
        try {
            System.out.println(binance.time().getServerTime());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    /*    for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            Long start= System.currentTimeMillis();
                            System.out.println(binance.time().getServerTime()+"#####"+(System.currentTimeMillis()-start) );
                            Thread.sleep(1000L);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }*/


    }
}
