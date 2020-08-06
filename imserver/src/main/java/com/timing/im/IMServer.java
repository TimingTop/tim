package com.timing.im;

import com.timing.im.container.ConnectionManagerImpl;
import com.timing.im.container.Module;

import java.util.LinkedHashMap;
import java.util.Map;

public class IMServer {

    private static IMServer instance;
    private ClassLoader loader;

    private Map<Class, Module> modules = new LinkedHashMap<>();


    public IMServer() {
        if (instance != null) {
            throw new IllegalStateException("A server is already running");
        }
        instance = this;
//        start();
    }

    public void start() {
        loader = Thread.currentThread().getContextClassLoader();
        // 1. loadModules()
        loadModules();
        startModules();

    }
    private void loadModules() {
        // 先处理 连接
        loadModule(ConnectionManagerImpl.class.getName());
    }

    private void startModules() {
        for (Module module : modules.values()) {
            module.start();
        }
    }


    private void loadModule(String module) {
        try {
            Class<Module> moduleClass = (Class<Module>) loader.loadClass(module);
            Module mod = moduleClass.getConstructor().newInstance();
            this.modules.put(moduleClass, mod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("------ begin -------");


        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                IMServer server = new IMServer();
                server.start();
            }
        });

        a.start();
        a.join();

        System.out.println("------ end ------------");

        // netty session mina session
        // https://blog.csdn.net/xiao__gui/article/details/39180209?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase






        // https://blog.csdn.net/d1240673769/article/details/102632178
        // nc -u 127.0.0.1 9988
    }

}
