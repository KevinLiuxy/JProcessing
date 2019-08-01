PFont f;

void setup() {
  size(960, 540);
  background(0);

  // Create the font
  printArray(PFont.list());
  f = createFont("Calibri", 40);
  textFont(f);
  textAlign(CENTER, CENTER);
} 

void draw() {
  background(0);
  // Set the left and top margin
  int margin = 8;
  translate(margin*6, margin*6);

  int gap = 72;
  int counter = 35;
  
  for (int y = 0; y < height-gap; y += gap) {
    for (int x = 0; x < width-gap; x += gap) {

      char letter = char(counter);
      
      if (letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U') {
        fill(255, 204, 0);
      } else {
        fill(255);
      }
      // Draw the letter to the screen
      text(letter, x, y);
      // Increment the counter
      counter++;
    }
  }
}