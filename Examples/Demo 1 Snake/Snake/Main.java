import JProcessing.Processing;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "Snake.pde"
                                        //Author:  Kevin Liu (2016.12.2)
                                        //Note: Some Lines has been Modified in order for it to Work with JProcessing
    int ShapeSize;                                              //Block Size
    int MapSize=32;                                             //Map Size
    long FrameCount=0;                                          //Rendered Frames

    int[] SnakeX;                                               //Snake X Coordinate
    int[] SnakeY;                                               //Snake Y Coordinate
    int Length=5;                                               //Snake Length
    int Direction=1;                                            //Direction (1 As Up, 2 As Down, 3 As Left, 4 As right)
    int OX, OY;                                                 //Circle Location

    protected void setup() {
        fullScreen();                                           //Setup Graphics
        frameRate(20);
        ellipseMode(CORNER);
                                        //Modification: form <=int(height/MapSize)> to <=height/MapSize>
        ShapeSize=height/MapSize;                               //Initialize Variables
        SnakeX=new int[1024];
        SnakeY=new int[1024];
        for (int CUse=0; CUse<5; CUse++) {                      //Set Snake Start Position
            SnakeX[CUse]=15;
            SnakeY[CUse]=26+CUse;
        }
        NewO();
    }

    protected void draw() {                                     //Start Rendering
        background(127, 127, 127);
        fill(0, 0, 0);
        noStroke();
        translate((width-height)/2f, (height-MapSize*ShapeSize)/2f);

        for (int GUseY=0; GUseY<MapSize; GUseY++) {             //Draw Map
            for (int GUseX=0; GUseX<MapSize; GUseX++) {
                rect(GUseX*ShapeSize, GUseY*ShapeSize, ShapeSize, ShapeSize);
            }
        }                                                       //Draw Circle
        fill(255, 255, 255);
        ellipse(OX*ShapeSize, OY*ShapeSize, ShapeSize, ShapeSize);

        rect (SnakeX[0]*ShapeSize, SnakeY[0]*ShapeSize, ShapeSize, ShapeSize);
        for (int GUse=Length-1; GUse>0; GUse--) {               //Draw Snake
            rect (SnakeX[GUse]*ShapeSize+2, SnakeY[GUse]*ShapeSize+2, ShapeSize-4, ShapeSize-4);
        }

        if (FrameCount%2==0) Move();                            //Move Snake Every 2 frames
        FrameCount++;
    }

    protected void keyPressed() {
        switch (keyCode) {
            case UP:
                if (SnakeY[1]!=SnakeY[0]-1) Direction=1;        //Turn Up
                break;
            case DOWN:
                if (SnakeY[1]!=SnakeY[0]+1) Direction=2;        //Turn Down
                break;
            case LEFT:
                if (SnakeX[1]!=SnakeX[0]-1) Direction=3;        //Turn Left
                break;
            case RIGHT:
                if (SnakeX[1]!=SnakeX[0]+1) Direction=4;        //Turn Right
                break;
        }
    }

    private void Move() {                                       //Move Snake
        if (SnakeX[0]==OX&&SnakeY[0]==OY) {                     //Snake Reaches Circle
            Length++;
            if (Length==1023)exit();
            NewO();                                             //Generate New Circle
        }

        for (int GUse=Length-1; GUse>0; GUse--) {               //Move Snake Body
            SnakeX[GUse]=SnakeX[GUse-1];
            SnakeY[GUse]=SnakeY[GUse-1];
        }

        switch(Direction) {                                     //Move Snake Head
            case 1:
                if (SnakeY[0]>0) SnakeY[0]--;
                break;
            case 2:
                if (SnakeY[0]<MapSize-1) SnakeY[0]++;
                break;
            case 3:
                if (SnakeX[0]>0) SnakeX[0]--;
                break;
            case 4:
                if (SnakeX[0]<MapSize-1) SnakeX[0]++;
                break;
        }

        for (int CUse=1; CUse<Length; CUse++) {                 //Death Detection
            if (SnakeX[0]==SnakeX[CUse]&&SnakeY[0]==SnakeY[CUse]) {
                delay(2500);
                exit();
            }
        }
    }

    private void NewO() {                                       //Generate New Circle
        boolean OUse=true;                                      //Circle Position Availability (True As Invalid, False As Valid)
        int OUseX=0;
        int OUseY=0;

        while (OUse) {                                          //Generate New Circle Position
            OUseX=floor(random(0, 31));
            OUseY=floor(random(0, 31));
            OUse=false;                 //Modification: form <=int(random(...))> to <=floor(random(...))>

            for (int CUse=0; CUse<Length-1; CUse++) {            //Check Availability
                if (SnakeX[CUse]==OUseX&&SnakeY[CUse]==OUseY)OUse=true;
            }
        }

        OX=OUseX;
        OY=OUseY;
    }
}