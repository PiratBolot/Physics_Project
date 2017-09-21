package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.Image;

public class Model {

    Vec2d movement;
    BallCenter center;
    double tempx;
    double tempy;
    static double mass;
    Image img;

    class BallCenter
    {
        private double x, y;

        BallCenter(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        void changeX(double t) {
            center.x = t;
        }
        void changeY(double t) {
            center.y = t;
        }
    }

    public Model(double x, double y, Vec2d move, Image img, double mass) {
        center = new BallCenter(x, y);
        movement = new Vec2d(move);
        tempx = 0;
        tempy = 0;
        this.img = img;
        this.mass = mass;
    }

    static public void moveBalls() {
        for (int i = 0; i < AdditionWindow.balls.size(); i++) {
            AdditionWindow.balls.get(i).tempx = AdditionWindow.balls.get(i).center.getX() + AdditionWindow.balls.get(i).movement.x;
            AdditionWindow.balls.get(i).tempy = AdditionWindow.balls.get(i).center.getY() + AdditionWindow.balls.get(i).movement.y;
        }
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(movement.x, 2) + Math.pow(movement.y, 2));
    }

    public void changeSpeed(double k) {
        movement.x *= k;
        movement.y *= k;
    }

    @Override
    public String toString() {
        return "X: " + Math.round(center.getX()) + ", Y: " + Math.round(center.getY());
    }

    static public void ballToBallDetect (Model b1, Model b2) {
        double whatTime = 0;
        boolean clash = false;
        double xmov1 = b1.movement.x;
        double ymov1 = b1.movement.y;
        double xmov2 = b2.movement.x;
        double ymov2 = b2.movement.y;

        double xl1 = b1.center.getX();
        double yl1 = b1.center.getY();
        double xl2 = b2.center.getX();
        double yl2 = b2.center.getY();

        //int R = b1.radius + b2.radius;
        int R = Controller.ballSize - 1;
        double a = -2 * xmov1 * xmov2 + xmov1 * xmov1 + xmov2 * xmov2;
        double b = -2 * xl1 * xmov2 - 2 * xl2 * xmov1 + 2 * xl1 * xmov1 + 2 * xl2 * xmov2;
        double c = -2 * xl1 * xl2 + xl1 * xl1 + xl2 * xl2;
        double d = -2 * ymov1 * ymov2 + ymov1 * ymov1 + ymov2 * ymov2;
        double e = -2 * yl1 * ymov2 - 2 * yl2 * ymov1 + 2 * yl1 * ymov1 + 2 * yl2 * ymov2;
        double f = -2 * yl1 * yl2 + yl1 * yl1 + yl2 * yl2;
        double g = a + d;
        double h = b + e;
        double k = c + f - R * R;

        double koren = Math.sqrt(h * h - 4 * g * k);
        double t1 = (-h + koren)/(2 * g);
        double t2 = (-h - koren)/(2 * g);

        if (t1 > 0 && t1 <= 1) {
            whatTime = t1;
            clash = true;
        }
        if (t2 > 0 && t2 <= 1) {
            if (whatTime == 0 || t2 < t1) {
                whatTime = t2;
                clash = true;
            }
        }
        if (clash) {
            ball2BallReaction(b1, b2, xl1, xl2, yl1, yl2, whatTime);
        }
    }

