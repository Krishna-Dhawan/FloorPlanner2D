package comps;

import java.io.Serializable;

public class Dim implements Serializable {
    public int width;
    public int height;
    public Dim(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
