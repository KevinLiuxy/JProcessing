import ddf.minim.*;                     //Using Minim 2.2.2 Audio Library by Damien Quartz
import JProcessing.Processing;          //     (Website: https://github.com/ddf/Minim)

public class Main extends Processing {  //More Processing Examples at: https://processing.org/examples
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "MineSweeper_2018.pde"
    byte SizeX=30, SizeY=30;            //Author:  Kevin Liu (2018.5.20)
    float BlockX, BlockY;               //Note: Some Lines has been Modified in order for it to Work with JProcessing
    byte[][] Map;
    byte[][] State;
    color C[]={ color(0,95,191), color(95,191,0), color(191,95,0), color(191,0,95),
                color(255,0,0), color(0,191,95), color(95,0,191), color(0,0,0) };
    PImage Background;
    Minim Player;
    AudioPlayer Music;
    AudioPlayer Effect[]=new AudioPlayer[5];

    byte Level=3;
    short Mine=150;
    short Found;
    boolean Game=false;
    byte Mouse=0;
    long StartTime;

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void setup() {
        size(722, 772);
        noStroke();                                             //Setup Graphics Parameters
        noLoop();
        frameRate(15);
        textFont(createFont("Calibri", 32));
                                                                //Load Image and Sound Files
        Background=loadImage("Data/IMG.jpg");
        Player=new Minim(this);
        Music=Player.loadFile("Data/music.mp3");
        for (int X=0; X<5; X++) {
            Effect[X]=Player.loadFile("Data/a"+X+".mp3");
        }
        Music.setGain(-15);                                     //Setup Music Player
        Music.setLoopPoints(51, 90503);
        Music.loop();
        ellipseMode(CORNER);
        NewGame();                                              //Start new Game
    }

