import Config.Config;
import Config.Functions;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import static Config.Functions.*;

public class Main {


    public static int selectTarget = 0;


    public static class config{

        public int id;
        public String source;

        public config(int _id, String _source){
            id = _id;
            source = _source;
        }
    }

    public static void start() throws IOException, InterruptedException {
        File configTxt = new File("tatu.config");

        if (!configTxt.exists()){
            System.out.println("tatu.config nao existe, config padrao aplicada...");
            return;}
        FileReader reader = new FileReader(configTxt);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String configstxt = new String();
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



        System.out.println("\n----- Tatu.config -----\n");
        System.out.println(Config.rarityTarget);
        System.out.println(Config.enableTrashhold);
        System.out.println(Config.trashholdType);
        System.out.println(Config.minTrashhold);
        System.out.println(Config.maxTrashhold);
        System.out.println(Config.matchMethod);
        System.out.println(Config.finderTolerance);
        System.out.println(Config.loopsInTreasure);
        System.out.println(Config.metamaskDelay);
        Thread.sleep(2000);
        return;
    }



    public static void main(String[] args) throws AWTException, InterruptedException, IOException {


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

        String[] path = {"common_icon.png","super_rare_icon.png","legend_icon.png","work_icon.png","work_icon.png"};
        Loops loop = new Loops();
        Thread.sleep(1000);
        //loop.ClickRestButtons("common_icon.png");

        Screenshot("source");
        loop.isOnScreen("targets\\home.png");
        //loop.isOnScreen("targets\\character_tab.png");

       // loop.isOnScreen("targets\\metamask_icon.png");



        try {
            loop.Looper();
        }catch (Error error){

            Main.main(null);
        }
        //loop.FindAndMove("targets\\lock_slider.png","targets\\lock3.png");
        //loop.ClickWorkButtons("common_icon.png");
        //RemoveDuplicates(loop.heroLocated);

        }

}





