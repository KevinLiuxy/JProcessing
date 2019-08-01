import JProcessing.Processing;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
    //================================================================================================================================
                                        //Example: "RegularPolygon.pde"
    public void setup() {               //Source:  https://processing.org/examples/regularpolygon.html
        size(960, 540);
    }

    public void draw() {
        background(102);

        pushMatrix();
        translate(width*0.2, height*0.5);
        rotate(frameCount / 200.0);
        polygon(0, 0, 123, 3);  // Triangle
        popMatrix();

        pushMatrix();
        translate(width*0.5, height*0.5);
        rotate(frameCount / 50.0);
        polygon(0, 0, 120, 20); // Icosahedron
        popMatrix();

        pushMatrix();
        translate(width*0.8, height*0.5);
        rotate(frameCount / -100.0);
        polygon(0, 0, 105, 7);  // Heptagon
        popMatrix();
    }

    private void polygon(float x, float y, float radius, int npoints) {
        float angle = TWO_PI / npoints;
        beginShape();
        for (float a = 0; a < TWO_PI; a += angle) {
            float sx = x + cos(a) * radius;
            float sy = y + sin(a) * radius;
            vertex(sx, sy);
        }
        endShape(CLOSE);
    }
}