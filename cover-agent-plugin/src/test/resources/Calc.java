package ai.qodo.test;
public class Calc {
    public static void main(String[] args) {
        int one = Integer.parseInt(args[0]);
        int two = Integer.parseInt(args[1]);
        sub(one, two);
    }

    public static int sub(int num1, int num2) {
        return num1 - num2;
    }

}