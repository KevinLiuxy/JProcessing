import ddf.minim.*;

byte SizeX=30, SizeY=30;
float BlockX, BlockY;
byte[][] Map;
byte[][] State;

color C[]={color(0, 95, 191), color(95, 191, 0), color(191, 95, 0), color(191, 0, 95), color(255, 0, 0), color(0, 191, 95), color(95, 0, 191), color(0, 0, 0)};
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
void setup() {
  size(722, 772);
  noStroke();
  noLoop();
  frameRate(15);
  textFont(createFont("Calibri", 32));

  Background=loadImage("Data/IMG.jpg");
  Player=new Minim(this);
  Music=Player.loadFile("Data/music.mp3");
  for (int X=0; X<5; X++) {
    Effect[X]=Player.loadFile("Data/a"+X+".mp3");
  }

  Music.setGain(-15);
  Music.setLoopPoints(51, 90503);
  Music.loop();
  ellipseMode(CORNER);
  NewGame();
}

void draw() {
  Display();

  textSize(28);
  fill(0, 159, 255);
  if (!Game) {
    noLoop();
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
  }
  textAlign(LEFT);
  textSize(32);
  fill(64);
  text("Mine: "+(Mine-Found), 10, 39);
  textAlign(RIGHT);
  if (StartTime!=0) {
    text("Time: "+int((millis()-StartTime)/1000), 710, 39);
  } else {
    textSize(20);
    if (Music.isPlaying()) {
      text("SPACE to Mute", 710, 37);
    } else {
      text("by Kevin Liu", 710, 37);
    }
  }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
void keyPressed() {
  switch (keyCode) {
  case ESC:
    exit();
    break;
  case 19:
  case 32:
    if (Music.isPlaying()) {
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
  case ENTER: 
    NewGame();
    break;
  case 49: 
  case 112: 
  case 97:
    Level=1;
    SizeX=15;
    SizeY=15;
    Mine=30;
    NewGame();
    break;
  case 50: 
  case 113: 
  case 98:
    Level=2;
    SizeX=20;
    SizeY=20;
    Mine=60;
    NewGame();
    break;
  case 51: 
  case 114: 
  case 99:
    Level=3;
    SizeX=30;
    SizeY=30;
    Mine=150;
    NewGame();
    break;
  case 52: 
  case 115: 
  case 100:
    Level=4;
    SizeX=36;
    SizeY=36;
    Mine=250;
    NewGame();
    break;
  case 53: 
  case 116: 
  case 101:
    Level=5;
    SizeX=40;
    SizeY=40;
    Mine=350;
    NewGame();
    break;
  }
}

void mousePressed() {
  int MX=int((mouseX-2)/BlockX);
  int MY=int((mouseY-52)/BlockY);
  if (MX>=0 && MY>=0) {

    if (Game) {
      if (mouseButton==LEFT) {
        if (State[MX][MY]==1) {
          Explore(MX, MY);
          if (Game) {
            CheckWin();
          } else {
            Lose();
          }
        } else if (State[MX][MY]==0) {
          Mouse++;
          if (Mouse==2) Clear(MX, MY);
        }
      } else if (mouseButton==RIGHT) {
        switch (State[MX][MY]) {
        case 0:
          Mouse++;
          if (Mouse==2) Clear(MX, MY);
          break;
        case 1:
          State[MX][MY]++;
          Found++;
          Effect[3].rewind();
          Effect[3].play();
          CheckWin();
          break;
        case 2:
          State[MX][MY]++;
          Found--;
          CheckWin();
          break;
        case 3:
          State[MX][MY]=1;
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

void mouseReleased() {
  if (Mouse>0) Mouse--;
}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
void NewGame() {
  Map=new byte[SizeX][SizeY];
  State=new byte[SizeX][SizeY];
  BlockX=int(720/SizeX);
  BlockY=int(720/SizeY);
  Found=0;
  StartTime=0;
  Game=false;

  for (int Y=0; Y<SizeY; Y++) {
    for (int X=0; X<SizeX; X++) {
      Map[X][Y]=0;
      State[X][Y]=1;
    }
  }

  for (int M=Mine; M>0; M--) {
    int X, Y;
    do {
      X=int(random(0, SizeX));
      Y=int(random(0, SizeY));
    } while (Map[X][Y]==9);

    Map[X][Y]=9;
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
void Display() {
  pushMatrix();
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
void Explore(int X, int Y) {
  if (State[X][Y]==1) {
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

void NextBlock(int X, int Y) {
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
void Clear(int X, int Y) {
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
void CheckLose(int X, int Y) {
  if (Map[X][Y]==9) {
    Map[X][Y]=10;
    Game=false;
  }
}

void Lose() {
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
  Effect[4].rewind();
  Effect[4].play();
}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------
void CheckWin() {
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

void Win() {
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
}