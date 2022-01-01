package apps.helpApp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.DrawUtil;

public class HelpAppController
{

    @FXML
    private AnchorPane topMainPane;
    @FXML
    private BorderPane titlebar;

    @FXML
    private HBox toolBar;

    @FXML
    private TabPane tabpane;

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
    public void adaptWindow()
    {
        double sw=stage.getWidth();
        double sh=stage.getHeight();
        double w = 50 *sw/1920;

        titlebar.setPrefWidth(sw);
        titlebar.setMaxWidth(sw);
        titlebar.setMinWidth(sw);

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

    public void init(Stage stage)
    {
        this.stage=stage;

        // 拖动监听器
        DrawUtil drawUtil=new DrawUtil();
        drawUtil.addDrawFunc(stage, topMainPane);
        //监听窗口
        // 监听窗口面板高度变化
        stage.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                // 更新GUI组件
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
                // 更新GUI组件
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



}
