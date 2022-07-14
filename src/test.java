import java.util.ArrayList;
import java.util.List;

public class test {


    static List<Integer> ints =new ArrayList<Integer>();
    static int i = 0;
    static int ix = 0;
    public static void main(String[] args) {
        while (true){
            ix = coisa();
        }
    }

    public static Integer coisa(){

        outraCOisa();
        return 1;
    }
    public static float outraCOisa(){

        maisOutraCOisa();
        return 12;
    }
    public static float maisOutraCOisa(){
        i++;
        System.out.println(i);
        return 123;
    }
}
