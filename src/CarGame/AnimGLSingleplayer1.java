package CarGame;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;

public class AnimGLSingleplayer1 extends AnimListener  {


    public AnimGLSingleplayer1() {}

    String[] textureNames = {
            "vv.png", "Rcar.png", "Bcar.png", "Pcar.png",
            "1.png","2.png","3.png","4.png","5.png","6.png","7.png",
            "8.png","9.png","10.png"
            ,"SCORE.png","winner2.png","lose.png","live.png", "pause.png"
    };    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];

    public int[] textures = new int[textureNames.length];

    int maxWidth = 100;
    int maxHeight = 100;
    private BitSet keyboardBitSet = new BitSet(256);
    boolean isPaused = false;



    double Leftbound = maxWidth * 0.143;
    double Rightbound = maxWidth * 0.85;
    double roadLeft = maxWidth * 0.143;
    double roadRight = maxWidth * 0.85;


    double playerX = maxWidth / 2.0;
    double playerY = maxHeight / 5.0;

    double enemy1X, enemy1Y, enemy2X, enemy2Y;
    float enemySpeed = 0.6f;
    float backgroundset = 0.0f;
    float backgroundSpeed = 0.05f;
    double carAngle = 0.0;
    private boolean disabled = false;
    double sideCollisionThreshold = 8.5;
    double backCollisionThreshold = 18.0;


    double playerYbottom = maxHeight * 0.165;
    double playerYtop = maxHeight * 0.9;
    Random random = new Random();
    boolean isGameOver = false;
    boolean isGameFinished = false;
    public int displayTimer = 0;
    public int currentImageIndex = 4;
    public final int IMAGE_DISPLAY_DURATION = 50;
    public boolean isImageChanging = false;
    int Lives = 3 ;



    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("assets//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );

                System.out.println("Texture loaded successfully: " + textureNames[i]);
            } catch (IOException e) {
                System.err.println("Error loading texture: " + textureNames[i]);
                e.printStackTrace();
            }
        }

        System.out.println("All textures loaded successfully.");
        // new JFXPanel();

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        DrawBackground(gl);

        DrawSprite(gl, playerX, playerY, 1, 2.0f);
        if (isPaused) {
            DrawSprite(gl, 50, 50, textureNames.length - 1, 3.0f);
            return;
        }
        if(currentImageIndex==8){
            backgroundSpeed=0.07f;
            enemySpeed=0.7f;
        }
        if (!isGameOver && !isGameFinished) {
            if (isImageChanging) {
                float scale = 2.0f;
                DrawSprite(gl, 50, 90, currentImageIndex, scale);

                displayTimer++;
                if (displayTimer >= IMAGE_DISPLAY_DURATION) {
                    displayTimer = 0;
                    currentImageIndex++;

                    if (currentImageIndex == textureNames.length - 5) {
                        backgroundSpeed = 0;
                        isImageChanging = false;
                        isGameFinished = true;
                        return;
                    }
                }
            } else {
                isImageChanging = true;
            }

            updateEnemyPositions();

            if (!isGameOver && !isGameFinished) {
                updateEnemyPositions();

                if (checkCollision(playerX, playerY, enemy1X, enemy1Y) ||
                        checkCollision(playerX, playerY, enemy2X, enemy2Y)) {
                    Lives--;
                    if (Lives > 0) {
                        resetEnemyPositions();
                        System.out.println("Collision detected! Lives remaining: " +Lives);
                    } else {
                        isGameOver = true;

                        System.out.println("Game Over! Collision Detected.");
                    }
                }
            }
        }
        DrawSprite(gl, enemy1X, enemy1Y, 2, 2.0f);
        DrawSprite(gl, enemy2X, enemy2Y, 3, 2.0f);

        if (isGameOver) {
            disabled = true;
            isImageChanging = false;

            DrawSprite(gl,50,50,16,5);
            DrawSprite(gl, 60, 75, currentImageIndex, 1.5f);
            DrawSprite(gl, 30, 60, 14, 5);
        }

        if (isGameFinished) {
            disabled = true;

            DrawSprite(gl, 50, 55, 15, 5);
        }

        for (int i = 0; i < Lives; i++) {
            DrawFrame(gl, 15 + i, 0.75 + (i * 0.07));
        }




    }



    public void updateEnemyPositions() {
        enemy1Y -= enemySpeed;
        enemy2Y -= enemySpeed;

        if (enemy1Y < 0) {
            enemy1Y = maxHeight;
            enemy1X = roadLeft + random.nextDouble() * (roadRight - roadLeft);
        }
        if (enemy2Y < 0) {
            enemy2Y = maxHeight + 60;
            enemy2X = roadLeft + random.nextDouble() * (roadRight - roadLeft);
        }
    }



    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f + backgroundset);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f + backgroundset);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f + backgroundset);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f + backgroundset);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);

        if (!isPaused && !isGameOver) {
            backgroundset += backgroundSpeed;
            if (backgroundset > 1.0f) {
                backgroundset -= 1.0f;
            }
        }
    }

    public void DrawSprite(GL gl, double x, double y, int index, float scale) {
        if (textures[index] == 0) {
            System.err.println("Texture not ready: " + index);
            return;
        }

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1.0, y / (maxHeight / 2.0) - 1.0, 0);

        if (x == playerX && y == playerY) {
            gl.glRotated(carAngle, 0, 0, 1);
        }

        gl.glScaled(0.1 * scale, 0.1 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public boolean checkCollision(double x1, double y1, double x2, double y2) {
        double deltaX = Math.abs(x2 - x1);
        double deltaY = Math.abs(y2 - y1);

        if (deltaX < sideCollisionThreshold && deltaY < sideCollisionThreshold) {
            return true;
        }

        if (deltaY < backCollisionThreshold && deltaX < sideCollisionThreshold) {
            return true;
        }

        return false;
    }


    public void resetEnemyPositions() {
        enemy1X = roadLeft + random.nextDouble() * (roadRight - roadLeft);
        enemy1Y = maxHeight;

        enemy2X = roadLeft + random.nextDouble() * (roadRight - roadLeft);
        enemy2Y = maxHeight + 60;
    }
    public void DrawFrame(GL gl, int i, double tr) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[17]);

        gl.glPushMatrix();
        gl.glTranslated(tr, 0.88, 0);
        gl.glScaled(0.04, 0.04, 1.0);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }


    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (disabled) return;

        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            isPaused = !isPaused;
            System.out.println("Pause state: " + isPaused);

            return;
        }




        if (isGameOver || isPaused) return;

        if (keyCode == KeyEvent.VK_UP) {
            playerY += 2.0;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            playerY -= 2.0;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            playerX -= 2.0;
            carAngle = 8;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            playerX += 2.0;
            carAngle = -8;
        }

        if (playerX < Leftbound) {
            playerX = Leftbound;
        } else if (playerX > Rightbound) {
            playerX = Rightbound;
        }
        if (playerY < playerYbottom) {
            playerY = playerYbottom;
        } else if (playerY > playerYtop) {
            playerY = playerYtop;
        }
        keyboardBitSet.set(e.getKeyCode(), true);



    }



    @Override
    public void keyReleased(KeyEvent e) {
        if (disabled) return;
        handleKeyRelease(e.getKeyCode());
        keyboardBitSet.set(e.getKeyCode(),false);
    }

    public void handleKeyRelease(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            carAngle = 0;
        }
    }

}


