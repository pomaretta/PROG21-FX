package simple.PongSimple;

/*

    Project     PROG21-FX
    Package     simple.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-12

    DESCRIPTION
    Juego inspirado en el Pong.
    
*/

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Carlos Pomares
 */

public class Pong_carlos_pomares extends Application {

    // WINDOW PROPERTIES
    private int ALTO;
    private int ANCHO;

    public final String TITULO = "Pong", VERSION = "1.0";

    // STAGE
    private Stage stage;

    // MANAGER
    private SceneManager manager;

    // SCENES
    public GameScene gameScene;

    // GAME PROPERTIES
    private int RONDAS_PARA_GANAR;

    @Override
    public void start(Stage stage) throws Exception {

        // ASSIGN WIDTH AND HEIGHT
        this.ANCHO = 800;
        this.ALTO = 600;
        this.RONDAS_PARA_GANAR = 15;

        // INSTANCE AVAILABLE SCENES
        this.gameScene = new GameScene(this.ANCHO,this.ALTO,this);
        this.gameScene.setPuntosMaximos(this.RONDAS_PARA_GANAR);

        // SCENE MANAGER
        this.stage = stage;
        this.manager = new SceneManager(this);
        this.manager.registerScene(gameScene);

        // LOAD GAME SCENE
        this.manager.changeScene(gameScene);

        // STAGE
        this.stage.setScene(this.manager.getScene());
        this.stage.setTitle(String.format(
                "%s - V%s", this.TITULO, this.VERSION
        ));
        this.stage.setResizable(false);
        this.stage.show();

    }

    public int getHeight() {
        return ALTO;
    }

    public int getWidth() {
        return ANCHO;
    }

    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

/*
*
*   SCENE MANAGER
*
* */

class SceneManager {

    private final Scene scene;
    private final Pong_carlos_pomares toManage;
    private PongScene currentScene;
    private final ArrayList<PongScene> scenes;

    /**
     *
     * Constructor del manager de la escena al cual se le asigna
     * la clase Pong a la que va a manejar.
     *
     * @param bind la clase Pong la cual va a gestionar.
     */
    public SceneManager(Pong_carlos_pomares bind){
        this.toManage = bind;
        this.scene = new Scene(new Pane(),toManage.getWidth(),toManage.getHeight());
        this.scenes = new ArrayList<>();
    }

    /**
     * Se obtendra la escena actual a través del método
     * getCurrentScene() con el cual utilizaremos el método
     * run() y obtendremos el panel de la escena con el método
     * getScene() y será asignado al flujo de la aplicación.
     */
    private void loadScene() {
        assert getCurrentScene() != null;
        this.scene.setRoot(getCurrentScene().getScene());
        getCurrentScene().run();
    }

    /**
     * Se sustituirá el panel actual por uno instanciado sin contenido,
     * de esta manera podemos asegurar que en caso de que no se carge el nuevo
     * contenido o no se borre el anterior no va a aparecer en escena.
     */
    private void unloadScene(){
        this.scene.setRoot(new Pane());
    }

    /**
     *
     * Permite borrar una escena registrada.
     *
     * @param scene la escena que se desea eliminar.
     * @throws NoSuchElementException si el manager no contiene la escena.
     */
    public void removeScene(PongScene scene)
        throws NoSuchElementException {
        assert scene != null;
        if(!this.scenes.contains(scene)){
            throw new NoSuchElementException();
        }
        unloadScene();
        this.scenes.remove(scene);
    }

    /**
     *
     * Permite registrar una escena al manager, para posteriormente
     * poder ser utilizada.
     *
     * @param scene la escena a registrar.
     * @throws Exception si el manager ya contiene la escena.
     */
    public void registerScene(PongScene scene)
        throws Exception {
        assert scene != null;
        if(this.scenes.contains(scene)){
            throw new Exception();
        }
        this.scenes.add(scene);
        scene.bindManager(this);
    }

