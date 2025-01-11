package woareXengine.util;

import org.joml.Vector2f;

public class MathUtils {

    public static float dist(Vector2f a, Vector2f b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }


    public static void rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float) Math.cos(Math.toRadians(angleDeg));
        float sin = (float) Math.sin(Math.toRadians(angleDeg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += origin.x;
        yPrime += origin.y;

        vec.x = xPrime;
        vec.y = yPrime;
    }
}