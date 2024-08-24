import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Abc obj = new Abc();
        obj.setVisible(true);
    }
}
class Abc extends JFrame {
    public Abc() {
        setTitle("Abc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
    }
}