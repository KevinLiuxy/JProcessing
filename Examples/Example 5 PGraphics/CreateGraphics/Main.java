import JProcessing.Processing;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "CreateGraphics.pde"
    PGraphics pg;                       //Source:  https://processing.org/examples/creategraphics.html

    public void setup() {
        size(960, 540);
        pg = createGraphics(600, 300);
    }

    public void draw() {
        fill(0, 12);
        rect(0, 0, width, height);
        fill(255);
        noStroke();
        ellipse(mouseX, mouseY, 90, 90);

        pg.beginDraw();
        pg.background(51);
        pg.noFill();
        pg.stroke(255);
        pg.ellipse(mouseX-180, mouseY-120, 90, 90);
        pg.endDraw();

        // Draw the offscreen buffer to the screen with image()
        image(pg, 180, 120);
    }
}