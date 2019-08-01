import JProcessing.Processing;
import java.util.ArrayList;
                                        //More Processing Examples at: https://processing.org/examples
public class Main extends Processing {
    private Main(){ super(); }
    public static void main(String[] args){ new Main(); }
//================================================================================================================================
                                        //Example: "SimpleParticleSystem.pde"
    ParticleSystem ps;                  //Source:  https://processing.org/examples/simpleparticlesystem.html

    public void setup() {
        size(720, 640);
        ps = new ParticleSystem(new PVector(width/2, 100));
    }

    public void draw() {
        background(0);
        ps.addParticle();
        ps.run();
    }

// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles

    class ParticleSystem {
        ArrayList<Particle> particles;
        PVector origin;

        ParticleSystem(PVector position) {
            origin = position.copy();
            particles = new ArrayList<Particle>();
        }

        void addParticle() {
            particles.add(new Particle(origin));
        }

        void run() {
            for (int i = particles.size()-1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.run();
                if (p.isDead()) {
                    particles.remove(i);
                }
            }
        }
    }

// A simple Particle class

    class Particle {
        PVector position;
        PVector velocity;
        PVector acceleration;
        float lifespan;

        Particle(PVector l) {
            acceleration = new PVector(0, 0.05);
            velocity = new PVector(random(-1.25, 1.25), random(-2.5, 0));
            position = l.copy();
            lifespan = 255;
        }

        void run() {
            update();
            display();
        }

        // Method to update position
        void update() {
            velocity.add(acceleration);
            position.add(velocity);
            lifespan -= 1.0;
        }

        // Method to display
        void display() {
            stroke(255, lifespan);
            fill(255, lifespan);
            ellipse(position.x, position.y, 8, 8);
        }

        // Is the particle still useful?
        boolean isDead() {
            if (lifespan < 0.0) {
                return true;
            } else {
                return false;
            }
        }
    }
}