    protected void draw() {
        Display();
        textSize(28);
        fill(0, 159, 255);
        if (!Game) {                                            //Display Instructions while Game is not Running
            if (StartTime==0) {
                text("Press 1~5 to Select Difficulty", 360, 37);
            } else if (Found==Mine) {
                text("--- Congratulations ---", 360, 37);
                Effect[0].rewind();
                Effect[0].play();
            } else {
                text(">>  ENTER to Restart  <<", 360, 37);
            }
        } else {
            text("Level "+Level+" :  "+SizeX+" × "+SizeY+" Array", 360, 37);
        }                                                       //Display States while Game is Running
        textAlign(LEFT);
        textSize(32);
        fill(64);
        text("Mine: "+(Mine-Found), 10, 39);
        textAlign(RIGHT);
        if (StartTime!=0) {             //Modification: from <+ int((...)/1000),> to <+ (...)/1000,>
            text("Time: " + (millis()-StartTime)/1000, 710, 39);
        } else {
            textSize(20);
            if (Music.isPlaying()) {                            //Display Time since Start of Game
                text("SPACE to Mute", 710, 37);
            } else {
                text("by Kevin Liu", 710, 37);
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected void keyPressed() {
        switch (keyCode) {                                      //Exit Game
            case ESC:
                exit();
                break;
            case 19:
            case 32:
                if (Music.isPlaying()) {                        //Pause or Resume Music
                    Music.pause();
                    for (int X=0; X<5; X++) {
                        Effect[X].mute();
                    }
                } else {
                    Music.loop();
                    for (int X=0; X<5; X++) {
                        Effect[X].unmute();
                    }
                }
                break;
            case ENTER:                                         //Start new Game
                NewGame();
                break;
            case 49:
            case 112:
            case 97:                                            //Change Game Level to 1
                Level=1;
                SizeX=15;
                SizeY=15;
                Mine=30;
                NewGame();
                break;
            case 50:
            case 113:
            case 98:                                            //Change Game Level to 2
                Level=2;
                SizeX=20;
                SizeY=20;
                Mine=60;
                NewGame();
                break;
            case 51:
            case 114:
            case 99:                                            //Change Game Level to 3
                Level=3;
                SizeX=30;
                SizeY=30;
                Mine=150;
                NewGame();
                break;
            case 52:
            case 115:
            case 100:                                           //Change Game Level to 4
                Level=4;
                SizeX=36;
                SizeY=36;
                Mine=250;
                NewGame();
                break;
            case 53:
            case 116:
            case 101:                                           //Change Game Level to 5
                Level=5;
                SizeX=40;
                SizeY=40;
                Mine=350;
                NewGame();
                break;
        }
    }

    protected void mousePressed() {     //Modification: from <=int(...)> to <=(int)(...)>
        int MX=(int)((mouseX-2)/BlockX);
        int MY=(int)((mouseY-52)/BlockY);
        if (MX>=0 && MY>=0) {
            if (Game) {
                if (mouseButton==LEFT) {
                    if (State[MX][MY]==1) {                     //Check Win or Lose
                        Explore(MX, MY);
                        if (Game) {
                            CheckWin();
                        } else {
                            Lose();
                        }
                    } else if (State[MX][MY]==0) {              //Clear Tile
                        Mouse++;
                        if (Mouse==2) Clear(MX, MY);
                    }
                } else if (mouseButton==RIGHT) {
                    switch (State[MX][MY]) {
                        case 0:
                            Mouse++;                            //Clear known Surrounding Tile
                            if (Mouse==2) Clear(MX, MY);
                            break;
                        case 1:
                            State[MX][MY]++;                    //Mark as Mine
                            Found++;
                            Effect[3].rewind();
                            Effect[3].play();
                            CheckWin();
                            break;
                        case 2:
                            State[MX][MY]++;                    //Mark as Unknown
                            Found--;
                            CheckWin();
                            break;
                        case 3:
                            State[MX][MY]=1;                    //Mark as Regular Tile
                            break;
                    }
                }
            } else if (StartTime==0) {
                while (Map[MX][MY]==9) NewGame();
                Game=true;
                StartTime=millis();
                Explore(MX, MY);
                loop();
                CheckWin();
            }
        }
    }
    protected void mouseReleased() { if (Mouse>0) Mouse--; }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void NewGame() {                                    //Start a new Game
        Map=new byte[SizeX][SizeY];
        State=new byte[SizeX][SizeY];   //Modification: from <=int(720/SizeX)> to <=floor(720f/SizeX)>
        BlockX=floor(720f/SizeX);
        BlockY=floor(720f/SizeY);
        Found=0;
        StartTime=0;
        Game=false;

        for (int Y=0; Y<SizeY; Y++) {                           //Reset Map Field
            for (int X=0; X<SizeX; X++) {
                Map[X][Y]=0;
                State[X][Y]=1;
            }
        }
        for (int M=Mine; M>0; M--) {                            //Set Mine Location
            int X, Y;
            do {                        //Modification: from <=int(...)> to <=(int)(...)>
                X=(int)(random(0, SizeX));
                Y=(int)(random(0, SizeY));
            } while (Map[X][Y]==9);

            Map[X][Y]=9;                                        //Calculate Number on Map
            if (X>0) {
                if (Y>0 && Map[X-1][Y-1]!=9) Map[X-1][Y-1]++;
                if (Y<SizeY-1 && Map[X-1][Y+1]!=9) Map[X-1][Y+1]++;
                if (Map[X-1][Y]!=9) Map[X-1][Y]++;
            }
            if (X<SizeX-1) {
                if (Y>0 && Map[X+1][Y-1]!=9) Map[X+1][Y-1]++;
                if (Y<SizeY-1 && Map[X+1][Y+1]!=9) Map[X+1][Y+1]++;
                if (Map[X+1][Y]!=9) Map[X+1][Y]++;
            }
            if (Y>0) {
                if (Map[X][Y-1]!=9) Map[X][Y-1]++;
            }
            if (Y<SizeY-1) {
                if (Map[X][Y+1]!=9) Map[X][Y+1]++;
            }
        }
        redraw();
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void Display() {
        pushMatrix();                                           //Display Current Game Map
        image(Background, 0, 0, width, height);
        fill(255, 63);
        rect(0, 50, width, height);

        translate(1, 51);
        textSize(BlockY*1.25);
        textAlign(CENTER);

        for (int Y=0; Y<SizeY; Y++) {
            for (int X=0; X<SizeX; X++) {
                switch(State[X][Y]) {
                    case 0:
                        fill(255, 127);
                        rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                        switch (Map[X][Y]) {
                            case 9:
                                fill(223, 127);
                                rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                                fill(63);
                                ellipse((X+0.25)*BlockX, (Y+0.25)*BlockY, BlockX*0.5, BlockY*0.5);
                                break;
                            case 10:
                                fill(255, 191, 191);
                                rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                                fill(255, 0, 0);
                                ellipse((X+0.25)*BlockX, (Y+0.25)*BlockY, BlockX*0.5, BlockY*0.5);
                                break;
                            case 11:
                                fill(191, 255, 255);
                                rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                                fill(63);
                                ellipse((X+0.25)*BlockX, (Y+0.25)*BlockY, BlockX*0.5, BlockY*0.5);
                                break;
                            case 0:
                                break;
                            default:
                                fill(C[Map[X][Y]-1]);
                                text(Map[X][Y], (X+0.5)*BlockX, (Y+0.9)*BlockY);
                        }
                        break;
                    case 1:
                        fill(127);
                        rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                        strokeWeight(1);
                        stroke(63);
                        line(X*BlockX+2, (Y+1)*BlockY-2, (X+1)*BlockX-2, (Y+1)*BlockY-2);
                        line((X+1)*BlockX-2, Y*BlockY+2, (X+1)*BlockX-2, (Y+1)*BlockY-2);
                        stroke(191);
                        line(X*BlockX+1, Y*BlockY+1, (X+1)*BlockX-2, Y*BlockY+1);
                        line(X*BlockX+1, Y*BlockY+1, X*BlockX+1, (Y+1)*BlockY-2);
                        noStroke();
                        break;
                    case 2:
                        fill(191, 255, 255);
                        rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                        fill(63);
                        text("!", (X+0.5)*BlockX, (Y+0.9)*BlockY);
                        break;
                    case 3:
                        fill(255, 191, 255);
                        rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                        fill(63);
                        text("?", (X+0.5)*BlockX, (Y+0.9)*BlockY);
                        break;
                    case 4:
                        fill(255, 255, 191);
                        rect(X*BlockX+1, Y*BlockY+1, BlockX-2, BlockY-2);
                        fill(63);
                        text("!", (X+0.5)*BlockX, (Y+0.9)*BlockY);
                        break;
                }
            }
        }
        popMatrix();
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void Explore(int X, int Y) {
        if (State[X][Y]==1) {                                   //Reveal all Surrounding Tiles
            State[X][Y]=0;
            CheckLose(X, Y);
            Effect[2].pause();
            Effect[2].rewind();

            if (Map[X][Y]==0) {
                NextBlock(X, Y);
                if (Game) Effect[2].play();
            }
        }
    }
    private void NextBlock(int X, int Y) {                      //Keep Exploring if Surrounding Tiles still have no Mine
        if (X>0) {
            Explore(X-1, Y);
            if (Y>0) Explore(X-1, Y-1);
            if (Y<SizeY-1) Explore(X-1, Y+1);
        }
        if (X<SizeX-1) {
            Explore(X+1, Y);
            if (Y>0) Explore(X+1, Y-1);
            if (Y<SizeY-1) Explore(X+1, Y+1);
        }
        if (Y>0) Explore(X, Y-1);
        if (Y<SizeY-1) Explore(X, Y+1);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void Clear(int X, int Y) {                          //Count Mines in Surrounding
        byte Index=0;

        if (Map[X][Y]!=0) {
            if (X>0) {
                if (State[X-1][Y]==2) Index++;
                if (Y>0) {
                    if (State[X-1][Y-1]==2) Index++;
                }
                if (Y<SizeY-1) {
                    if (State[X-1][Y+1]==2) Index++;
                }
            }
            if (X<SizeX-1) {
                if (State[X+1][Y]==2) Index++;
                if (Y>0) {
                    if (State[X+1][Y-1]==2) Index++;
                }
                if (Y<SizeY-1) {
                    if (State[X+1][Y+1]==2) Index++;
                }
            }
            if (Y>0 && State[X][Y-1]==2) Index++;
            if (Y<SizeY-1 && State[X][Y+1]==2) Index++;

            if (Index==Map[X][Y]) {
                NextBlock(X, Y);
                if (Game) {
                    CheckWin();
                } else {
                    Lose();
                }
            } else {
                Effect[1].rewind();
                Effect[1].play();
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void CheckLose(int X, int Y) {                      //Check Lose Condition
        if (Map[X][Y]==9) {
            Map[X][Y]=10;
            Game=false;
        }
    }
    private void Lose() {
        for (int Y=0; Y<SizeY; Y++) {
            for (int X=0; X<SizeX; X++) {
                if (Map[X][Y]==9) {
                    if (State[X][Y]==2) Map[X][Y]=11;
                    State[X][Y]=0;
                } else if (State[X][Y]==2) {
                    State[X][Y]=4;
                    Found--;
                }
            }
        }
        noLoop();
        redraw();
        Effect[4].rewind();
        Effect[4].play();
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void CheckWin() {                                   //Check Win Condition
        boolean Correct=true;

        for (int Y=0; Y<SizeY; Y++) {
            for (int X=0; X<SizeX; X++) {
                if (Map[X][Y]!=9 && State[X][Y]!=0) Correct=false;
            }
        }
        if (Correct) {
            Win();
        } else if (Found==Mine) {
            Correct=true;
            for (int Y=0; Y<SizeY; Y++) {
                for (int X=0; X<SizeX; X++) {
                    if (Map[X][Y]!=9 && State[X][Y]==2) Correct=false;
                }
            }
            if (Correct) Win();
        }
    }
    private void Win() {
        Game=false;
        Found=Mine;
        for (int Y=0; Y<SizeY; Y++) {
            for (int X=0; X<SizeX; X++) {
                if (Map[X][Y]==9) {
                    if (State[X][Y]==2) Map[X][Y]=11;
                    State[X][Y]=0;
                }
            }
        }
        noLoop();
        redraw();
    }
}