    /**
     *
     * Devuelve la escena que actualmente esta asignada a la propiedad currentScene.
     *
     * @return la propiedad currentScene
     * @throws NullPointerException si la escena actual es null.
     */
    public PongScene getCurrentScene()
        throws NullPointerException {
        if(this.currentScene == null){
            throw new NullPointerException();
        }
        return this.currentScene;
    }

    /**
     *
     * Permite cambiar la escena, mediante el uso de otros métodos del manager,
     * permiten descargar la escena actual y cargar la nueva escena a través de este
     * método.
     *
     * @param scene la escena a la que se quiere cambiar.
     * @throws NoSuchElementException si la escena a la que se quiere cambiar
     * no está registrada en el manager.
     */
    public void changeScene(PongScene scene)
        throws NoSuchElementException {
        assert scene != null;
        if(!this.scenes.contains(scene)){
            throw new NoSuchElementException();
        }
        this.currentScene = scene;
        loadScene();
    }

    /**
     * Devuelve la escena de tipo Scene.
     *
     * @return la escena del manager para poder asignarla al Stage que contiene la clase Pong.
     */
    public Scene getScene(){
        return this.scene;
    }

}

interface Component {
    /**
     * Colocar las inicializaciones de componentes
     * de una escena.
     */
    public void generateComponents();

    /**
     * Colocar las sentencias donde se agregan los
     * diferentes componentes al panel raíz.
     */
    public void loadComponents();

    /**
     * Vaciara el panel de la escena donde se llame
     * al método, de forma que permite la llamada
     * del método generateComponents() de nuevo.
     */
    public void unloadComponents();

    /**
     * Colocar las sentencias donde se realizen acciones
     * relacionadas con la colocación de los componentes
     * dentro de la escena.
     */
    public void relocateComponents();

    /**
     * Método que debe realizar las siguientes tareas,
     * descargar los componentes, para confirmar que en
     * caso de existir destruirlos, generar los componentes,
     * añadirlos a través del método loadComponents()
     * y colocarlos en la escena. Es el método que se utilizará
     * para cambiar entre escenas.
     */
    public void run();

    /**
     *
     * Permite asignar el manager a las diferentes escenas, para que
     * a través de este puedan realizar acciones de redirección del
     * flujo de contenido dentro del programa. Mediante este método
     * podemos "encadenar" las escenas.
     *
     * @param manager el manager el cual utilizará la escena.
     * @throws Exception si le pasan un objeto de tipo null.
     */
    public void bindManager(SceneManager manager) throws Exception;

    /**
     *
     * Permite obtener el panel raíz de cada escena para que el manager
     * pueda realizar las tareas de obtención de los componentes y poder
     * encadenar las escenas.
     *
     * @return el panel raíz de la escena.
     */
    public Parent getScene();
}

abstract class PongScene implements Component {

    private final int WIDTH;
    private final int HEIGHT;
    private final Pane ROOT;
    protected SceneManager manager;
    protected Pong_carlos_pomares parent;

    public PongScene(int width, int height, Pong_carlos_pomares parent){
        this.WIDTH = width;
        this.HEIGHT = height;
        this.parent = parent;
        this.ROOT = new Pane();
    }

    @Override
    public void unloadComponents() {
        getRoot().getChildren().clear();
    }

    @Override
    public void bindManager(SceneManager manager)
            throws Exception {
        assert manager != null;
        if(this.manager != null){
            throw new Exception();
        }
        this.manager = manager;
    }

    @Override
    public void run() {
        unloadComponents();
        generateComponents();
        loadComponents();
        relocateComponents();
    }

    /**
     * Método que permitira dentro de la escena poder obtener
     * su raíz para realizar diferentes acciones.
     *
     * @return el panel raíz de la escena.
     */
    protected Pane getRoot(){
        return this.ROOT;
    }

    @Override
    public Parent getScene() {
        return this.getRoot();
    }

    /**
     * Método que permitira que dentro de la escena los componentes
     * puedan obtener el ancho de la escena a través de un método
     * definido y estándar dentro de nuestro sistema.
     *
     * @return el ancho de la escena.
     */
    public int getWidth() {
        return this.WIDTH;
    }

