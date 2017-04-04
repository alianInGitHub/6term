import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by anastasia on 4/4/17.
 */
public class HunterGame extends JComponent {

    private class Controller implements Runnable {

        @Override
        public void run() {
            for (;;) {
                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Duck implements Runnable {
        private int speed;
        private Point coordinates;
        private boolean flyToRightSide;
        private boolean upFlight;
        private boolean isDead;

        public Duck() {
            generateAbilities();
        }

        private void generateAbilities() {
            speed = random.nextInt(10) + 5;
            coordinates = new Point();
            coordinates.y = random.nextInt(HEIGHT / 2);
            flyToRightSide = random.nextBoolean();
            if(flyToRightSide) {
                coordinates.x = 0;
            } else {
                coordinates.x = WIDTH - DUCK_SIDE;
            }
            upFlight = false;
            isDead = false;
        }

        private void drawDuck() {
            try {
                lock.lock();
                if (isDead) {
                    graphics2D.drawImage(duckDead, duckOp, coordinates.x, coordinates.y);
                    return;
                }

                if (flyToRightSide) {
                    if (upFlight) {
                        graphics2D.drawImage(duckRightUp, duckOp, coordinates.x, coordinates.y);
                    } else {
                        graphics2D.drawImage(duckRightDown, duckOp, coordinates.x, coordinates.y);
                    }
                } else {
                    if (upFlight) {
                        graphics2D.drawImage(duckLeftUp, duckOp, coordinates.x, coordinates.y);
                    } else {
                        graphics2D.drawImage(duckLeftDown, duckOp, coordinates.x, coordinates.y);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private void update() {
            if (isDead) {
                coordinates.y += 2.0;
                if (coordinates.y > HEIGHT) {
                    generateAbilities();
                    try {
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else
            if (flyToRightSide) {
                coordinates.x += speed;
            } else {
                coordinates.x -= speed;
            }
            upFlight = !upFlight;
            if (coordinates.x < 0 || coordinates.x > WIDTH) {
                generateAbilities();
                try {
                    Thread.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            for (;;) {
                drawDuck();
                update();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //................................................CONSTANTS.......................................................//

    static final int WIDTH = 900;
    static final int HEIGHT = 600;
    private final int DUCK_SIDE = 60;

    //.............................................PRIVATE FIELDS.....................................................//

    private Graphics2D graphics2D;
    private Image image;
    private BlockingDeque<Thread> ducks;

    private BufferedImage background;
    private AffineTransformOp backgroundOp;

    private BufferedImage duckRightUp;
    private BufferedImage duckRightDown;
    private BufferedImage duckLeftUp;
    private BufferedImage duckLeftDown;
    private BufferedImage duckDead;
    private AffineTransformOp duckOp;

    private Random random;
    private ReentrantLock lock;


    //.............................................PUBLIC METHODS.....................................................//

    public HunterGame() {
        setSize(WIDTH, HEIGHT);
        initialize();
        loadBackground();
        loadDuck();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(image == null) {
            initializeGraphics();
            start();
        }
        lock.lock();
        g.drawImage(image, 0, 0, null);
        clear();
        lock.unlock();
    }

    public void start() {
        generateDucks();
        new Thread(new Controller()).start();
    }

    //............................................PRIVATE METHODS.....................................................//

    private void initialize() {
        random = new Random();
        lock = new ReentrantLock();
    }

    private void initializeGraphics() {
        image = createImage(getSize().width, getSize().height);
        graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clear();
    }

    private BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.drawImage(img, 0, 0, null);
        Image temp = setTransparentBackGround(image);
        return ImageToBufferedImage(temp, temp.getWidth(null), temp.getHeight(null));
    }

    private BufferedImage ImageToBufferedImage(Image image, int width, int height)
    {
        BufferedImage dest = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }

    private Image setTransparentBackGround(BufferedImage img) {
        ImageFilter filter = new RGBImageFilter() {
            int transparentColor = Color.white.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == transparentColor) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private void loadBackground() {
        int h = 1;
        background = loadImage("pictures/b.gif");
        AffineTransform at = AffineTransform.getScaleInstance((double) (HEIGHT + 10) / (double)background.getHeight(), (double) (HEIGHT + 10) / (double)background.getHeight());
        backgroundOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
    }

    private void loadDuck() {
        duckRightDown = loadImage("pictures/rightDown.png");
        duckRightUp = loadImage("pictures/rightUp.png");
        duckLeftDown = loadImage("pictures/leftDown.png");
        duckLeftUp = loadImage("pictures/leftUp.png");
        duckDead = loadImage("pictures/dead.png");

        AffineTransform at = AffineTransform.getScaleInstance(DUCK_SIDE / 105.0, DUCK_SIDE / 105.0);
        duckOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
    }

    private void generateDucks() {
        ducks = new LinkedBlockingDeque<>(10);
        int n = random.nextInt(3) + 2;
        for (int i = 0; i < n; i++) {
            ducks.add(new Thread(new Duck()));
            ducks.getLast().start();
        }
    }

    private void clear() {
        graphics2D.drawImage(background, backgroundOp, 0, 0);
    }

}
