import JProcessing.Processing;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "TriangleStrip.pde"
    int x;                              //Source:  https://processing.org/examples/trianglestrip.html
    int y;                              //Note: Some Lines has been Modified in order for it to Work with JProcessing
    float outsideRadius = 225;
    float insideRadius = 150;

    public void setup() {
        size(960, 540);
        background(204);
        x = width/2;
        y = height/2;
    }

    public void draw() {
        background(204);
                                        //Modification: form <=int(...)> to <=(int)(...)>
        int numPoints = (int)map(mouseX, 0, width, 6, 60);
        float angle = 0;                //Modification: from <180.0/numPoints> to <180f/numPoints>
        float angleStep = 180f/numPoints;

        beginShape(TRIANGLE_STRIP);
        for (int i = 0; i <= numPoints; i++) {
            float px = x + cos(radians(angle)) * outsideRadius;
            float py = y + sin(radians(angle)) * outsideRadius;
            angle += angleStep;
            vertex(px, py);
            px = x + cos(radians(angle)) * insideRadius;
            py = y + sin(radians(angle)) * insideRadius;
            vertex(px, py);
            angle += angleStep;
        }
        endShape();
    }
}