    /**
     * Método que permitirá que dentro de la escena los componentes
     * puedan obtener el alto de la escena a través de un método
     * definido y estándar dentro de nuestro sistema.
     *
     * @return el alto de la escena.
     */
    public int getHeight() {
        return this.HEIGHT;
    }

}

/*
 *
 *   GAME
 *
 * */

class GameScene extends PongScene {

    // OBJECTS
    private final Player[] jugadores;
    private final Ball bola;
    private final Timeline bucle;

    // PROPERTIES
    final private int RADIO = 10;
    final private double[] P1_POS;
    final private double[] P2_POS;

    private int puntosMaximos;
    private boolean ultimoGanador;
    private boolean jugando = true;

    // Components
    private Font fuentePuntos = Font.font("Monospace",FontWeight.BOLD,72);
    private Label puntoIzquierdo;
    private Label puntoDerecho;
    private Line lineaCentral;
    private Label barraEspaciadora;
    private Label finalDeJuego;

    public GameScene(int width, int height, Pong_carlos_pomares parent){
        super(width, height, parent);

        this.bola = new Ball(this.RADIO,Color.WHITE);

        this.jugadores = new Player[]{
                new Player(10,60,Color.WHITE,"Player 0"),
                new Player(10,60,Color.WHITE, "Player 1")
        };

        this.bucle = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                manejadorDeRonda();
            }
        }));
        this.bucle.setCycleCount(Timeline.INDEFINITE);
        
        this.P1_POS = new double[]{
                90,
                (((double) this.getHeight() / 2) - this.jugadores[0].getSprite().getHeight())
        };

        this.P2_POS = new double[]{
                this.getWidth() - 100,
                (((double) this.getHeight() / 2) - this.jugadores[0].getSprite().getHeight())
        };

        this.ultimoGanador = !(Math.random() * 1 > 0.5);

    }

    @Override
    public void generateComponents() {

        this.lineaCentral = new Line();
        this.lineaCentral.setEndY(this.getHeight() - 5);
        this.lineaCentral.setStrokeWidth(5);
        this.lineaCentral.getStrokeDashArray().addAll(2d, 15d);
        this.lineaCentral.setStroke(Color.WHITE);

        this.puntoIzquierdo = new Label("0");
        this.puntoIzquierdo.setTextFill(Color.WHITE);
        this.puntoIzquierdo.setFont(this.fuentePuntos);

        this.puntoDerecho = new Label("0");
        this.puntoDerecho.setTextFill(Color.WHITE);
        this.puntoDerecho.setFont(this.fuentePuntos);

        this.barraEspaciadora = new Label("Press space to start.");
        this.barraEspaciadora.setFont(Font.font("Arial",FontWeight.BOLD,56));
        this.barraEspaciadora.setTextFill(Color.WHITE);
        this.barraEspaciadora.setOpacity(.8);

        this.finalDeJuego = new Label("Game over");
        this.finalDeJuego.setFont(Font.font("Arial",FontWeight.BOLD,52));
        this.finalDeJuego.setTextFill(Color.WHITE);
        this.finalDeJuego.setOpacity(0.0);

        this.getRoot().setStyle("-fx-background-color: black");

    }

    @Override
    public void loadComponents() {

        this.getRoot().getChildren().addAll(
                this.jugadores[0].getSprite()
                ,this.jugadores[1].getSprite()
                ,this.bola.getSprite()
        );

        this.getRoot().getChildren().addAll(
                this.puntoIzquierdo
                ,this.puntoDerecho
                ,this.lineaCentral);

        this.getRoot().getChildren().addAll(
                this.barraEspaciadora,
                this.finalDeJuego
        );

    }

    @Override
    public void relocateComponents() {

        this.jugadores[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.jugadores[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);

        this.bola.colocarEnMedio(this.getRoot());
        this.bola.colocarAleatorio(this.getHeight(),50);


        this.puntoIzquierdo.relocate(
                (double) this.getWidth() / 2 - 120,
                50
        );

        this.puntoDerecho.relocate(
                (double) this.getWidth() / 2 + 75,
                50
        );


        this.lineaCentral.relocate(
                (double) this.getWidth() / 2 - 2.5,
                0
        );
        this.lineaCentral.toBack();


        this.barraEspaciadora.relocate(
                (double) this.getWidth() / 2 - 280,
                400
        );
        this.barraEspaciadora.toFront();


        this.finalDeJuego.relocate(
                ((double) this.getWidth() / 2) - 130
                ,280
        );
        this.finalDeJuego.toFront();

    }

    @Override
    public void run() {
        super.run();

        this.manager.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                this.barraEspaciadora.setVisible(false);
                inicioRonda();
            }
        });

        this.jugando = true;
        this.bola.getSprite().setVisible(true);
        this.jugadores[0].reiniciarPuntos();
        this.jugadores[1].reiniciarPuntos();

    }

    /**
     *
     * Realiza funciones de generación de nuevas propiedades e reproducción
     * del bucle así como establecer ciertos estados a diferentes componentes.
     * También establece las propiedades de eventos de teclado.
     *
     */
    private void inicioRonda(){

        this.bola.generarAngulo(this);

        if(this.jugadores[0].getPuntos() == puntosMaximos || this.jugadores[1].getPuntos() == puntosMaximos) {
            this.jugando = false;
        }

        this.puntoIzquierdo.setText(String.format("%d",this.jugadores[0].getPuntos()));
        this.puntoDerecho.setText(String.format("%d",this.jugadores[1].getPuntos()));

        // https://stackoverflow.com/questions/52580865/javafx-multiple-keylisteners-at-once
        final List<KeyCode> acceptedCodes = Arrays.asList(KeyCode.S, KeyCode.W, KeyCode.UP, KeyCode.DOWN);
        final Set<KeyCode> codes = new HashSet<>();
        manager.getScene().setOnKeyReleased(e -> {
            codes.clear();
        });
        manager.getScene().setOnKeyPressed(e -> {
            if (acceptedCodes.contains(e.getCode())) {
                codes.add(e.getCode());
                if (codes.contains(KeyCode.W) && this.jugadores[0].limiteSuperior(this.getRoot())) {
                    this.jugadores[0].moverArriba();
                } else if (codes.contains(KeyCode.S) && this.jugadores[0].limiteInferior(this.getRoot())) {
                    this.jugadores[0].moverAbajo();
                }
                if (codes.contains(KeyCode.UP) && this.jugadores[1].limiteSuperior(this.getRoot())) {
                    this.jugadores[1].moverArriba();
                } else if (codes.contains(KeyCode.DOWN) && this.jugadores[1].limiteInferior(this.getRoot())) {
                    this.jugadores[1].moverAbajo();
                }
            }
        });

        if(this.jugando) {
            bucle.play();
        } else {
            finalDeJuego();
        }

    }

    /**
     * Inicia una serie de intrucciones las cuales permiten reiniciar ciertas partes
     * de los estados de los diferentes componentes de la escena.
     */
    private void reinicioDeRonda(){
        this.bola.reiniciarPropiedades();
        relocateComponents();
        inicioRonda();
    }

    /**
     * Establece una serie de comprobaciones que serán utilizadas n veces por el bucle.
     * Destacamos la comprobación de las colisiones así como la detección de los limites
     * para añadir los puntos y establecer las nuevas condiciones.
     */
    private void manejadorDeRonda(){

        if(this.bola.isLimiteIzquierdo()) {
            this.jugadores[1].sumarPunto();
            this.ultimoGanador = false;
            reinicioDeRonda();
        }
        if(this.bola.isLimiteDerecho()) {
            this.jugadores[0].sumarPunto();
            this.ultimoGanador = true;
            reinicioDeRonda();
        }

        this.jugadores[0].detectarColision(this.bola);
        this.jugadores[1].detectarColision(this.bola);
        this.bola.movimiento(this.getRoot());

    }

    /**
     * Para el bucle, y inicializa una serie de instrucciones que permiten mostrar por pantalla
     * el texto "Game over" así como aplicarle una animación, posteriormente cuando esta termine
     * se reiniciará el juego y volverá a empezar de manera inicial.
     */
    private void finalDeJuego() {

        this.bucle.stop();
        this.bola.getSprite().setVisible(false);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800),this.finalDeJuego);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000),this.finalDeJuego);

        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        translateTransition.setByY(-30);
        translateTransition.setCycleCount(3);
        translateTransition.setAutoReverse(true);

        translateTransition.play();
        fadeTransition.play();

        translateTransition.setOnFinished(e -> {
            this.manager.changeScene(this.parent.gameScene);
        });

    }

    /**
     * Permite establecer el puntaje máximo del juego.
     *
     * @param puntosMaximos los puntos que se desea aplicar.
     */
    public void setPuntosMaximos(int puntosMaximos){
        assert puntosMaximos > 0;
        this.puntosMaximos = puntosMaximos;
    }

    /**
     * Obtención de los puntos máximos del juego.
     *
     * @return los puntos máximos actuales.
     */
    public int getPuntosMaximos(){
        return this.puntosMaximos;
    }

    /**
     * Obtención del último ganador de la ronda.
     * Devuelve true si es el jugador 0 y false
     * si es el 1.
     *
     * @return el ganador de la anterior ronda.
     */
    public boolean getLastWinner(){
        return this.ultimoGanador;
    }

}

