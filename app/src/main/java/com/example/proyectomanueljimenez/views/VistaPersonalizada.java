package com.example.proyectomanueljimenez.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectomanueljimenez.clases.Figura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VistaPersonalizada extends View {

    public static final int VELOCIDAD_MAXIMA = 10;
    private final List<Figura> figurasMoviles = new ArrayList<>();

    private int ancho, alto;

    private final ExecutorService executorService = Executors.newFixedThreadPool(12);

    private final Random random = new Random();

    public VistaPersonalizada(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ancho = w;
        alto = h;
        cargarFiguras();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(0,0, 0, 0);

        // Dibujamos cada figura
        for (Figura figura: figurasMoviles) {
            figura.actualizarFigura(ancho, alto);
            figura.dibujar(canvas);
        }

        postInvalidateDelayed(10);
    }

    private void cargarFiguras() {

        // LIMPIAMOS LAS FIGURAS
        figurasMoviles.clear();

        // PINTAMOS 3 CIRCULOS
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                Paint pincelNuevo = new Paint();

                // ESTILO DEL PINCEL
                pincelNuevo.setStyle(Paint.Style.FILL);
                pincelNuevo.setStrokeWidth(4);

                // COLOR DEL PINCEL
                pincelNuevo.setARGB(random.nextInt(250) + 30, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                // CREAMOS EL PATH
                Path pathCiculo = new Path();

                // TAMAÑO CIRCULO ALEATORIO
                int anchoFigura = random.nextInt(120) + 30;
                int altoFigura = random.nextInt(120) + 30;

                // Posición inicial aleatoria
                int posX = random.nextInt(ancho - anchoFigura);
                int posY = random.nextInt(alto - altoFigura);

                // AÑADIMOS EL CIRCULO
                pathCiculo.addCircle(40, 40, 40, Path.Direction.CCW);

                int velocidadX = random.nextInt(VELOCIDAD_MAXIMA) - 5;
                int velocidadY = random.nextInt(VELOCIDAD_MAXIMA) - 5;

                // EVITAR EL 0 PARA QUE NO SE QUEDEN PARADOS
                if (velocidadX == 0) velocidadX = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);
                if (velocidadY == 0) velocidadY = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);

                Figura figura = new Figura(pathCiculo, pincelNuevo, posX, posY, velocidadX, velocidadY, anchoFigura, altoFigura);
                figurasMoviles.add(figura);
            });
        }

        // PINTAMOS 3 CUADRADOS
        for (int i = 0; i < 3; i++) {

            executorService.execute(() -> {

                Paint pincelNuevo = new Paint();

                pincelNuevo.setStyle(Paint.Style.FILL);
                pincelNuevo.setStrokeWidth(4);

                pincelNuevo.setARGB(random.nextInt(250) + 30, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                Path pathCuadrado = new Path();

                // TAMAÑO Y POSICION ALEATORIO
                int lado = random.nextInt(120) + 30;
                int posX = random.nextInt(ancho - lado);
                int posY = random.nextInt(alto - lado);

                pathCuadrado.addRect(40, 40, 40 + lado, 40 + lado, Path.Direction.CCW);

                int velocidadX = random.nextInt(VELOCIDAD_MAXIMA) - 5;
                int velocidadY = random.nextInt(VELOCIDAD_MAXIMA) - 5;

                // EVITAR EL 0 PARA QUE NO SE QUEDEN PARADOS
                if (velocidadX == 0) velocidadX = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);
                if (velocidadY == 0) velocidadY = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);

                Figura figura = new Figura(pathCuadrado, pincelNuevo, posX, posY, velocidadX, velocidadY, lado, lado);

                figurasMoviles.add(figura);
            });
        }

        // PINTAMOS 3 TRIANGULOS
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                Paint pincelNuevo = new Paint();

                pincelNuevo.setStyle(Paint.Style.FILL);
                pincelNuevo.setStrokeWidth(4);

                pincelNuevo.setARGB(random.nextInt(250) + 30, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                int lado = random.nextInt(120) + 30;;

                int posX = random.nextInt(ancho - lado);
                int posY = random.nextInt(alto - lado);

                int velocidadX = random.nextInt(VELOCIDAD_MAXIMA) - 5;
                int velocidadY = random.nextInt(VELOCIDAD_MAXIMA) - 5;

                // EVITAR EL 0 PARA QUE NO SE QUEDEN PARADOS
                if (velocidadX == 0) velocidadX = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);
                if (velocidadY == 0) velocidadY = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);

                Figura figura = new Figura(creaTriangulo(lado/2), pincelNuevo, posX, posY, velocidadX, velocidadY, lado, lado);

                figurasMoviles.add(figura);
            });
        }

        // PINTAMOS 3 ESTRELLAS
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                Paint pincelNuevo = new Paint();

                pincelNuevo.setStyle(Paint.Style.FILL);
                pincelNuevo.setStrokeWidth(4);

                pincelNuevo.setARGB(random.nextInt(250) + 30, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                // TAMAÑO ALEATORIO
                int lado = random.nextInt(120) + 30;

                int posX = random.nextInt(ancho - lado);
                int posY = random.nextInt(alto - lado);

                int velocidadX = random.nextInt(VELOCIDAD_MAXIMA) - 5;
                int velocidadY = random.nextInt(VELOCIDAD_MAXIMA) - 5;

                // EVITAR EL 0 PARA QUE NO SE QUEDEN PARADOS
                if (velocidadX == 0) velocidadX = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);
                if (velocidadY == 0) velocidadY = (random.nextBoolean() ? 1 : -1) * (random.nextInt(5) + 1);

                Figura figura = new Figura(creaEstrella(lado/2), pincelNuevo, posX, posY, velocidadX, velocidadY, lado, lado);

                figurasMoviles.add(figura);
            });
        }
    }

    public Path creaTriangulo (int radio){
        Point centro = new Point(radio, radio);

        //Generación de 3 puntos
        Point[] triangleP = new Point[3];
        for(int i = 0, angulo = (int)(Math.random()*120); i < triangleP.length; i++, angulo += 120){
            triangleP[i] = polar2rect(radio, angulo);
        }

        //Creación del path
        Path triangle = new Path();
        triangle.moveTo(centro.x + triangleP[0].x, centro.y + triangleP[0].y);
        triangle.lineTo(centro.x + triangleP[1].x, centro.y + triangleP[1].y);
        triangle.lineTo(centro.x + triangleP[2].x, centro.y + triangleP[2].y);
        triangle.lineTo(centro.x + triangleP[0].x, centro.y + triangleP[0].y);

        return  triangle;
    }

    public Path creaEstrella (int radio){
        Point centro = new Point(radio, radio);

        //Creamos 10 puntos para trazar la estrella
        Point[] starP = new Point[10];
        //Creamos los puntos utilizando coordenadas polares
        //En este bucle for tenemos dos variables incrementales, la del índice del punto 'i' y la del ángulo
        //que parte de un valor aleatorio entre 0~180 y se irá incrementando en cada iteración 360/(nº de puntos)
        for (int i = 0, angulo = (int)(Math.random()*180); i < starP.length; i++, angulo += 360/starP.length) {
            if (i % 2 == 0) //Los puntos pares tendrán un módulo 'radio' (puntas de la estrella)
                starP[i] = polar2rect(radio, angulo);
            else //Los puntos impares tendrán un módulo 'radio/2' (puntos interiores de la estrella)
                starP[i] = polar2rect((double) radio / 2, angulo);
        }
        //Creamos el Path: desde el punto 0 vamos creando líneas hasta cerrar la forma
        Path star = new Path();
        star.moveTo(starP[0].x + centro.x, starP[0].y + centro.y);
        for(int i=1; i<starP.length; i++)
            star.lineTo(starP[i].x + centro.x, starP[i].y + centro.y);
        star.lineTo(starP[0].x + centro.x, starP[0].y + centro.y); //Última línea para cerrar la estrella
        return star;
    }

    public Point polar2rect (double radio, int grados){
        double rad = grados * Math.PI / 180; //para pasar el ángulo de grados a radianes
        return new Point(
                (int) Math.round(radio * Math.cos(rad)), //coordenada x
                (int) Math.round(radio * Math.sin(rad))  //coordenada y
        );
    }
}