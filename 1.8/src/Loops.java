
import Config.Config;
import Config.Functions;

import Config.Tasks;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static Config.Config.finderTolerance;
import static Config.Config.matchMethod;
import static Config.Functions.CutImage;
import static Config.Functions.Screenshot;

public class Loops {


    List<Functions.HeroTab> heroLocated = new ArrayList<>() {
    };

    public void ClickWorkButtons(String rarity) throws AWTException, InterruptedException, IOException {

        heroLocated.clear();
        System.out.println("\n---- Clicking to work ----");
        System.out.println("Lista de herois apagada");
        Robot robot = new Robot();
        Tasks.mouseScrol(robot, 1200, 10);
        Thread.sleep(1000);
        Screenshot("source");

        Mat image = Imgcodecs.imread("source.png");
        //Mat image = Imgcodecs.imread("test.png");
        Mat target = Imgcodecs.imread("targets\\rarity\\" + rarity);
        //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("target carregado");
        Mat originalImage = image.clone();

        Mat outputRarity = new Mat();

        int machMethod = Imgproc.TM_CCOEFF;


        Mat tab = new Mat();
        Mat workTarget = new Mat();
        Mat energyTarget = new Mat();
        Mat workOut = new Mat();
        Mat energyOut = new Mat();


        int scanTimes = 5;
        for (int i = 0; i < scanTimes; i++) {
            Screenshot("source");
            try {
                Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
            } catch (Exception e) {
                System.out.println("nao encontrado");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
            Point matchLoc = mmr.maxLoc;

            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));


            Functions.RarityCard rarityCard = new Functions.RarityCard(image, mmr);
            //------------------tab-------------------------------------------------------------------------

            BufferedImage source = ImageIO.read(new File("source.png"));
            CutImage("source.png", "final\\tab" + i + ".png", 0, (int) matchLoc.y - 50, source.getWidth(), 75);


            tab = Imgcodecs.imread("final\\tab" + i + ".png");
            workTarget = Imgcodecs.imread("targets\\work_icon.png");
            energyTarget = Imgcodecs.imread("targets\\work_icon.png");

            Imgproc.matchTemplate(tab, workTarget, workOut, matchMethod);


            Imgproc.rectangle(tab, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(0, 250, 255));

            Core.MinMaxLocResult workMMR = Core.minMaxLoc(workOut);
            Point tabMatchLoc = workMMR.maxLoc;

            Imgproc.rectangle(tab, tabMatchLoc, new Point(tabMatchLoc.x + workTarget.cols(),
                    tabMatchLoc.y + workTarget.rows()), new Scalar(workTarget.cols(), workTarget.rows(), 255));

            Imgcodecs.imwrite("final\\tab" + i + ".png", tab);


            //Exclui itens reepetidos da lista
            Functions.TabCard tabCard = new Functions.TabCard(tab, workMMR);
            Functions.HeroTab newTabToAdd = new Functions.HeroTab(rarity, rarityCard, tabCard);
            List<Boolean> ntem = new ArrayList();
            if (heroLocated.size() > 0) {
                for (Functions.HeroTab heroTab : heroLocated) {


                    if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.y != (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.y) {
                        ntem.add(true);
                    } else {
                        ntem.add(false);
                        System.out.println("Ja tem");
                    }
                }
            }
            if (ntem.contains(false)) {
                newTabToAdd = null;
            } else {
                heroLocated.add(newTabToAdd);
            }
            String[] energyPaths = {"targets\\50energy.png", "targets\\25energy.png", "targets\\75energy.png", "targets\\full_energy.png"};


        }
        for (Functions.HeroTab heroTab : heroLocated) {
            if (isOnScreen(tab, "targets\\25energy.png")) {
                heroTab.tabCard.energy = 25;

            }
            if (isOnScreen(tab, "targets\\50energy.png")) {
                heroTab.tabCard.energy = 50;

            }
            if (isOnScreen(tab, "targets\\70energy.png")) {
                heroTab.tabCard.energy = 75;

            }
            if (isOnScreen(tab, "targets\\full_energy.png")) {
                heroTab.tabCard.energy = 100;

            }


        }
//------------------------------mouse to work button --------------------------------------------------------------------------
        Functions.HeroTab lowerHeroTab = heroLocated.get(0);

        for (Functions.HeroTab heroTab : heroLocated) {

            if (heroTab.rarityCard.minMaxLocResult.maxLoc.y > lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y) {
                Thread.sleep(250);
                lowerHeroTab = heroTab;

                robot.mouseMove((int) heroTab.tabCard.minMaxLocResult.maxLoc.x + workTarget.cols() / 2, (int) heroTab.rarityCard.minMaxLocResult.maxLoc.y - ((int) heroTab.tabCard.minMaxLocResult.maxLoc.y / 2));
                Thread.sleep(200);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(50);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Thread.sleep(500);
            }


        }


        System.out.println("-----------------LIST-------------------");

