package in.liquidmetal.dubsteptetris;


import android.sax.TextElementListener;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by utkarsh on 5/6/13.
 * This class implements an awesome background that is used throughout the game
 */
public class GLBackground {
    private class Particle {
        private long MAX_AGE = 1000;

        private float vx, vy;
        private float px, py;
        private float sx, sy;
        private long age;
        private AlignedRect rect;

        public Particle(float px, float py, float vx, float vy, float sx, float sy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
            this.sx = sx;
            this.sy = sy;

            rect = new AlignedRect();
            rect.setPosition(px, py);
            rect.setScale(sx, sy);
            rect.setColor(0,0,1);

            MAX_AGE += (float)Math.random() * 1000 -500f;
        }

        public void draw() {
            rect.draw();
        }

        public boolean update(long timeDelta) {
            px += vx*timeDelta;
            py += vy*timeDelta;

            rect.setPosition(px, py);

            age += timeDelta

            if(age>MAX_AGE && Math.random()>0.5)
                return false;

            return true;
        }
    }

    private static long PARTICLE_COUNT = 100;

    private LinkedList<Particle> mParticles;
    private long lastUpdateTime = -1;

    public GLBackground() {
        mParticles = new LinkedList<Particle>();
        initializeParticles();
        lastUpdateTime = (new Date()).getTime();
    }

    public void initializeParticles() {
        while(mParticles.size() < PARTICLE_COUNT) {
            spawnNewParticle();
        }
    }

    private void spawnNewParticle() {
        mParticles.add(new Particle((float)Math.random()*700, (float)Math.random()*1200, (float)Math.random()-0.5f, (float)Math.random()-0.5f, (float)Math.random()*32, (float)Math.random()*32f));
    }

    public void update() {
        long newUpdateTime = (new Date()).getTime();
        Iterator<Particle> iter = mParticles.iterator();
        while(iter.hasNext()) {
            Particle currentParticle = iter.next();
            boolean ret = currentParticle.update(newUpdateTime - lastUpdateTime);

            if(!ret) {
                iter.remove();
            }
        }

        initializeParticles();


        lastUpdateTime = newUpdateTime;
    }

    public void draw() {
        for(Particle p:mParticles) {
            p.draw();
        }
    }
}