class Player {

    private double deltaY = 10;
    private double velocidad = 1.5;

    private String nombre;
    private Rectangle sprite;

    private int puntos;
    private int contadorDeColisiones = 0;

    /**
     *
     * Constructor que establece una propiedades iniciales como
     * la instancia del rectángulo y su color. El nombre por defecto
     * será de "Player".
     *
     * @param ancho del rectángulo.
     * @param alto del rectángulo.
     * @param color del rectángulo.
     */
    public Player(int ancho,int alto,Color color){
        this.sprite = new Rectangle(ancho,alto,color);
        this.nombre = "Player";
    }

    /**
     *
     * Constructor que establece una propiedades iniciales como
     * la instancia del rectángulo y su color. El nombre puede
     * ser establecido.
     *
     * @param ancho del rectángulo.
     * @param alto del rectángulo.
     * @param color del rectángulo.
     * @param nombre del jugador.
     */
    public Player(int ancho,int alto,Color color,String nombre){
        this.sprite = new Rectangle(ancho,alto,color);
        this.nombre = nombre;
    }

    /**
     * Permite incrementar en 1 punto el puntaje actual del jugador.
     */
    public void sumarPunto(){
        this.puntos++;
    }

    /**
     * Permite reiniciar el puntaje del jugador a valores por defecto, 0.
     */
    public void reiniciarPuntos(){
        this.puntos = 0;
    }

