package com.example.proyectomanueljimenez.clases;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Figura {

    private Path path;
    private Paint paint;

    // POSICION
    private int centroX, centroY;

    // VELOCIDAD
    private int velocidadX, velocidadY;

    // TAMAÑO
    private int ancho, alto;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Figura(Path path, Paint paint, int centroX, int centroY, int velocidadX, int velocidadY, int ancho, int alto) {
        this.path = path;
        this.paint = paint;
        this.centroX = centroX;
        this.centroY = centroY;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void actualizarFigura(int limiteX, int limiteY) {

            centroX += velocidadX;
            centroY += velocidadY;

            if (centroX < 0 || centroX + ancho > limiteX) {
                velocidadX *= -1;
                centroX = Math.max(0, Math.min(centroX, limiteX - ancho));
            }

            if (centroY < 0 || centroY + alto > limiteY) {
                velocidadY *= -1;
                centroY = Math.max(0, Math.min(centroY, limiteY - alto));
            }
    }

    // Dibuja la figura trasladando el canvas a su posición actual
    public void dibujar(Canvas canvas) {
        canvas.save();
        canvas.translate(centroX, centroY);
        canvas.drawPath(path, paint);
        canvas.restore();
    }
}
