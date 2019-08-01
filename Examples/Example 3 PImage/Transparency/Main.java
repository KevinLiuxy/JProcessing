import JProcessing.Processing;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "Transparency.pde"
    PImage img;                         //Source:  https://processing.org/examples/transparency.html
    float offset = 0;                   //Note: Some Lines has been Modified in order for it to Work with JProcessing
    float easing = 0.05f;               //Modification: form <=0.05> to <=0.05f>

    public void setup() {
        size(960, 540);
        img = loadImage("AzurLane_Ayanami.jpg");
        img = img.resize(960, 540);
    }                                   //Modification: form <img.resize(...)> to <img = img.resize(...)>

    public void draw() {
        image(img, 0, 0); // Display at full opacity
        float dx = (mouseX-img.width/2f) - offset;
        offset += dx * easing;
        tint(255, 127);   // Display at half opacity
        image(img, offset, 0);
    }
}