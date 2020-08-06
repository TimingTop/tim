//package com.test;
//
//
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.util.concurrent.ExecutorService;
//
///**
// *
// *
// *
// * 多 selector 多线程 的 NIO
// *
// *
// *
// */
//
//
//
//
//public class MultiThreadReactor implements Runnable {
//
//    int cpus = Runtime.getRuntime().availableProcessors() + 3;
//
//    int selectorNums;
//
//    Selector[] selectorArr;
//
//
//    ExecutorService executorService;
//    ServerSocketChannel serverSocketChannel;
//    Integer currentSelector;
//
//
//    MultiThreadReactor(ServerSocketChannel serverSocketChannel) {
//        this.serverSocketChannel = serverSocketChannel;
//
//    }
//
//    private void dispatch(SelectionKey key) {
//        if (key == null || key.attachment() == null) {
//            return;
//        }
//
//
//
//
//    }
//
//
//}
