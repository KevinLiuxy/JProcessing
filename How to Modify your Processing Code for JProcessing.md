After copying and pasting your Processing code into a subclass of <code>JProcessing.Processing</code>, There will be a few changes that is required to make for your code to run in Java IDE:
<h4>1. Decimal Precisions</h4>
Java's default decimal precision is <code>double</code>, while Processing's use <code>float</code> as its default. When assigning values to float numbers, change: <code>float n = 0.5;</code> to: <code>float n = 0.5f;</code>

<h4>2. Variable Casting</h4>
Processing uses different variable casting syntax compared to Java. In Processing, variable casting using the following syntax:<br>
<code>m = int(0.5); n=char(27); p=float(PI);</code> needs to be changed to:<br>
<code>m = (int)0.5; n=(char)27; p=(float)Math.PI;</code> in Java IDE.

<h4>3. Method Modifier</h4>
All event methods in <code>JProcessing.Processing</code> have scope modifiers <code>protected</code>. They will have to be also assigned with <code>protected</code> or <code>public</code> when you override them. For example:<br>
change <code>void setup(){ }</code> to <code>public/protected void setup(){ }</code> ,<br>
change <code>void draw(){ }</code> to <code>public/protected void draw(){ }</code> ,<br>
change <code>void mousePressed(){ }</code> to <code>public/protected void mousePressed(){ }</code> etc.