    /**
     * Permite mover al jugador n píxeles en sentido ascendente de forma visual.
     */
    public void moverArriba(){
        this.sprite.setLayoutY(this.sprite.getLayoutY() - this.deltaY * velocidad);
    }

    /**
     * Permite mover al jugador n píxeles en sentido descendente de forma visual.
     */
    public void moverAbajo(){
        this.sprite.setLayoutY(this.sprite.getLayoutY() + this.deltaY * velocidad);
    }

    /**
     * Permite reiniciar el contador de colisiones a 0.
     */
    public void reiniciarContadorDeColisiones(){
        this.contadorDeColisiones = 0;
    }

    /**
     *
     * Método que comprueba el estado de posición del jugador sobre el de la bola,
     * por lo cual si se detecta que se están interpolando entre ellos realiza funciones
     * sobre la bola, así como incrementar el contador de colisiones para realizar funciones
     * de incremento de velocidad a n colisiones.
     *
     * @param bola la bola que comprueba su posición relativa a la del jugador.
     * @return si se detecta la colisión.
     */
    public boolean detectarColision(Ball bola){
        if(getSprite().getBoundsInParent().intersects(bola.getSprite().getBoundsInParent())){
            this.contadorDeColisiones++;

            bola.incrementarColision();
            bola.modificarX();

            if(contadorDeColisiones > 1 && getSprite().getBoundsInParent().intersects(bola.getSprite().getBoundsInParent())){
                bola.getSprite().setLayoutX(
                        (bola.getDeltaX() < 0)
                                ? bola.getSprite().getLayoutX() + 5 + (bola.getSprite().getRadius() * 2)
                                : bola.getSprite().getLayoutX() -  5 - (bola.getSprite().getRadius() * 2)
                );
            }

            if(bola.getColisionesConsecutivas() >= 5){
                bola.acelerar();
                bola.reiniciarColisiones();
            }
            return true;
        } else if(!getSprite().getBoundsInParent().intersects(bola.getSprite().getBoundsInParent())){
            reiniciarContadorDeColisiones();
        }
        return false;
    }

