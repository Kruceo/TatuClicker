import Config.Config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowStateListener;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static Config.Functions.Screenshot;

public class Main {


    public static int selectTarget = 0;

    public Main() throws IOException, InterruptedException, AWTException {
    }


    public static class config{

        public int id;
        public String source;

        public config(int _id, String _source){
            id = _id;
            source = _source;
        }
    }

    public static class access{

        ExecutorService service = Executors.newFixedThreadPool(1);




        public Boolean access = false;

        Client client = new Client();

        public void run() throws IOException {

            //ExecutorService service = Executors.newSingleThreadExecutor();
            //service.execute(client);
            Thread service = new Thread(()->
                    client.run());


            //service.awaitTermination(10, TimeUnit.SECONDS);
            // service.shutdown();
            System.out.println("service terminated");
            service.start();
            // thread.wait();

        }
        public boolean get(){
            access = client.permitedAccess;
            if (access){
            return true;}
            else {
                return false;
            }
        }


    }


    public static void window()
        {
            WindowMaker windowMaker = new WindowMaker();
            windowMaker.debug();
        }

    public static void start() throws IOException, InterruptedException {

        //window();

        File configTxt = new File("tatu.config");

        if (!configTxt.exists()){
            System.out.println("tatu.config nao existe, config padrao aplicada...");
            return;}
        FileReader reader = new FileReader(configTxt);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String configstxt = "";
        while(bufferedReader.ready()){

            configstxt += bufferedReader.readLine()+"\n";
        }


        String[] configUnits = configstxt.split("\n");

        Config.rarityTarget = configUnits[0].split("==")[1];
        Config.debugMode =
                (Boolean.parseBoolean(configUnits[1].split("==")[1]));
        Config.enableTrashhold =
                (Boolean.parseBoolean(configUnits[2].split("==")[1]));
        Config.trashholdType =
                Integer.parseInt(configUnits[3].split("==")[1]);
        Config.minTrashhold =
                Integer.parseInt(configUnits[4].split("==")[1]);
        Config.maxTrashhold =
                Integer.parseInt(configUnits[5].split("==")[1]);
        Config.matchMethod =
                Integer.parseInt(configUnits[6].split("==")[1]);
        Config.finderTolerance =
                Integer.parseInt(configUnits[7].split("==")[1]);
        Config.loopsInTreasure =
                Integer.parseInt(configUnits[8].split("==")[1]);
        Config.metamaskDelay =
                Integer.parseInt(configUnits[9].split("==")[1]);
        Config.accountsInScreen =
                Integer.parseInt(configUnits[10].split("==")[1]);
        //Config.initialStage =
               // (configUnits[11].split("==")[1]);
        Config.saveAndLoad =
                Boolean.parseBoolean(configUnits[11].split("==")[1]);



        System.out.println("\n----- Tatu.config -----\n");

        System.out.println(Config.class);

        System.out.println(Config.rarityTarget);
        System.out.println(Config.enableTrashhold);
        System.out.println(Config.trashholdType);
        System.out.println(Config.minTrashhold);
        System.out.println(Config.maxTrashhold);
        System.out.println(Config.matchMethod);
        System.out.println(Config.finderTolerance);
        System.out.println(Config.loopsInTreasure);
        System.out.println(Config.metamaskDelay);
        System.out.println(Config.accountsInScreen);
        System.out.println(Config.initialStage);
        System.out.println(Config.saveAndLoad);
        Thread.sleep(2000);
        return;
    }

    public static Boolean access = false;
    public static Loops loop;

    public static void main(String[] args) throws AWTException, InterruptedException, IOException {


        //Client client = new Client();
        //ExecutorService service = Executors.newSingleThreadExecutor();
        //service.execute(client);

       // while (!access){
            //System.out.println("validando...");
            //Thread.sleep(500);
        //}
        System.out.println("validado: "+access);
        start();


        System.out.println("--------------------------LOADING DLL---------------------------- \n");

        String s = System.getProperty("java.library.path");
        String[] libpaths = s.split(";");

        String pathToLib = "tatuClicker_opencv_java454.dll";

        File source = new File("lib\\opencv_java454.dll");
        if (!source.exists()){

            System.out.println("lib nao encontrada");
        }else {

            System.out.println("dll ta safe, confia!");
        }
        System.load(source.getAbsolutePath());
        // System.loadLibrary("lib/opencv_java454");
        System.out.println("Lib carregada");
        System.out.println(Config.loaderStr);



        Thread.sleep(3000);
        //loop.ClickRestButtons("common_icon.png");

        //Screenshot("source");
        //loop.isOnScreen("targets\\home.png");
        //loop.isOnScreen("targets\\character_tab.png");

       // loop.isOnScreen("targets\\metamask_icon.png");


        loop = new Loops();
        try {
            loop.Looper();
        }catch (Error error){
            System.out.println("erro no loop" + error.getMessage());
        }
        //loop.FindAndMove("targets\\lock_slider.png","targets\\lock3.png");
        //loop.ClickWorkButtons("common_icon.png");
        //RemoveDuplicates(loop.heroLocated);

        }
    }