        for (Functions.HeroTab hero : heroLocated) {
            hero.info();
        }
        Imgcodecs.imwrite("final\\final.png", image);


    }

    public void ClickWorkButtons(String rarity, int energy) throws AWTException, InterruptedException, IOException {

        System.out.println("Lista de herois apagada");
        System.out.println("\n---- Clicking to work ----");
        Robot robot = new Robot();
        Tasks.mouseScrol(robot, 1200, 10);
        for (int i = 0; i < 1; i++) {


            Screenshot("source");

            Mat image = Imgcodecs.imread("source.png");
            //Mat image = Imgcodecs.imread("test.png");
            Mat target = Imgcodecs.imread("targets\\rarity\\" + rarity);
            //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
            System.out.println("target carregado");
            Mat originalImage = image.clone();

            Mat outputRarity = new Mat();

            int machMethod = Imgproc.TM_CCOEFF;


            Mat tab = new Mat();
            Mat workTarget = new Mat();
            Mat energyTarget = new Mat();
            Mat workOut = new Mat();
            Mat energyOut = new Mat();


            int scanTimes = 15;
            for (int z = 0; z < scanTimes; z++) {
                heroLocated.clear();
                try {
                    Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
                } catch (Exception e) {
                    System.out.println("nao encontrado");
                }


                Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
                Point matchLoc = mmr.maxLoc;

                Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                        matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));


                Functions.RarityCard rarityCard = new Functions.RarityCard(image, mmr);
                //------------------tab-------------------------------------------------------------------------

                BufferedImage source = ImageIO.read(new File("source.png"));
                CutImage("source.png", "final\\tab" + i + ".png", 0, (int) matchLoc.y - 50, source.getWidth(), 75);


                tab = Imgcodecs.imread("final\\tab" + i + ".png");
                workTarget = Imgcodecs.imread("targets\\work_icon.png");
                energyTarget = Imgcodecs.imread("targets\\work_icon.png");

                Imgproc.matchTemplate(tab, workTarget, workOut, matchMethod);


                Imgproc.rectangle(tab, matchLoc, new Point(matchLoc.x + target.cols(),
                        matchLoc.y + target.rows()), new Scalar(0, 250, 255));

                Core.MinMaxLocResult workMMR = Core.minMaxLoc(workOut);
                Point tabMatchLoc = workMMR.maxLoc;

                Imgproc.rectangle(tab, tabMatchLoc, new Point(tabMatchLoc.x + workTarget.cols(),
                        tabMatchLoc.y + workTarget.rows()), new Scalar(workTarget.cols(), workTarget.rows(), 255));

                Imgcodecs.imwrite("final\\tab" + i + ".png", tab);


///            adiciona o valor do energy
                for (Functions.HeroTab heroTab : heroLocated) {
                    if (isOnScreen(tab, "targets\\full_energy.png")) {
                        heroTab.tabCard.energy = 100;

                    }
                    if (isOnScreen(tab, "targets\\70energy.png")) {
                        heroTab.tabCard.energy = 75;

                    }
                    if (isOnScreen(tab, "targets\\50energy.png")) {
                        heroTab.tabCard.energy = 50;

                    }
                    if (isOnScreen(tab, "targets\\25energy.png")) {
                        heroTab.tabCard.energy = 25;

                    }


                }
                //Exclui itens reepetidos da lista
                Functions.TabCard tabCard = new Functions.TabCard(tab, workMMR);
                Functions.HeroTab newTabToAdd = new Functions.HeroTab(rarity, rarityCard, tabCard);
                List<Boolean> ntem = new ArrayList();
                if (heroLocated.size() > 0) {
                    for (Functions.HeroTab heroTab : heroLocated) {
                        if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.y != (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.y) {
                            if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.x == (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.x){
                            if (heroTab.tabCard.energy >= energy) {
                                ntem.add(true);
                            } else {
                                ntem.add(false);
                                System.out.println("Ja tem");
                            }
                        }
                    }
                }
                }
                if (ntem.contains(false)) {
                    newTabToAdd = null;
                } else {
                    if (newTabToAdd.tabCard.energy >= energy)
                        if (isOnScreen(newTabToAdd.tabCard.mat,"targets\\work_icon.png")){
                        heroLocated.add(newTabToAdd);
                        }else {
                            newTabToAdd = null;
                        }
                }
                String[] energyPaths = {"targets\\50energy.png", "targets\\25energy.png", "targets\\75energy.png", "targets\\full_energy.png"};



//------------------------------mouse to work button --------------------------------------------------------------------------

            if (heroLocated.size() > 0) {
                Functions.HeroTab lowerHeroTab = heroLocated.get(0);

                for (Functions.HeroTab heroTab : heroLocated) {

                    if (heroTab.rarityCard.minMaxLocResult.maxLoc.y > lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y) {
                        lowerHeroTab = heroTab;
                    }


                }


                if(i < heroLocated.size()) {

                    //FindAndClick(lowerHeroTab.tabCard.mat, "");
                    robot.mouseMove((int) lowerHeroTab.tabCard.minMaxLocResult.maxLoc.x + workTarget.cols() / 2, (int) lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y - ((int) lowerHeroTab.tabCard.minMaxLocResult.maxLoc.y / 2));
                    Thread.sleep(200);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(50);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                    Thread.sleep(500);
                }

                System.out.println("-----------------LIST-------------------");

                for (Functions.HeroTab hero : heroLocated) {
                    hero.info();
                }
                Imgcodecs.imwrite("final\\final.png", image);


            } else {

                System.out.println("nenhum personagem com energia o bastante");
            }
        }
    }

}

    public void ClickRestButtons(String rarity) throws AWTException, InterruptedException, IOException {

        System.out.println("\n---- Clicking to rest ----");

        for (int z = 0;z<1;z++){
        Robot robot = new Robot();


        Screenshot("source");

        Mat image = Imgcodecs.imread("source.png");
        //Mat image = Imgcodecs.imread("test.png");
        Mat target = Imgcodecs.imread("targets\\rarity\\" + rarity);
        //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("target carregado");
        Mat originalImage = image.clone();

        Mat outputRarity = new Mat();

        int machMethod = Imgproc.TM_CCOEFF;


        Mat tab = new Mat();
        Mat tabTarget = new Mat();
        Mat tabOut = new Mat();


        int scanTimes = 8;
        for (int i = 0; i < scanTimes; i++) {
            heroLocated.clear();
            try {
                Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
            } catch (Exception e) {
                System.out.println("nao encontrado");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
            Point matchLoc = mmr.maxLoc;

            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));


            Functions.RarityCard rarityCard = new Functions.RarityCard(image, mmr);
            //------------------tab-------------------------------------------------------------------------

            BufferedImage source = ImageIO.read(new File("source.png"));
            try {
                CutImage("source.png", "final\\tab" + i + ".png", 0, (int) matchLoc.y - 50, source.getWidth(), 75);
            }catch (Error error){
                System.out.println("Erro ao cortar imagem");
                error.printStackTrace();

            }

            tab = Imgcodecs.imread("final\\tab" + i + ".png");
            tabTarget = Imgcodecs.imread("targets\\rest_icon.png");
            tabOut = new Mat();
            Imgproc.matchTemplate(tab, tabTarget, tabOut, matchMethod);
            Imgproc.rectangle(tab, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(0, 250, 255));

            Core.MinMaxLocResult tabMMR = Core.minMaxLoc(tabOut);
            Point tabMatchLoc = tabMMR.maxLoc;

            Imgproc.rectangle(tab, tabMatchLoc, new Point(tabMatchLoc.x + tabTarget.cols(),
                    tabMatchLoc.y + tabTarget.rows()), new Scalar(tabTarget.cols(), tabTarget.rows(), 255));

            Imgcodecs.imwrite("final\\tab" + i + ".png", tab);








            //Exclui itens reepetidos da lista
            Functions.TabCard tabCard = new Functions.TabCard(tab, tabMMR);
            Functions.HeroTab newTabToAdd = new Functions.HeroTab(rarity, rarityCard, tabCard);
            List<Boolean> ntem = new ArrayList();
            if (heroLocated.size() > 0) {
                for (Functions.HeroTab heroTab : heroLocated) {


                    if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.y != (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.y)
                        if ((int)heroTab.rarityCard.minMaxLocResult.maxLoc.x == (int)newTabToAdd.rarityCard.minMaxLocResult.maxLoc.x)
                        {
                        ntem.add(true);
                    } else {
                        ntem.add(false);
                        System.out.println("Ja tem");
                    }
                }
            }
            if (ntem.contains(false)) {
                newTabToAdd = null;
            } else {
                if (isOnScreen(newTabToAdd.tabCard.mat,"targets\\rest_icon.png")){
                    heroLocated.add(newTabToAdd);
                }else {
                    newTabToAdd = null;
                }
            }


            Thread.sleep(1000);

//------------------------------mouse to work button --------------------------------------------------------------------------
        Functions.HeroTab lowerHeroTab = heroLocated.get(0);

        for (Functions.HeroTab heroTab : heroLocated) {

            if (heroTab.rarityCard.minMaxLocResult.maxLoc.y < lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y){
                Thread.sleep(250);
                lowerHeroTab = heroTab;
            }


        }

            for (Functions.HeroTab heroTab : heroLocated) {
                robot.mouseMove((int) lowerHeroTab.tabCard.minMaxLocResult.maxLoc.x + tabTarget.cols() / 2, (int) lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y - ((int) lowerHeroTab.tabCard.minMaxLocResult.maxLoc.y / 2));
                Thread.sleep(200);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(50);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        Thread.sleep(500);
        System.out.println("lowerHeroTab - " + lowerHeroTab.name+"= " +lowerHeroTab.rarityCard.minMaxLocResult.maxLoc.y);
        System.out.println("-----------------LIST-------------------");

        for (Functions.HeroTab hero : heroLocated){
            System.out.println(hero.info());
        }

        Imgcodecs.imwrite("final\\toRest\\final.png", image);

        heroLocated.clear();
        System.out.println("Lista de herois apagada");
    }
    }}

    public void FindAndClick(String targetPath) throws AWTException, InterruptedException, IOException {

        Robot robot = new Robot();




        Mat image = Imgcodecs.imread("source.png");

        Mat target = Imgcodecs.imread(targetPath);
        //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
        //System.out.println("Clicker = "+targetPath+" ---> target carregado");
        Mat originalImage = image.clone();
        if (Config.enableTrashhold == true)
        {
            Imgproc.threshold(target,target,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
        }
        Mat outputRarity = new Mat();

        int machMethod = Imgproc.TM_CCOEFF;
        Point matchLoc = new Point();

        Mat tab = new Mat();
        Mat tabTarget = new Mat();
        Mat tabOut = new Mat();

if (isOnScreen(targetPath)){
        int scanTimes = 1;
        for (int i = 0; i < scanTimes; i++) {
            try {
                Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
            } catch (Exception e) {
                System.out.println("Clicker = "+targetPath+" ---> erro no caminho");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
            matchLoc = mmr.maxLoc;

            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));

        }
//------------------------------mouse to work button --------------------------------------------------------------------------

        robot.mouseMove((int) matchLoc.x + target.cols()/2,(int)matchLoc.y+target.rows()/2);
        Thread.sleep(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(500);

        System.out.println("Clicker = "+targetPath+" ---> thread terminada");
        Imgcodecs.imwrite("final\\final.png", image);
    }
    }
    public void FindAndClick(Mat targetMat) throws AWTException, InterruptedException, IOException {

        Robot robot = new Robot();




        Mat image = Imgcodecs.imread("source.png");

        Mat target = targetMat;
        //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
        //System.out.println("Clicker = "+targetPath+" ---> target carregado");
        Mat originalImage = image.clone();
        if (Config.enableTrashhold == true)
        {
            Imgproc.threshold(target,target,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
        }
        Mat outputRarity = new Mat();

        int machMethod = Imgproc.TM_CCOEFF;
        Point matchLoc = new Point();

        Mat tab = new Mat();
        Mat tabTarget = new Mat();
        Mat tabOut = new Mat();

if (isOnScreen(targetMat)){
        int scanTimes = 1;
        for (int i = 0; i < scanTimes; i++) {
            try {
                Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
            } catch (Exception e) {
                System.out.println("Clicker = "+targetMat.dump()+" ---> erro no caminho");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
            matchLoc = mmr.maxLoc;

            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));

        }
//------------------------------mouse to work button --------------------------------------------------------------------------

        robot.mouseMove((int) matchLoc.x + target.cols()/2,(int)matchLoc.y+target.rows()/2);
        Thread.sleep(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(500);

        System.out.println("Clicker = "+targetMat.dump()+" ---> thread terminada");
        Imgcodecs.imwrite("final\\final.png", image);
    }
    }
    public void FindAndMove(String targetPath,String secondTargetPath) throws AWTException, InterruptedException, IOException {

        Robot robot = new Robot();




        Mat image = Imgcodecs.imread("source.png");

        Mat target = Imgcodecs.imread(targetPath);

        Mat toTarget = Imgcodecs.imread(secondTargetPath);
        if (Config.enableTrashhold == true)
        {
            Imgproc.threshold(target,target,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
            Imgproc.threshold(toTarget,toTarget,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
        }
        Mat output = new Mat();
        Mat toOutput = new Mat();

        int machMethod = Imgproc.TM_CCOEFF;
        Point matchLoc = new Point();
        Point toMatchLoc = new Point();

        Mat tab = new Mat();
        Mat tabTarget = new Mat();
        Mat tabOut = new Mat();

if (isOnScreen(targetPath)){
        int scanTimes =5;
        for (int i = 0; i < scanTimes; i++) {
            try {
                Imgproc.matchTemplate(image, target, output, matchMethod);
                Imgproc.matchTemplate(image, toTarget, toOutput, matchMethod);
            } catch (Exception e) {
                System.out.println("Clicker = "+targetPath+" ---> erro no caminho");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(output);
            Core.MinMaxLocResult toMMR = Core.minMaxLoc(toOutput);
            matchLoc = mmr.maxLoc;
            toMatchLoc = toMMR.maxLoc;



            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));

            Imgproc.rectangle(image, toMatchLoc, new Point(toMatchLoc.x + toTarget.cols(),
                    toMatchLoc.y + toTarget.rows()), new Scalar(toTarget.cols(), toTarget.rows(), 140));


        }
//------------------------------mouse to work button --------------------------------------------------------------------------
    Imgcodecs.imwrite("final\\final.png", image);
        robot.mouseMove((int) matchLoc.x + target.cols()/2,(int)matchLoc.y+target.rows()/2);
        Thread.sleep(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(50);
        robot.mouseMove((int) toMatchLoc.x ,(int)toMatchLoc.y+toTarget.rows()/2);

        Thread.sleep(500);

        System.out.println("Clicker = "+targetPath+" ---> thread terminada");

    }
    }

    public boolean isOnScreen(String path) throws IOException, InterruptedException, AWTException {

        List<Point> attemptsList = new ArrayList<>();



        Mat image = Imgcodecs.imread("source.png");
        //Mat image = Imgcodecs.imread("test.png");
        Mat target = Imgcodecs.imread(path);
        //Mat targetGray = Imgcodecs.imread("targets\\connect_button.png",Imgcodecs.IMREAD_GRAYSCALE);
        //System.out.println("FINDER "+path+" ---> target carregado");
        Mat originalImage = image.clone();

        if (Config.enableTrashhold == true) {
           // Imgproc.threshold(image, image, Config.minTrashhold,Config.maxTrashhold, Config.trashholdType);
            Imgproc.threshold(target, target, Config.minTrashhold,Config.maxTrashhold, Config.trashholdType);
        }
        Mat outputRarity = new Mat();


        Point matchLoc = new Point();


//3 EM i
        int secondMethod = 3;   //methods  0 e 1  fails
        for (int i =0  ; i < 2; i++) {

            try {
                if (i == 0)
                {
                Imgproc.matchTemplate(image, target, outputRarity, 3); // naom mudar
                }
                if (i == 1)
                {
                    Imgproc.matchTemplate(image, target, outputRarity, 4); //nao mudar
                }

            } catch (Exception e) {
                System.out.println("nao encontrado");
            }

            //Imgproc.matchTemplate(image, target, outputRarity, i);

            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);

            matchLoc = mmr.maxLoc;
            attemptsList.add(matchLoc);



            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(),target.depth()));
            Imgcodecs.imwrite("final\\finalFinder.png", image);
            Imgcodecs.imwrite("final\\finder"+target.cols()+".png",target );

        }


        if (Math.abs(attemptsList.get(0).x - attemptsList.get(1).x) <= finderTolerance){

            if (Math.abs(attemptsList.get(0).y - attemptsList.get(1).y) <= finderTolerance){
                System.out.println("FINDER = "+path+" Dif = "+
                        (Math.abs(attemptsList.get(0).x - attemptsList.get(1).x)+" / "+
                                (Math.abs(attemptsList.get(0).y - attemptsList.get(1).y)) ));
                return true;

            }

        }
        //System.out.println("FINDER = "+path+" ---> false *" +"Dif = "+
          //      (Math.abs(attemptsList.get(0).x - attemptsList.get(1).x)+" / "+
            //            (Math.abs(attemptsList.get(0).y - attemptsList.get(1).y)) ));
        return false;
    }
    public boolean isOnScreen(Mat source,String targetPath) throws IOException, InterruptedException, AWTException {

        List<Point> attemptsList = new ArrayList<>();



        Mat image = source;
        //Mat image = Imgcodecs.imread("test.png");
        Mat target = Imgcodecs.imread(targetPath);
        //Mat targetGray = Imgcodecs.imread("targets\\connect_button.png",Imgcodecs.IMREAD_GRAYSCALE);
        //System.out.println("FINDER "+path+" ---> target carregado");
        Mat originalImage = image.clone();
        if (Config.enableTrashhold == true)
        {
        Imgproc.threshold(target,target,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
        }
        Mat output = new Mat();


        Point matchLoc = new Point();



        int scanTimes =2;   //methods  0 e 1  fails
        for (int i =3 ; i < 3+scanTimes; i++) {
            try {
                Imgproc.matchTemplate(image, target, output, i);
            } catch (Exception e) {
                System.out.println("nao encontrado");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(output);

            matchLoc = mmr.maxLoc;
            attemptsList.add(matchLoc);


            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));



        }
        Imgcodecs.imwrite("final\\finder\\"+target.rows() +".png",target);
        if (attemptsList.get(0).x == attemptsList.get(1).x){

            if (attemptsList.get(0).y == attemptsList.get(1).y){
                System.out.println("FINDER = "+targetPath+" ---> target esta na tela");
                Imgcodecs.imwrite("final\\finder\\final"+".png", image);
                return true;

            }

        }
        // System.out.println("FINDER = "+path+" ---> target nao esta na tela");
        return false;
    }
    public boolean isOnScreen(Mat targetMat) throws IOException, InterruptedException, AWTException {

        List<Point> attemptsList = new ArrayList<>();



        Mat image = Imgcodecs.imread("source.png");;
        //Mat image = Imgcodecs.imread("test.png");
        Mat target = targetMat;
        //Mat targetGray = Imgcodecs.imread("targets\\connect_button.png",Imgcodecs.IMREAD_GRAYSCALE);
        //System.out.println("FINDER "+path+" ---> target carregado");
        Mat originalImage = image.clone();
        if (Config.enableTrashhold == true)
        {
        Imgproc.threshold(target,target,Config.minTrashhold,Config.maxTrashhold,Config.trashholdType);
        }
        Mat output = new Mat();


        Point matchLoc = new Point();



        int scanTimes =2;   //methods  0 e 1  fails
        for (int i =3 ; i < 3+scanTimes; i++) {
            try {
                Imgproc.matchTemplate(image, target, output, i);
            } catch (Exception e) {
                System.out.println("nao encontrado");
            }


            Core.MinMaxLocResult mmr = Core.minMaxLoc(output);

            matchLoc = mmr.maxLoc;
            attemptsList.add(matchLoc);


            Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                    matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));



        }
        Imgcodecs.imwrite("final\\finder\\"+target.rows() +".png",target);
        if (attemptsList.get(0).x == attemptsList.get(1).x){

            if (attemptsList.get(0).y == attemptsList.get(1).y){
                System.out.println("FINDER = "+targetMat.dump()+" ---> target esta na tela");
                Imgcodecs.imwrite("final\\finder\\final"+".png", image);
                return true;

            }

        }
        // System.out.println("FINDER = "+path+" ---> target nao esta na tela");
        return false;
    }



    public boolean isHeroTiredOut(String rarity) throws IOException, InterruptedException, AWTException {

        if (isOnScreen("targets\\character_tab.png")) {
            heroLocated.clear();
            System.out.println("\n---- are tiredout ----");
            System.out.println("Lista de herois apagada");
            Robot robot = new Robot();
            //Tasks.mouseScrol(robot,1200,10);
            Screenshot("source");

            Mat image = Imgcodecs.imread("source.png");
            //Mat image = Imgcodecs.imread("test.png");
            Mat target = Imgcodecs.imread("targets\\rarity\\" + rarity);
            //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
            System.out.println("target carregado");
            Mat originalImage = image.clone();
            if (Config.enableTrashhold == true) {
                Imgproc.threshold(target, target, Config.minTrashhold, Config.maxTrashhold, Config.trashholdType);
            }
            Mat outputRarity = new Mat();

            int machMethod = Imgproc.TM_CCOEFF;


            Mat tab = new Mat();
            Mat workTarget = new Mat();
            Mat energyTarget = new Mat();
            Mat workOut = new Mat();
            Mat energyOut = new Mat();


            int scanTimes = 10;
            for (int i = 0; i < scanTimes; i++) {
                try {
                    Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
                } catch (Exception e) {
                    System.out.println("nao encontrado");
                }


                Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
                Point matchLoc = mmr.maxLoc;

                Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                        matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));


                Functions.RarityCard rarityCard = new Functions.RarityCard(image, mmr);
                //------------------tab-------------------------------------------------------------------------

                BufferedImage source = ImageIO.read(new File("source.png"));
                CutImage("source.png", "final\\tab" + i + ".png", 0, (int) matchLoc.y - 50, source.getWidth(), 75);


                tab = Imgcodecs.imread("final\\tab" + i + ".png");
                workTarget = Imgcodecs.imread("targets\\work_icon.png");
                //energyTarget = Imgcodecs.imread("targets\\work_icon.png");

                Imgproc.matchTemplate(tab, workTarget, workOut, matchMethod);


                Core.MinMaxLocResult workMMR = Core.minMaxLoc(workOut);


                Imgcodecs.imwrite("final\\tab" + i + ".png", tab);


                //Exclui itens reepetidos da lista
                Functions.TabCard tabCard = new Functions.TabCard(tab, workMMR);
                Functions.HeroTab newTabToAdd = new Functions.HeroTab(rarity, rarityCard, tabCard);
                List<Boolean> ntem = new ArrayList();
                if (heroLocated.size() > 0) {
                    for (Functions.HeroTab heroTab : heroLocated) {


                        if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.y != (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.y) {
                            ntem.add(true);
                        } else {
                            ntem.add(false);
                            //System.out.println("Ja tem");
                        }
                    }
                }
                if (ntem.contains(false)) {
                    newTabToAdd = null;
                } else {
                    heroLocated.add(newTabToAdd);
                }
                String[] energyPaths = {"targets\\50energy.png", "targets\\25energy.png", "targets\\75energy.png", "targets\\full_energy.png"};

            }
            List<Boolean> trues = new ArrayList<>();
            for (Functions.HeroTab heroTab : heroLocated) {
                if (isOnScreen(tab, "targets\\full_energy.png")) {
                    heroTab.tabCard.energy = 100;
                    trues.add(false);
                    heroTab.info();

                }
                if (isOnScreen(tab, "targets\\70energy.png")) {
                    heroTab.tabCard.energy = 75;
                    trues.add(false);
                    heroTab.info();
                }

                if (isOnScreen(tab, "targets\\50energy.png")) {
                    heroTab.tabCard.energy = 50;
                    trues.add(false);
                    heroTab.info();
                }
                if (isOnScreen(tab, "targets\\25energy.png")) {
                    heroTab.tabCard.energy = 25;
                    trues.add(false);
                    heroTab.info();
                }

                if (isOnScreen(tab, "targets\\0energy.png")) {
                    heroTab.tabCard.energy = 0;
                    trues.add(true);
                    heroTab.info();
                }
                Imgcodecs.imwrite("final\\finder\\tab" + tab.dataAddr() + ".png", tab);
            }
            if (!trues.contains(false)) {
                Imgcodecs.imwrite("final\\final" + ".png", image);
                System.out.println("estao cansados");
                return true;
            }
            System.out.println("----------------------------------");
            for (Boolean unit : trues) {

                System.out.println(unit);
            }


            Imgcodecs.imwrite("final\\final" + ".png", image);
        }
        return false;
    }

    public boolean heroIsPowered(String rarity,int energy) throws IOException, InterruptedException, AWTException {

        if (isOnScreen("targets\\character_tab.png")) {
            heroLocated.clear();
            System.out.println("\n---- are tiredout ----");
            System.out.println("Lista de herois apagada");
            Robot robot = new Robot();
            //Tasks.mouseScrol(robot,1200,10);
            Screenshot("source");

            Mat image = Imgcodecs.imread("source.png");
            //Mat image = Imgcodecs.imread("test.png");
            Mat target = Imgcodecs.imread("targets\\rarity\\" + rarity);
            //Mat targetGray = Imgcodecs.imread(path[0],Imgcodecs.IMREAD_GRAYSCALE);
            System.out.println("target carregado");
            Mat originalImage = image.clone();

                Imgproc.threshold(target, target, Config.minTrashhold, Config.maxTrashhold, Config.trashholdType);

            Mat outputRarity = new Mat();

            int machMethod = Imgproc.TM_CCOEFF;


            Mat tab = new Mat();
            Mat workTarget = new Mat();
            Mat energyTarget = new Mat();
            Mat workOut = new Mat();
            Mat energyOut = new Mat();


            int scanTimes = 10;
            for (int i = 0; i < scanTimes; i++) {
                try {
                    Imgproc.matchTemplate(image, target, outputRarity, matchMethod);
                } catch (Exception e) {
                    System.out.println("nao encontrado");
                }


                Core.MinMaxLocResult mmr = Core.minMaxLoc(outputRarity);
                Point matchLoc = mmr.maxLoc;

                Imgproc.rectangle(image, matchLoc, new Point(matchLoc.x + target.cols(),
                        matchLoc.y + target.rows()), new Scalar(target.cols(), target.rows(), 140));


                Functions.RarityCard rarityCard = new Functions.RarityCard(image, mmr);
                //------------------tab-------------------------------------------------------------------------

                BufferedImage source = ImageIO.read(new File("source.png"));
                try {


                    CutImage("source.png", "final\\tab" + i + ".png", 0, (int) matchLoc.y - 50, source.getWidth(), 75);
                }catch (Error error){

                    error.printStackTrace();
                    return false;
                }

                tab = Imgcodecs.imread("final\\tab" + i + ".png");
                workTarget = Imgcodecs.imread("targets\\work_icon.png");
                //energyTarget = Imgcodecs.imread("targets\\work_icon.png");

                Imgproc.matchTemplate(tab, workTarget, workOut, matchMethod);


                Core.MinMaxLocResult workMMR = Core.minMaxLoc(workOut);


                Imgcodecs.imwrite("final\\tab" + i + ".png", tab);


                //Exclui itens reepetidos da lista
                Functions.TabCard tabCard = new Functions.TabCard(tab, workMMR);
                Functions.HeroTab newTabToAdd = new Functions.HeroTab(rarity, rarityCard, tabCard);
                List<Boolean> ntem = new ArrayList();
                if (heroLocated.size() > 0) {
                    for (Functions.HeroTab heroTab : heroLocated) {


                        if ((int) heroTab.rarityCard.minMaxLocResult.maxLoc.y != (int) newTabToAdd.rarityCard.minMaxLocResult.maxLoc.y) {
                            ntem.add(true);
                        } else {
                            ntem.add(false);
                            //System.out.println("Ja tem");
                        }
                    }
                }
                if (ntem.contains(false)) {
                    newTabToAdd = null;
                } else {
                    heroLocated.add(newTabToAdd);
                }
                String[] energyPaths = {"targets\\50energy.png", "targets\\25energy.png", "targets\\75energy.png", "targets\\full_energy.png"};

            }


            List<Boolean> trues = new ArrayList<>();
            for (Functions.HeroTab heroTab : heroLocated) {
                if (isOnScreen(tab, "targets\\full_energy.png")) {
                    heroTab.tabCard.energy = 100;



                }
                if (isOnScreen(tab, "targets\\70energy.png")) {
                    heroTab.tabCard.energy = 75;


                }

                if (isOnScreen(tab, "targets\\50energy.png")) {
                    heroTab.tabCard.energy = 50;


                }
                if (isOnScreen(tab, "targets\\25energy.png")) {
                    heroTab.tabCard.energy = 25;


                }

                if (isOnScreen(tab, "targets\\0energy.png")) {
                    heroTab.tabCard.energy = 0;


                }

                heroTab.info();
                if (heroTab.tabCard.energy >= energy){
                    Imgcodecs.imwrite("final\\final" + ".png", image);
                    System.out.println("alguem tem pelo menos " + energy+"% de energia" );
                    System.out.println("----------------------------------");
                    return true;

                }
                Imgcodecs.imwrite("final\\finder\\tab" + tab.dataAddr() + ".png", tab);
            }




            Imgcodecs.imwrite("final\\final" + ".png", image);
        }
        return false;
    }

    public void verification() throws IOException, InterruptedException, AWTException {

        if (isOnScreen("target\\lock1.png")){




        }
    }

    String teoricalStage = "goingToWork";//goingToRest , goingToWork, inTreasureHunt, inRestTime,toTreasureHunt,energyVerification
    int errorCount = 0;
    int loopsInTreasure = 0;
    int loopsInLogin = 0;
    int energyTarget = 0;

    static int timeX = 0;

    public  void timer(int x) throws InterruptedException {
    }


    public void Looper() throws IOException, InterruptedException, AWTException {
        if (errorCount > 10){

            new Robot().keyPress(KeyEvent.VK_F5);
            errorCount = 0;
            new Robot().keyRelease(KeyEvent.VK_F5);
        } ///error reload
        if (loopsInLogin > 20){

            new Robot().keyPress(KeyEvent.VK_F5);
            loopsInLogin = 0;
            new Robot().keyRelease(KeyEvent.VK_F5);
        } ///error reload

        //----------------------------------------------------------------------------

        if (teoricalStage == null)//first open
        {
        System.out.println("4 segundos pra tu se ajeitar parceiro B)");
        Thread.sleep(5000);
        }
        Screenshot("source");
        if (isOnScreen("targets\\bomb_logo.png")){

            loopsInLogin++;

            //teoricalStage = "login";

            FindAndClick("targets\\connect_button.png");
            Thread.sleep(Config.metamaskDelay*1000);
            Screenshot("source");
            if (isOnScreen("targets\\signin_button.png")){

                FindAndClick("targets\\signin_button.png");

            }
            else {
                Screenshot("source");
                if (isOnScreen("targets\\metamask_icon.png")){

                    FindAndClick("targets\\metamask_icon.png");
                    Thread.sleep(3000);
                    Screenshot("source");
                    Thread.sleep(3000);
                    if (isOnScreen("targets\\signin_button.png")){

                        FindAndClick("targets\\signin_button.png");

                    }
                }

            }
        } //login

        if (isOnScreen("targets\\home.png")){

            if (teoricalStage == null){
                teoricalStage = "goingToRest";
            }

            if (teoricalStage == "goingToWork"||teoricalStage == "goingToRest"
                    ||teoricalStage == "inRestTime"||teoricalStage == "energyVerification")
            {
                FindAndClick("targets\\enter_character.png");
            }

            if (teoricalStage == "toTreasureHunt"||teoricalStage =="inTreasureHunt"){
                FindAndClick("targets\\treasure_hunt.png");
            }

        } // home

        if (isOnScreen("targets\\character_tab.png")){

            System.out.println(">>>  Characters Tab  <<<");
            if (teoricalStage == "goingToWork"){

                if (!Config.rarityTarget.equalsIgnoreCase("all")) {
                    ClickWorkButtons("common_icon.png", 0);
                    teoricalStage = "toTreasureHunt";
                }
                if (Config.rarityTarget.equalsIgnoreCase("all")) {
                    FindAndClick("targets\\all.png");
                    teoricalStage = "toTreasureHunt";
                }
                FindAndClick("targets\\exit_character.png");
            }
            if (teoricalStage == "goingToRest"){
                try {


                ClickRestButtons("common_icon.png");
                teoricalStage = "inRestTime";}catch (Error error){}

                //FindAndClick("targets\\exit_character.png");
            }
            if (teoricalStage == "inRestTime"){
                //ClickRestButtons("common_icon.png");
                //teoricalStage = "inRestTime";
                 System.out.println("Descansando");
                 if (heroIsPowered("common_icon.png",energyTarget)){
                 teoricalStage = "goingToWork";}
                //FindAndClick("targets\\exit_character.png");
            }
            if (teoricalStage == "toTreasureHunt"||teoricalStage =="inTreasureHunt"){

               FindAndClick("targets\\exit_character.png");
            }

            if (teoricalStage == "energyVerification"){

                System.out.println("Descansando");
                if (heroIsPowered("common_icon.png",energyTarget)){
                    teoricalStage = "goingToWork";
                }else {
                    teoricalStage = "goingToRest";
                }
            }

        } // character screen
        if (isOnScreen("targets\\map_complete.png")){
            FindAndClick("targets\\newmap.png");


        }
        if (isOnScreen("targets\\exit_treasure_hunt.png")){

            if (isOnScreen("targets\\map_complete.png")){
                FindAndClick("targets\\newmap.png");


            }
            if (teoricalStage == "goingToRest"||teoricalStage == "inRestTime"||teoricalStage == "goingToWork"){
                FindAndClick("targets\\exit_treasure_hunt.png");
            }
            if (teoricalStage == "toTreasureHunt"){

               teoricalStage = "inTreasureHunt";


            }
            if (teoricalStage == "inTreasureHunt"){
                loopsInTreasure++;

                System.out.println("loops in treasure: "+loopsInTreasure);


                if (loopsInTreasure >= Config.loopsInTreasure){

                    teoricalStage = "energyVerification";
                    loopsInTreasure = 0;

                }
            }
            if (teoricalStage == "energyVerification"){

                FindAndClick("targets\\exit_treasure_hunt.png");
            }



        } // inTreasure





//--------------------erros-------------------------------------------------

        if (isOnScreen("targets\\unknown_error.png")){
            FindAndClick("targets\\ok_button.png");
            errorCount ++;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("\n"+dtf.format(LocalDateTime.now()) + "  -> teorical stage == "+teoricalStage+"\n");
        cycleNumber++;
        //Tasks.mouseScrol(new Robot(),-1000,10);
        Looper();

    }
    int cycleNumber = 1;
}




