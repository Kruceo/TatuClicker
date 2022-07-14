import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {


    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Socket socket;
    Boolean permitedAccess = false;

    private static String address = "192.168.0.110";
    private static Integer port = 9669;
    private String token = "maezinha";

    public void Start() throws IOException {


        try {

            //socket = new Socket("127.0.0.1", 9669);
            socket = new Socket(address, port);
            //socket= new Socket("google.com",80);
            System.out.println("cliente conectado: " + socket.getInetAddress());
        } catch (IOException error) {

            System.out.println("server esta fechado") ;
            new Client().run();
        }

        //socket.
    }

    public void readToken() throws IOException {

        //File tokenTxt = new File("my.token");
        Scanner keyboard = new Scanner(System.in);
        String keyboardOut = null;
        while (keyboard.hasNext()) {
            keyboardOut = keyboard.nextLine();
            if (keyboardOut.startsWith("reset.token")) {
                System.out.println("reset your token to");
                token = keyboardOut.split(" ")[1];
                System.out.println("token reseted to "+token);
                //keyboard.remove();
                try {
                    keyboard.close();}catch (Error error){}
            }
            }

       // if ()

            //if (!configTxt.exists()) {
               // System.out.println("tatu.config nao existe, config padrao aplicada...");
               // return;
           // }
        //socket.
    }

    public void SendMsg(String msg) throws IOException {
        //readToken();
        PrintStream out;
        try {

            out = new PrintStream(socket.getOutputStream());
            out.println(msg);
            System.out.println("token enviado: " + msg);
            out.flush();
            //out.close();
        } catch (IOException error) {

            System.out.println("erro ao enviar msg");
            error.printStackTrace();
        }


    }

    public void ReceiveBool() throws IOException, InterruptedException {
        Scanner in = new Scanner(socket.getInputStream());
        while (in.hasNextLine()) {

            try {

                permitedAccess = Boolean.parseBoolean(in.nextLine());
                Main.access = permitedAccess;
                System.out.println("Permited Access: " + permitedAccess);


            } catch (Error e) {
                System.out.println("client error : " + e.getMessage());
            }
            if (!permitedAccess){

                Main.access = permitedAccess;
                System.out.println("token nao existente ou ja ativo...");
                Thread.sleep(3000);
                this.run();
                //System.exit(0);
            }
            else {
                if (Main.loop != null)
                {
                    Main.loop.waitClient = false;
                }
            }


        }
        while (!in.hasNextLine()) {
            System.out.println("Server fechado");
            if (Main.loop!=null){
            Main.loop.waitClient = true;
            }
            new Client().run();
           // System.exit(0);
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    @Override
    public void run() {
        Client client = new Client();
        Thread thread = new Thread(client, "janela");
        //WindowMaker windowMaker = new WindowMaker();
        //windowMaker.make();

        //windowMaker.send.addActionListener(new ActionListener() {
            //@Override
           // public void actionPerformed(ActionEvent e) {
                //if (windowMaker.tokenEnter.getText().length() > 0){
                try {
                    client.Start();
                } catch (IOException t) {
                    t.printStackTrace();
                }

                //client.SendMsg(windowMaker.tokenEnter.getText());
                //client.SendMsg(windowMaker.tokenEnter.getText());

                //client.SendMsg("000x12554");
        try {
            client.SendMsg(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
                    client.ReceiveBool();
                } catch (IOException | InterruptedException u) {
                    u.printStackTrace();
                }

                if (permitedAccess != true) {

                } else {

                    //windowMaker.window.setVisible(false);
                }

            }
       // });
    //}
}

