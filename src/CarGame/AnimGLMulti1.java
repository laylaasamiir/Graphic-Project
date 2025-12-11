package CarGame;


import Texture.TextureReader;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

class AnimGLMulti1 extends AnimListener {


    public AnimGLMulti1() {
        resetEnemy1();
        resetEnemy2();
    }

    String[] textureNames = {"road1multi.png", "Rcar.png", "Gcar.png", "Bcar.png", "Vcar.pbng ", "player1.png","player2.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];

    int maxWidth = 100;
    int maxHeight = 100;

    double player1X = maxWidth / 3.0;
    double player1Y = maxHeight / 5.0;
    double player2X = 2 * maxWidth / 3.0;
    double player2Y = maxHeight / 5.0;

    double player1RoadLeft = maxWidth * 0.1;
    double player1RoadRight = maxWidth * 0.4;
    double player2RoadLeft = maxWidth * 0.6;
    double player2RoadRight = maxWidth * 0.93;
    double playerYMin = maxHeight * 0.165;
    double playerYMax = maxHeight * 0.9;

    double player1Rotation = 0.0;
    double player2Rotation = 0.0;

    float backgroundOffset = 0.0f;
    float backgroundSpeed = 0.05f;

    boolean wPressed = false, sPressed = false, aPressed = false, dPressed = false;
    boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;


    int winner = 0;
    double enemy1X, enemy1Y, enemy2X, enemy2Y;
    Random random = new Random();

    double carWidth = 5.0;
    double carHeight = 19.0;

    boolean gameOver = false;

    private void resetEnemy1() {
        enemy1X = player1RoadLeft + random.nextDouble() * (player1RoadRight - player1RoadLeft);
        enemy1Y = playerYMax;
    }

    private void resetEnemy2() {
        enemy2X = player2RoadLeft + random.nextDouble() * (player2RoadRight - player2RoadLeft);
        enemy2Y = playerYMax;
    }

    public boolean checkCollision(double playerX, double playerY, double enemyX, double enemyY) {
        double deltaX = Math.abs(enemyX - playerX);
        double deltaY = Math.abs(enemyY - playerY);

        if (deltaX < carWidth && deltaY < carHeight) {
            return true;
        }
        return false;
    }
    public void handleCollision() {
        if (checkCollision(player1X, player1Y, enemy1X, enemy1Y) ||
                checkCollision(player1X, player1Y, enemy2X, enemy2Y)) {
            System.out.println("Player 1 collided with an enemy car!");
            gameOver = true;
            winner = 2;
            return;
        }

        if (checkCollision(player2X, player2Y, enemy1X, enemy1Y) ||
                checkCollision(player2X, player2Y, enemy2X, enemy2Y)) {
            System.out.println("Player 2 collided with an enemy car!");
            gameOver = true;
            winner = 1;
        }
    }


    public void clampPlayerPosition() {
        player1X = Math.max(player1RoadLeft, Math.min(player1X, player1RoadRight));
        player1Y = Math.max(playerYMin, Math.min(player1Y, playerYMax));

        player2X = Math.max(player2RoadLeft, Math.min(player2X, player2RoadRight));
        player2Y = Math.max(playerYMin, Math.min(player2Y, playerYMax));
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();



        if (gameOver) {
            DrawBackground(gl);

            if (winner == 1) {
                DrawSprite(gl, maxWidth / 2.0, maxHeight - 50, 5, 2.0f, 0); // Adjust Y position for top of screen
            } else if (winner == 2) {
                DrawSprite(gl, maxWidth / 2.0, maxHeight - 50, 6, 2.0f, 0); // Adjust Y position for top of screen
            }

            DrawSprite(gl, player1X, player1Y, 1, 2.0f, player1Rotation);
            DrawSprite(gl, player2X, player2Y, 2, 2.0f, player2Rotation);
            DrawSprite(gl, enemy1X, enemy1Y, 3, 2.0f, 0);
            DrawSprite(gl, enemy2X, enemy2Y, 4, 2.0f, 0);

            return;
        }

        backgroundOffset += backgroundSpeed;
        if (backgroundOffset > 1.0f) {
            backgroundOffset -= 1.0f;
        }
        DrawBackground(gl);

        enemy1Y -= 1.5;
        enemy2Y -= 1.5;

        if (enemy1Y < playerYMin) resetEnemy1();
        if (enemy2Y < playerYMin) resetEnemy2();

        if (wPressed) player1Y += 2.0;
        if (sPressed) player1Y -= 2.0;
        if (aPressed) {
            player1X -= 2.0;
            player1Rotation = 8;
        }
        if (dPressed) {
            player1X += 2.0;
            player1Rotation = -8;
        }

        if (upPressed) player2Y += 2.0;
        if (downPressed) player2Y -= 2.0;
        if (leftPressed) {
            player2X -= 2.0;
            player2Rotation = 8;
        }
        if (rightPressed) {
            player2X += 2.0;
            player2Rotation = -8;
        }

        clampPlayerPosition();

        handleCollision();

        DrawSprite(gl, player1X, player1Y, 1, 2.0f, player1Rotation);
        DrawSprite(gl, player2X, player2Y, 2, 2.0f, player2Rotation);
        DrawSprite(gl, enemy1X, enemy1Y, 3, 2.0f, 0);
        DrawSprite(gl, enemy2X, enemy2Y, 4, 2.0f, 0);
    }




    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f + backgroundOffset);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f + backgroundOffset);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f + backgroundOffset);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f + backgroundOffset);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite(GL gl, double x, double y, int index, float scale, double rotation) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1.0, y / (maxHeight / 2.0) - 1.0, 0);
        gl.glRotated(rotation, 0, 0, 1);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            sPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            aPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            dPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            upPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            sPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            aPressed = false;
            player1Rotation = 0.0;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            dPressed = false;
            player1Rotation = 0.0;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            upPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
            player2Rotation = 0.0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            player2Rotation = 0.0;
        }
    }

}