    /**
     *
     * Devuelve true si detecta que el jugador esta en el límite superior de la ventana.
     *
     * @param canvas el panel sobre el que medir las posiciones.
     * @return si esta posicionado sobre el límite.
     */
    public boolean limiteSuperior(Parent canvas){
        final Bounds limits = canvas.getBoundsInLocal();
        final boolean upperLimit = getSprite().getLayoutY() <= (limits.getMinY());
        return !upperLimit;
    }

    /**
     *
     * Devuelve true si detecta que el jugador esta en el límite inferior de la ventana.
     *
     * @param canvas el panel sobre el que medir las posiciones.
     * @return si esta posicionado sobre el límite.
     */
    public boolean limiteInferior(Parent canvas){
        final Bounds limits = canvas.getBoundsInLocal();
        final boolean lowerLimit = getSprite().getLayoutY() >= (limits.getMaxY() - getSprite().getHeight());
        return !lowerLimit;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setColor(Color color){
        this.sprite.setFill(color);
    }

    public double getDeltaY() {
        return deltaY;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNombre(){
        return this.nombre;
    }

    public Rectangle getSprite() {
        return sprite;
    }

}

class Ball {

    private int colisionesConsecutivas = 0;
    private double velocidadPorDefecto = 1, maximaVelocidad = 5;
    private double deltaX, deltaY, velocidad;
    private boolean limiteIzquierdo = false, limiteDerecho = false;
    private Circle sprite;

    /**
     *
     * Constructor que inicializa el Circle de la bola,
     * con su radio y color.
     *
     * @param radio de la bola.
     * @param color de la bola.
     */
    public Ball(int radio, Color color){
        this.sprite = new Circle(radio,color);
    }

    /**
     * Establece la polaridad contraria de la propiedad deltaY.
     */
    public void modificarY(){
        this.deltaY *= -1;
    }

    /**
     * Establece la polaridad contraria de la propiedad deltaX.
     */
    public void modificarX(){
        this.deltaX *= -1;
    }

    /**
     * Incrementa la velocidad en 1, sin superar el límite máximo.
     */
    public void acelerar(){
        if(this.velocidad < this.maximaVelocidad)
            this.velocidad++;
    }

    /**
     * Genera un angulo teniendo en cuenta el jugador ganador, y
     * a través de la aleatoriedad establece a que dirección se dirigirá
     * la bola.
     *
     * @param gameScene la escena para poder obtener el último ganador.
     */
    public void generarAngulo(GameScene gameScene){
        double angle = Math.toRadians(
                (gameScene.getLastWinner())
                        ? (Math.random() * 5 + 1 > 2.5)
                        ? 45 + ((int) (Math.random() * 15) + 1)
                        : -45 - ((int) (Math.random() * 15) + 1)
                        : (Math.random() * 5 + 1 > 2.5)
                        ? 135 + ((int) (Math.random() * 15) + 1)
                        : -135 - ((int) (Math.random() * 15) + 1)
        );
        this.velocidad = this.velocidadPorDefecto;
        this.deltaX = this.velocidad * Math.cos(angle);
        this.deltaY = this.velocidad * Math.sin(angle);
    }

    /**
     * Permite colocar la bola en el medio relativamente al ancho y alto de la escena.
     *
     * @param canvas el panel raíz sobre el cual obtener medidas.
     */
    public void colocarEnMedio(Parent canvas){
        Bounds limits = canvas.getBoundsInLocal();
        this.sprite.relocate(
                ((limits.getMaxX() - limits.getMaxX() / 2) - this.sprite.getRadius()),
                ((limits.getMaxY() - (limits.getMaxY() / 2)) - this.sprite.getRadius())
        );
    }

    /**
     * Permite generar una altura aleatoria entre dos valores, teniendo en cuenta
     * la altura máxima a la cual se puede generar y también el limite de espacio
     * para no posicionar la bola en el máximo o mínimo.
     *
     * @param alturaMaxima la altura máxima sobre el cual generar la cual posicionar la bola.
     * @param espacioEntreLimites el limite sobre el cual añadir para no poder tocar el mínimo.
     */
    public void colocarAleatorio(double alturaMaxima, double espacioEntreLimites){
        double randomY = Math.random() * (alturaMaxima - espacioEntreLimites);
        if(alturaMaxima < espacioEntreLimites){
            this.sprite.relocate(this.sprite.getLayoutX(),randomY + espacioEntreLimites);
        } else {
            this.sprite.relocate(this.sprite.getLayoutX(),randomY);
        }
    }

    /**
     * Permite volver ciertas propiedades de la bola a sus valores por defecto.
     */
    public void reiniciarPropiedades(){
        this.limiteIzquierdo = false;
        this.limiteDerecho = false;
        this.colisionesConsecutivas = 0;
    }

    /**
     * Permite incrementar el contador de colisiones en 1.
     */
    public void incrementarColision(){
        this.colisionesConsecutivas++;
    }

    /**
     * Permite reiniciar el contador de colisiones a 0.
     */
    public void reiniciarColisiones(){
        this.colisionesConsecutivas = 0;
    }

    /**
     * Establece las nuevas posiciones de la bola, de forma que también comprueba las
     * colisiones con los límites de la ventana, y sobre sus resultados actuará de una forma o
     * otra estableciendo y modificando propiedades.
     *
     * @param canvas panel raíz sobre el cual obtener los límites.
     */
    public void movimiento(Parent canvas){

        this.sprite.setLayoutX(this.sprite.getLayoutX() + deltaX * velocidad);
        this.sprite.setLayoutY(this.sprite.getLayoutY() + deltaY * velocidad);

        final Bounds limits = canvas.getBoundsInLocal();
        final boolean leftLimit = this.sprite.getLayoutX() <= (limits.getMinX() + this.sprite.getRadius());
        final boolean rightLimit = this.sprite.getLayoutX() >= (limits.getMaxX() - this.sprite.getRadius());
        final boolean upperLimit = this.sprite.getLayoutY() <= (limits.getMinY() + this.sprite.getRadius());
        final boolean lowerLimit = this.sprite.getLayoutY() >= (limits.getMaxY() - this.sprite.getRadius());

        if(leftLimit)
            this.limiteIzquierdo = true;

        if(rightLimit)
            this.limiteDerecho = true;

        if(upperLimit || lowerLimit)
            modificarY();

    }

    /**
     * Permite establecer la velocidad máxima de la pelota.
     *
     * @param maximaVelocidad la velocidad maxíma que se desea alcanzar.
     */
    public void setMaximaVelocidad(double maximaVelocidad){
        this.maximaVelocidad = maximaVelocidad;
    }

    /**
     * Establece la velocidad por defecto de la bola.
     *
     * @param velocidadPorDefecto la velocidad por defecto.
     */
    public void setVelocidadPorDefecto(double velocidadPorDefecto){
        this.velocidadPorDefecto = velocidadPorDefecto;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getVelocidad() {
        return velocidadPorDefecto;
    }

    public double getMaximaVelocidad(){
        return this.maximaVelocidad;
    }

    public Circle getSprite() {
        return sprite;
    }

    public boolean isLimiteIzquierdo() {
        return limiteIzquierdo;
    }

    public boolean isLimiteDerecho() {
        return limiteDerecho;
    }

    public int getColisionesConsecutivas(){
        return this.colisionesConsecutivas;
    }

}