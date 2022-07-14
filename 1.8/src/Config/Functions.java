package Config;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Functions {



    public static class HeroTab{

        public RarityCard rarityCard;
        public TabCard tabCard;
        public String name;
        public Boolean clicked;


        public HeroTab(String _name,RarityCard _rarityCard, TabCard _tabCard){
            name = _name;
            rarityCard = _rarityCard;
            tabCard =_tabCard;


        }

        public String info(){

            System.out.println(name+" \\ "+ rarityCard.minMaxLocResult.maxLoc+" \\ "+ tabCard.minMaxLocResult.maxLoc + " \\     energy: "+tabCard.energy);
            return name+" \\ "+ rarityCard.minMaxLocResult.maxLoc+" \\ "+ tabCard.minMaxLocResult.maxLoc+ " \\     energy: "+tabCard.energy;
        }




    }

    public static  class TabCard extends Mat {
        public Mat mat;
        public Core.MinMaxLocResult minMaxLocResult;
        public int energy;

        public TabCard(Mat _mat, Core.MinMaxLocResult _minMaxLocResult){

            mat = _mat;
            minMaxLocResult = _minMaxLocResult;
        }
    }

    public static class RarityCard{
        public Mat mat;
        public Core.MinMaxLocResult minMaxLocResult;

        public RarityCard(Mat _mat, Core.MinMaxLocResult _minMaxLocResult){

            mat = _mat;
            minMaxLocResult = _minMaxLocResult;
        }


    }


    public static void CutImage(String sourcePath,String outPath,int x,int y, int w, int h) throws IOException {

try {



        BufferedImage source = ImageIO.read(new File(sourcePath));
        if (y+h <= source.getHeight())
        {
            if (y < source.getHeight()
                    && y != source.getHeight()
                    && y > 0
                    && h > 0
                    && w > 0){

            System.out.println("Erro no 1");
                System.out.println(source.getWidth()+" / "+ source.getHeight());
                System.out.println(x+" / "+ y);
                System.out.println(w+" / "+ h);
            BufferedImage tabOut = source.getSubimage(x,y,w,h-1);
            ImageIO.write(tabOut,"png",new File(outPath));}

        }
        else{
            System.out.println("Erro no 2");
            BufferedImage tabOut = source.getSubimage(x,y,w,source.getHeight()-y);
            ImageIO.write(tabOut,"png",new File(outPath));
            }
}
catch (Error error){

    System.out.println("Aquele erro ao cortar T-T");
    return;
}
    }



    public static Mat Screenshot(String filepath) throws AWTException, IOException, InterruptedException {
try{
        Robot robot = new Robot();
        BufferedImage printer = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height));
        ImageIO.write(printer,"png",new File(filepath+".png"));
        //System.out.println("> Screenshot Tirada... cooldown de 1S");
        Thread.sleep(1000);}catch (Error error)
{
    error.printStackTrace();
}
        return Imgcodecs.imread(filepath);


    }
    public static Mat Screenshot(String filepath,int initX,int initY,int width, int height) throws AWTException, IOException, InterruptedException {
        try{
        Robot robot = new Robot();
        BufferedImage printer = robot.createScreenCapture(new Rectangle(initX,initY,width,height));
        ImageIO.write(printer,"png",new File(filepath+".png"));
        System.out.println("> Screenshot Tirada...");
        }
        catch (Error error)
    {
        error.printStackTrace();



    }
        return Imgcodecs.imread(filepath);
    }


    public static int Energy(TabCard tabCardMat){


        Mat targetBar = Imgcodecs.imread("targets\\50energy.png");
        Mat out = new Mat();
        Imgproc.matchTemplate(tabCardMat,targetBar,out,3);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(out);

        return 0;
    }

}
