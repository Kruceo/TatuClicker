package Config;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
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

    public static Mat img2Mat(BufferedImage in) {
        Mat out;
        byte[] data;
        int r, g, b;

        if (in.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            }
        } else {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 0) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b));
            }
        }
        out.put(0, 0, data);
        Imgcodecs.imwrite("source.png",out);
        return out;
    }

    public static Mat Screenshot(String filepath) throws AWTException, IOException, InterruptedException {
        BufferedImage printer = null;
        try {
            Robot robot = new Robot();
            printer = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
            //ImageIO.write(printer, "png", new File(filepath + ".png"));
            System.out.println("printado");
            Thread.sleep(300);
            return img2Mat(printer);


        } catch (Error error)

        {
            error.printStackTrace();
        }
        return null;
    }


        //assert printer != null;

        //return Imgcodecs.imread(filepath);



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
