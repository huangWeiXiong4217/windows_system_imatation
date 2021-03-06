package apps.browserApp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import utils.DrawUtil;

public class BrowserAppController
{
    @FXML
    private AnchorPane topMainPane;
    @FXML
    private BorderPane titlebar;
    @FXML
    private BorderPane browserWindow;

    @FXML
    private Button backButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button homeButton;

    @FXML
    private TextField adressBar;

    @FXML
    private Button jumpButton;


    @FXML
    private WebView webview;

    boolean haveResize=true;

    boolean isMax=false;

    @FXML
    void resizeStage(MouseEvent event)
    {
        if(!haveResize)
        {
            return;
        }
        haveResize=false;

        if(!isMax)
        {
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.setX(0);
            stage.setY(0);
            adaptWindow();
        }
        else {


            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
            stage.setX(0);
            stage.setY(0);
            adaptWindow();

        }
        isMax=!isMax;
        haveResize=true;
    }

    @FXML
    void closeStage(MouseEvent event)
    {
        stage.close();
    }

    @FXML
    void minimizeStage(MouseEvent event)
    {
        stage.setIconified(true);

    }


    @FXML
    void dragBar(MouseEvent event) {
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
        stage.setOpacity(0.8f);
    }

    @FXML
    void dragBarDone(DragEvent event) {
        stage.setOpacity(1.0f);
    }

    @FXML
    void pressBar(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void releaseBar(MouseEvent event) {
        stage.setOpacity(1.0f);
    }




    double xOffset = 0, yOffset = 0;
    Stage stage;
    Scene scene = null;

    public void init(Scene scene, Stage stage)
    {

        this.stage=stage;
        this.scene = scene;

        // webview ??????????????? ??????????????? ???????????????url????????????????????????
        webview.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
        {


            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue)
            {
                adressBar.setText(webview.getEngine().getLocation());

            }
        });

        //????????????
        home();
        // ???????????????
        DrawUtil drawUtil=new DrawUtil();
        drawUtil.addDrawFunc(stage, topMainPane);
        //????????????
        // ??????????????????????????????
        stage.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                // ??????GUI??????
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adaptWindow();
                    }
                });
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                // ??????GUI??????
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adaptWindow();
                    }
                });
            }
        });
        adaptWindow();
    }
    //????????????
    public void adaptWindow()
    {
        double sw=stage.getWidth();
        double sh=stage.getHeight();
        double w = 50;
//        double h = scene.getWidth() / 30;

        backButton.setPrefWidth(w);
        backButton.setMaxWidth(w);
        backButton.setMinWidth(w);

        forwardButton.setPrefWidth(w);
        forwardButton.setMaxWidth(w);
        forwardButton.setMinWidth(w);

        refreshButton.setPrefWidth(w);
        refreshButton.setMaxWidth(w);
        refreshButton.setMinWidth(w);

        homeButton.setPrefWidth(w);
        homeButton.setMaxWidth(w);
        homeButton.setMinWidth(w);

        jumpButton.setPrefWidth(w);
        jumpButton.setMaxWidth(w);
        jumpButton.setMinWidth(w);


        double btWidth=backButton.getWidth();
        adressBar.setPrefWidth(sw - 6 * btWidth);
        adressBar.setMinWidth(sw- 6* btWidth);
        adressBar.setMaxWidth(sw - 6 * btWidth);
        adressBar.resize(sw - 6 * btWidth,50);


        titlebar.setPrefWidth(sw);
        titlebar.setMaxWidth(sw);
        titlebar.setMinWidth(sw);

    }


    @FXML
    void back()
    {
        // ???????????????????????? ??????????????? ???????????????
        if (webview.getEngine().getHistory().getCurrentIndex() > 0)
        {
            webview.getEngine().getHistory().go(-1);
        }
    }

    @FXML
    void forward()
    {
        // ??????????????????????????????
        if (webview.getEngine().getHistory().currentIndexProperty().getValue()
                < webview.getEngine().getHistory().getEntries().size() - 1)
        {
            webview.getEngine().getHistory().go(1);
        }
    }

    @FXML
    void home()
    {
        // ??????????????????
        goTo("https://www.baidu.com");
    }

    @FXML
    void jump()
    {
        // ??????????????????url ?????????????????????????????????
        goTo(adressBar.getText().startsWith("http") ? adressBar.getText() : "http://" + adressBar.getText());
    }

    @FXML
    void refresh()
    {
        // ????????????????????????
        webview.getEngine().reload();
    }

    void goTo(String urlString)
    {
        webview.getEngine().load(urlString);
    }

}
