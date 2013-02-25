/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javathinking.batch.example;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author paul
 */
public class TestMe {

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    System.out.println("running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestMe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();

    }
}
