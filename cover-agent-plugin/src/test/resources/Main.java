package ai.qodo.test;
public class Main {
    public static void main(String[] args) {
        int one = Integer.parseInt(args[0]);
        int two = Integer.parseInt(args[1]);
        add(one, two);
    }

    public static int add(int num1, int num2) {
        return num1 + num2;
    }

}