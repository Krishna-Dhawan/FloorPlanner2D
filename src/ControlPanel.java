import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ControlPanel extends Component {
    JPanel cp;

    public ControlPanel(){
        cp = new JPanel();
        Button b = new Button("Click Me");
        cp.add(b);
        Label l = new Label("This is a control panel");
        cp.add(l);
    }
}
