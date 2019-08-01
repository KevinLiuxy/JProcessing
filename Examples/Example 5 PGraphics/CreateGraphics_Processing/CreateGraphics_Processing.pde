PGraphics pg;

void setup() {
  size(960, 540);
  pg = createGraphics(600, 300);
}

void draw() {
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