package app;

/**
 * Class used to start the application after aseembling to a standalone jar
 * file,
 * this is used to separate the FXML dependencies giving no errors on launch
 */
public class AppStarter {
  public static void main(final String[] args) {
    GameApp.main(args);
  }
}