    static public void ball2BallReaction(Model b1, Model b2, double x1, double x2, double y1, double y2, double time) {
        // задаем переменные массы шаров
        double mass1 = b1.mass;
        double mass2 = b2.mass;
        // задаем переменные скорости
        double xVel1 = b1.movement.x;
        double xVel2 = b2.movement.x;
        double yVel1 = b1.movement.y;
        double yVel2 = b2.movement.y;
        double run = (x1 - x2);
        double rise = (y1 - y2);
        //Угол между осью х и линией действия
        double Alfa = Math.atan2(rise, run);
        double cosAlfa = Math.cos(Alfa);
        double sinAlfa = Math.sin(Alfa);
        // находим скорости вдоль линии действия
        double xVel1prime = xVel1 * cosAlfa + yVel1 * sinAlfa;
        double xVel2prime = xVel2 * cosAlfa + yVel2 * sinAlfa;
        // находим скорости перпендикулярные линии действия
        double yVel1prime = yVel1 * cosAlfa - xVel1 * sinAlfa;
        double yVel2prime = yVel2 * cosAlfa - xVel2 * sinAlfa;
        // применяем законы сохранения
        double P = (mass1 * xVel1prime + mass2 * xVel2prime);
        double V = (xVel1prime - xVel2prime);
        double v2f = (P + mass1 * V)/(mass1 + mass2);
        double v1f = v2f - xVel1prime + xVel2prime;
        xVel1prime = v1f;
        xVel2prime = v2f;
        // Проецируем обратно на оси Х и У.
        xVel1 = xVel1prime * cosAlfa - yVel1prime * sinAlfa;
        xVel2 = xVel2prime * cosAlfa - yVel2prime * sinAlfa;
        yVel1 = yVel1prime * cosAlfa + xVel1prime * sinAlfa;
        yVel2 = yVel2prime * cosAlfa + xVel2prime * sinAlfa;
        // изменяем временные позиции шаров
        b1.tempx = b1.center.getX() + b1.movement.x * time * 0.99999999999;
        b1.tempy = b1.center.getY() + b1.movement.y * time * 0.99999999999;
        b2.tempx = b2.center.getX() + b2.movement.x * time * 0.99999999999;
        b2.tempy = b2.center.getY() + b2.movement.y * time * 0.99999999999;
        // ставим новые скорости шаров
        b1.movement.x = xVel1;
        b2.movement.x = xVel2;
        b1.movement.y = yVel1;
        b2.movement.y = yVel2;
    }

    static public void detection (Model ball) {
        // Столкновение с правой стеной
        if (ball.tempx + Controller.ballSize / 2 >  Main.WIDTH + 1 - Main.MARGIN) {
            double xc = Main.WIDTH - Controller.ballSize / 2 + 1 - Main.MARGIN;
            double otn = (ball.tempx - xc) / (ball.tempx - ball.center.getX());
            double yc = ball.tempy - otn * (ball.tempy - ball.center.getY());
            ball.tempx = xc;
            ball.tempy = yc;
            ball.movement.x *= -1;
        }
        // Столкновение с левой стеной
        if (ball.tempx - Controller.ballSize / 2 < Main.MARGIN - 2 - Main.MARGIN) {
            double xc = Controller.ballSize / 2 + Main.MARGIN - 2 - Main.MARGIN;
            double otn = (ball.tempx - xc) / (ball.tempx - ball.center.getX());
            double yc = ball.tempy - otn * (ball.tempy - ball.center.getY());
            ball.tempx = xc;
            ball.tempy = yc;
            ball.movement.x *= -1;
        }
        // Столкновение с верхней стеной
        if (ball.tempy - Controller.ballSize / 2 < Main.MARGIN - 2 - Main.MARGIN) {
            double yc = Controller.ballSize / 2 + Main.MARGIN - 2 - Main.MARGIN;
            double otn = (ball.tempy - yc) / (ball.tempy - ball.center.getY());
            double xc = ball.tempx - otn * (ball.tempx - ball.center.getX());
            ball.tempy = yc;
            ball.tempx = xc;
            ball.movement.y *= -1;
        }
        // Столкновение с нижней стеной
        if ((ball.tempy + Controller.ballSize / 2 > Main.HEIGHT + 1 - Main.MARGIN)) {
            double yc = Main.HEIGHT +- Controller.ballSize / 2 + 1 - Main.MARGIN;
            double otn = (ball.tempy - yc) / (ball.tempy - ball.center.y);
            double xc = ball.tempx - otn * (ball.tempx - ball.center.x);
            ball.tempy = yc;
            ball.tempx = xc;
            ball.movement.y *= -1;
        }
    }
}
