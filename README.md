# JProcessing
<h5>JProcessing is a Processing Code API for Java. It Enables Processing Code to be Programmed and Run directly in Java IDE.</h5>
<h3>Introduction</h3>
<p>
Processing is a programming language focused mainly on visual arts and animations in GUI programming. It provides a variety of simple and efficient methods for 2D graphics rendering, image/font displaying, as well as file operations. If you are unfamiliar with Processing language, please refer to <a href="https://processing.org/">Processing's official website</a> for more information.
<p>
Jprocessing allows you to to program and run Processing code in any Java IDE without using the official <a href="https://processing.org/download/">Processing IDE</a>. It enables you to write Processing code in Java integrated development environment(IDE) you prefer, as mainstream Java IDEs usually offers more functionalities.
<p>
Note: There is also an official way to run Processing code in Java <a href="https://happycoding.io/tutorials/java/processing-in-java">using PApplet</a>. Jprocessing is an similar approach to it. With the difference being JProcessing only implements the most frequent-used methods from processing. Methods like 3D rendering, matrix operations, reading PDFs etc. are not included. Therefore, it performs just like a much simplified version of <code>PApplet</code> that only contains essential features.

<h3>What is JProcessing</h3>
<p>
JProcessing is a .jar library. It is a Java API designed to implement Processing methods and functionality to Java programming environment. JProcessing contains methods of Processing in areas of 2D rendering, input/output, Math, <code>PFont</code>, <code>PImage</code>, and <code>PVector</code>. The main class <code>JProcessing.Processing</code> is the Processing GUI window (a subclass of <code>JFrame</code>), and contains Processing methods <code>void setup()</code>,<code>void draw()</code> and other input and event methods such as <code>mousePressed()</code>,<code>keyReleased()</code> etc.
<p>
After including JProcessing in your Java project, you can then program and run Processing code in Java normally. Most of the JProcessing methods perform the same functionality as of within Processing IDE, with a few exceptions. You can copy Processing code and paste them directly into Java IDE with JProcessing, and then run it. Most of them will run just fine (after a few small modifications that is).

<h3>How to use JProcessing</h3>
<p>
To implement Processing features in Java, first you need to include JProcessing jar library in your Java project. Then, you need to create a subclass of <code>JProcessing.Processing</code>. Your Processing codes can then be put inside it. Override <code>void setup()</code>,<code>void draw()</code> and other methods whenever they're needed. Finally, initialize a new object from your <code>JProcessing.Processing</code> subclass to start your GUI, and everything should work properly after that according to your code.
<p>
The templates of JProcessing implementation are included in the files (setup as IntelliJ projects), together with Javadocs providing the information that will help you better use and understand JProcessing library. The known issues and debugging guides are also included. The PDF version of the documentation is named "Template Manual.pdf". In the case of if anything goes wrong while using JProcessing, the issue and its solution might be already documented in the manual. So please have a look at it when any issue occurs.
<p>
If you are confused about certain method(s), you can check out <a href="https://processing.org/reference/"> the full documentation of Processing methods</a> on the Processing website. Alternatively, the Javadoc of JProcessing is also included in the folder. It contains documentation for all JProcessing classes and fields, and the difference between how Processing IDE and JProcessing works. To my apologies, most of the methods of JProcessing are not documented in its Javadoc (since it's truly time-consuming). You can just Google: "Method_Name" + "Processing", which will lead you to the documentation page of any Processing method on its official website.

<h3>About Development</h3>
<p>
Yes, the source code is included and you are free to use, share and edit them. You are more than welcomed to contribute as well. Note that the source code for the official Processing IDE is also on the GitHub. I would say those are also worthy checking out if you are interested in developing Processing (links <a href="https://github.com/processing">here</a>). Note that JProcessing does not referred or related to code from Processing IDE by any means. It is a stand-along project started completely from scratch.
<p>
I personally positioning JProcessing as a simple add-on in java that allows the basic function of Processing to be used in Java programming. The main goal is to make it simple (single jar file), small (only includes essential and frequently used functions), and convenient to use. Since otherwise, it might be better of working directly on Processing IDE for adding advanced and complex features.
<h6>
Update is not guaranteed in the future. Please contact me if you find any bugs or have any other concerns, and I will try to resolve them as soon as possible.</h6>
<h5>Have Fun!</h5>
