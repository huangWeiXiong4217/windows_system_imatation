package apps.fileApp.app;


import apps.fileApp.Controller.MainCtl;
import apps.fileApp.com.File;
import apps.fileApp.com.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainUI
{
  private TreeItem<String> rootNode, recentNode;
  public static FAT fat;//设为静态变量，方便调用
  private int ind;
  public static Disk copyBlock;
  private List<Disk> blockList;
  public String recentPath;
  public static File copyFile;
  public  static  boolean copyFlag , moveFlag= false;

  private Map<Path, TreeItem<String>> pathMap = new HashMap<Path, TreeItem<String>>();

  private ObservableList<Disk> disksItem;
  private ObservableList<File> fileOpened;

  private ContextMenu contextMenu, contextMenu2;
  private MenuItem createFileItem, createFolderItem, openItem, delItem, propItem,copyItem,moveItem,pasteItem;

  private Label[] icons;
  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/apps/fileApp/fxmls/mainUI.fxml"));
  private Parent root = fxmlLoader.load();

  // 获取FXML里的元素
  public FlowPane flowPane = (FlowPane) root.lookup("#flowPane");
  private Label currentPath = (Label) root.lookup("#currentPath");
  private TreeView<String> treeView = (TreeView<String>) root.lookup("#treeView");
  private TabPane TabP = (TabPane) root.lookup("#TabP");
  private Tab chartTab = TabP.getTabs().get(0);
  public  static  boolean clearFlag = false;
  //额外生成的窗口记录-蔡棱添加
  public static Vector<Stage> fileAppAdditionStageList = new Vector<>();
  //更新窗口队列
  public static void updatefileStageList(Stage stage)
  {
    for(int i=0;i<fileAppAdditionStageList.size();i++)
    {
      if(fileAppAdditionStageList.get(i)==stage)
      {
        fileAppAdditionStageList.remove(stage);
        break;
      }
    }
    fileAppAdditionStageList.add(stage);
  }

  public MainUI(Stage primaryStage) throws Exception
  {
    new LoadView();
    //控制器初始化
    Scene scene = null;
    primaryStage.setResizable(false);
    primaryStage.getIcons().add(new Image("apps/fileApp/res/ico.png"));
    primaryStage.setTitle("磁盘文件管理系统");
    scene = new Scene(root);

    /***********************面板设置-蔡棱************************************/
    scene.setFill(Color.TRANSPARENT);
    primaryStage.initStyle(StageStyle.TRANSPARENT);

    MainCtl MainCtl = fxmlLoader.getController();
    MainCtl.init(primaryStage);

    /***********************面板设置-蔡棱************************************/

    primaryStage.setScene(scene);
    primaryStage.setResizable(false);

    recentPath = "C:";

//    关闭时保存数据
    primaryStage.setOnCloseRequest(e ->
    {
      try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data")))
      {
        System.out.println("saving");
        outputStream.writeObject(fat);
      }
      catch (FileNotFoundException e1)
      {
        e1.printStackTrace();
      }
      catch (IOException e2)
      {
        e2.printStackTrace();
      }

    });
    loadData();
    FAT.closeAll();
//  初始化菜单
    menuInit();
    menuItemSetOnAction();
//  初始化树形目录
    initTreeView();
//    初始化表格
    tableInit();
    primaryStage.show();
    chartTab.setOnSelectionChanged(ActionEvent ->
    {
      pieInit();
    });
  }

  private void loadData() {
    //    读取数据文件
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./data")))
    {
      fat = (FAT) inputStream.readObject();
    }
    catch (Exception e)
    {
//      e.printStackTrace();
      System.out.println("读取完毕");
      System.out.println("clearFlag="+clearFlag);
    }
    if (fat == null || clearFlag)
    {
      fat = new FAT();
      clearFlag = false;
      System.out.println("fat=null");
    }

  }

  private void menuInit()
  {
    createFileItem = new MenuItem("新建文件");
    createFolderItem = new MenuItem("新建文件夹");
    pasteItem = new MenuItem("粘贴");

    openItem = new MenuItem("打开");
    delItem = new MenuItem("删除");
    propItem = new MenuItem("属性");
    copyItem = new MenuItem("复制");
    moveItem = new MenuItem("剪切");

    contextMenu = new ContextMenu(createFileItem, createFolderItem,pasteItem);
    contextMenu2 = new ContextMenu(openItem, delItem, propItem,copyItem,moveItem);

//    右击工作区域显示菜单
    flowPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) ->
    {
      if (me.getButton() == MouseButton.SECONDARY && !contextMenu2.isShowing())
      {
        pasteItem.setDisable(true);

        if(copyFlag||moveFlag)
          pasteItem.setDisable(false);

        contextMenu.show(flowPane, me.getScreenX(), me.getScreenY());
      }
      else
      {
        contextMenu.hide();
      }
    });
  }

  private void tableInit()
  {
    //    初始化表格
    TableView<Disk> diskTable = (TableView<Disk>) root.lookup("#diskTable");
    TableColumn<Disk, String> disk = (TableColumn<Disk, String>) diskTable.getColumns().get(0);
    TableColumn<Disk, String> index = (TableColumn<Disk, String>) diskTable.getColumns().get(1);
    TableColumn<Disk, String> type = (TableColumn<Disk, String>) diskTable.getColumns().get(2);
    TableColumn<Disk, String> content = (TableColumn<Disk, String>) diskTable.getColumns().get(3);


    TableView<File> openFile = (TableView<File>) root.lookup("#openFile");
    TableColumn<File, String> fileName = (TableColumn<File, String>) openFile.getColumns().get(0);
    TableColumn<File, String> openType = (TableColumn<File, String>) openFile.getColumns().get(1);
    TableColumn<File, String> beginDisk = (TableColumn<File, String>) openFile.getColumns().get(2);
    TableColumn<File, String> fileLength = (TableColumn<File, String>) openFile.getColumns().get(3);
    TableColumn<File, String> filePath = (TableColumn<File, String>) openFile.getColumns().get(4);


    disk.setCellValueFactory(new PropertyValueFactory<Disk, String>("numP"));
    index.setCellValueFactory(new PropertyValueFactory<Disk, String>("indexP"));
    type.setCellValueFactory(new PropertyValueFactory<Disk, String>("typeP"));
    content.setCellValueFactory(new PropertyValueFactory<Disk, String>("objectP"));


    disksItem = FXCollections.observableArrayList(fat.getDiskBlocks());
    diskTable.setItems(disksItem);

    fileName.setCellValueFactory(new PropertyValueFactory<File, String>("fileNameP"));
    openType.setCellValueFactory(new PropertyValueFactory<File, String>("flagP"));
    beginDisk.setCellValueFactory(new PropertyValueFactory<File, String>("diskNumP"));
    fileLength.setCellValueFactory(new PropertyValueFactory<File, String>("lengthP"));
    filePath.setCellValueFactory(new PropertyValueFactory<File, String>("locationP"));

    fileOpened = fat.getOpenedFiles();
    openFile.setItems(fileOpened);
  }

  public void pieInit()
  {
    int UsedNum = 0;
    for (int i = 0; i < 256; i++)
    {
      if (fat.getDiskBlocks()[i].getIndex() != 0)
        UsedNum++;
    }
    DecimalFormat decimalFormat = new DecimalFormat("##.00%");
    System.out.println(decimalFormat.format(UsedNum / 256));
    System.out.println("已占用：" + UsedNum);
    PieChart pieChart = (PieChart) root.lookup("#pie");
    ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("未占用  " + decimalFormat.format((256 - UsedNum) / 256.0), 256 - UsedNum),
                    new PieChart.Data("已占用  " + decimalFormat.format(UsedNum / 256.0), UsedNum));
    pieChart.setData(pieChartData);
    pieChart.setLabelsVisible(false);
  }

  //  初始化树形目录
  private void initTreeView()
  {
    rootNode = new TreeItem<>("C:", new ImageView("apps/fileApp/res/disk.png"));
    ((ImageView) rootNode.getGraphic()).setFitWidth(20);
    ((ImageView) rootNode.getGraphic()).setFitHeight(20);

    rootNode.setExpanded(true);
    recentNode = rootNode;
    pathMap.put(fat.getPath("C:"), rootNode);
    treeView.setRoot(rootNode);
    treeView.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());
    for (Path path : fat.getPaths())
    {
      System.out.println(path);
      if (path.hasParent() && path.getParent().getPathName().equals(rootNode.getValue()))
      {
        initTreeNode(path, rootNode);
      }
    }
    addIcon(fat.getBlockList(recentPath), recentPath);
  }

  //  初始化每个树节点
  private void initTreeNode(Path newPath, TreeItem<String> parentNode)
  {
    TreeItem<String> newNode = addNode(parentNode, newPath);
    if (newPath.hasChild())
    {
      for (Path child : newPath.getChildren())
      {
        initTreeNode(child, newNode);
      }
    }
  }

  private TreeItem<String> addNode(TreeItem<String> parentNode, Path newPath)
  {
    String pathName = newPath.getPathName();
    String value = pathName.substring(pathName.lastIndexOf('\\') + 1);

    TreeItem<String> newNode = new TreeItem<String>(value, new ImageView("apps/fileApp/res/node.png"));
    newNode.setExpanded(true);
    pathMap.put(newPath, newNode);
    ((ImageView) (newNode.getGraphic())).setFitWidth(15);
    ((ImageView) (newNode.getGraphic())).setFitHeight(15);

    parentNode.getChildren().add(newNode);
    return newNode;
  }

  public void removeNode(TreeItem<String> recentNode, Path remPath)
  {
    recentNode.getChildren().remove(pathMap.get(remPath));
    pathMap.remove(remPath);
  }

  public void addIcon(List<Disk> bList, String path)
  {
    blockList = bList;
    int n = bList.size();
//    System.out.println("n="+n);
    icons = new Label[n];
    for (int i = 0; i < n; i++)
    {

      if (bList.get(i).getObject() instanceof Folder)
      {
        String nname = ((Folder) bList.get(i).getObject()).getFolderName();
        if (nname.length() > 4)
          nname = nname.substring(0, 4) + "..";
        icons[i] = new Label(nname, new ImageView("apps/fileApp/res/folder.png"));
        icons[i].setPrefSize(100, 100);
        icons[i].setMinSize(100, 100);
        icons[i].setMaxSize(100, 100);

        icons[i].setAlignment(Pos.CENTER);
        ((ImageView) (icons[i].getGraphic())).setFitWidth(60);
        ((ImageView) (icons[i].getGraphic())).setFitHeight(60);
      }
      else
      {
        String nname = ((File) bList.get(i).getObject()).getFileName();
        if (nname.length() > 4)
          nname = nname.substring(0, 4) + "..";
        icons[i] = new Label(nname, new ImageView("apps/fileApp/res/file.png"));
        icons[i].setPrefSize(100, 100);
        icons[i].setMinSize(100, 100);
        icons[i].setMaxSize(100, 100);

        icons[i].setAlignment(Pos.CENTER);
        ((ImageView) (icons[i].getGraphic())).setFitWidth(60);
        ((ImageView) (icons[i].getGraphic())).setFitHeight(60);
      }
      icons[i].setContentDisplay(ContentDisplay.TOP);
      icons[i].setWrapText(true);
      flowPane.getChildren().add(icons[i]);

      icons[i].setOnMouseEntered(new EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent event)
        {
          Label src = (Label) event.getSource();
          for (int j = 0; j < n; j++)
          {
            if (src == icons[j])
            {
              ind = j;

            }
          }
          Disk thisBlock = blockList.get(ind);
          Tooltip.install(flowPane, new Tooltip(thisBlock.getObject().toString()));
          ((Label) event.getSource()).setStyle("-fx-background-color: #f0f8ff;");
        }

      });
      icons[i].setOnMouseExited(new EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent event)
        {
          Disk thisBlock = blockList.get(ind);
          Tooltip.uninstall(flowPane, new Tooltip(thisBlock.getObject().toString()));
          ((Label) event.getSource()).setStyle("-fx-background-color: #ffffff;");
        }
      });
      icons[i].setOnMouseClicked(new EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent event)
        {

          Label src = (Label) event.getSource();
          for (int j = 0; j < n; j++)
          {
            if (src == icons[j])
            {
              ind = j;
            }
          }
          copyItem.setDisable(false);
          moveItem.setDisable(false);
          createFileItem.setDisable(false);
          if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1)
          {//右击
            Disk thisBlock = blockList.get(ind);
            if(thisBlock.getType() == "文件夹"){
              copyItem.setDisable(true);
              moveItem.setDisable(true);
            }

            contextMenu2.show(src, event.getScreenX(), event.getScreenY());
          }
          else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
          {//双击
            try
            {
              onOpen();
            }
            catch (IOException e)
            {
              e.printStackTrace();
            }
          }
          else
          {
            contextMenu2.hide();
          }
        }
      });
    }
  }

  private void onOpen() throws IOException
  {
    Disk thisBlock = blockList.get(ind);
    for (Disk block : blockList)
    {
      System.out.println(block);
    }
    if (thisBlock.getObject() instanceof File)
    {
      if (fat.getOpenedFiles().size() < 5)
      {
        if (fat.isOpenedFile(thisBlock))
        {
          try
          {
            fileViewOpen((File) thisBlock.getObject(),thisBlock);
          }
          catch (Exception exception)
          {
            exception.printStackTrace();
          }
        }
        else
        {
          try
          {
            fileViewOpen((File) thisBlock.getObject(),thisBlock);
          }
          catch (Exception exception)
          {
            exception.printStackTrace();
          }

          fat.addOpenedFile(thisBlock);

        }
      }
      else
      {
        try
        {
          tipOpen("文件打开已到上限");
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }
      }
    }
    else
    {
      Folder thisFolder = (Folder) thisBlock.getObject();
      String newPath = thisFolder.getLocation() + "\\" + thisFolder.getFolderName();
      flowPane.getChildren().removeAll(flowPane.getChildren());
      addIcon(fat.getBlockList(newPath), newPath);
      currentPath.setText(newPath);
      recentPath = newPath;
      recentNode = pathMap.get(thisFolder.getPath());
    }
  }

  public final class TextFieldTreeCellImpl extends TreeCell<String>
  {

    private TextField textField;
    //  监听树形目录点击事件
    public TextFieldTreeCellImpl()
    {

      this.setOnMouseClicked(new EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent event)
        {
          if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1)
          {
            if (getTreeItem() != null)
            {
              String pathName = null;
//              找到该树结点的路径
              for (Map.Entry<Path, TreeItem<String>> entry : pathMap.entrySet())
              {
                if (getTreeItem() == entry.getValue())
                {
                  pathName = entry.getKey().getPathName();
                  break;
                }
              }
              List<Disk> fats = fat.getBlockList(pathName);
              flowPane.getChildren().removeAll(flowPane.getChildren());
              addIcon(fats, pathName);
              recentPath = pathName;
              System.out.println(pathName);
              recentNode = getTreeItem();
              currentPath.setText(recentPath);
            }
          }
        }
      });
    }

    @Override
    public void updateItem(String item, boolean empty)
    {
      super.updateItem(item, empty);

      if (empty)
      {
        setText(null);
        setGraphic(null);
      }
      else
      {
        if (isEditing())
        {
          if (textField != null)
          {
            textField.setText(getString());
          }
          setText(null);
          setGraphic(textField);
        }
        else
        {
          setText(getString());
          setGraphic(getTreeItem().getGraphic());
        }
      }
    }
    private String getString()
    {
      return getItem() == null ? "" : getItem().toString();
    }

  }

  public TreeItem<String> getRecentNode()
  {
    return recentNode;
  }


  private void menuItemSetOnAction()
  {
    createFileItem.setOnAction(ActionEvent ->
    {
      if((((Folder)fat.getDiskBlocks()[2].getObject()).getCatalogNum()>7)&&currentPath.getText().equals("C:"))
      {

        try {
          tipOpen("根路径最多创建"+"\n"+"8个目录项");
        } catch (Exception e) {
          e.printStackTrace();
        }
        return;
      }
      int no = fat.createFile(recentPath);
      if (no == -1)
      {
        try
        {
          tipOpen("磁盘容量已满，无法创建");
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }

      }
      else
      {
        flowPane.getChildren().removeAll(flowPane.getChildren());
        addIcon(fat.getBlockList(recentPath), recentPath);
      }
    });

    createFolderItem.setOnAction(ActionEvent ->
    {

//      System.out.println("   sdad "+((Folder)fat.getDiskBlocks()[2].getObject()).getCatalogNum());
       if((((Folder)fat.getDiskBlocks()[2].getObject()).getCatalogNum()>7)&&currentPath.getText().equals("C:"))
    {

      try {
        tipOpen("根路径最多创建"+"\n"+"8个目录项");
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }
      int no = fat.createFolder(recentPath);
      if (no == -1)
      {
        try
        {
          tipOpen("磁盘容量已满，无法创建");
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }
      }

      else {
        Folder newFolder = (Folder) fat.getBlock(no).getObject();
        Path newPath = newFolder.getPath();
        flowPane.getChildren().removeAll(flowPane.getChildren());
        addIcon(fat.getBlockList(recentPath), recentPath);
        addNode(recentNode, newPath);
      }
    });

    openItem.setOnAction(ActionEvent ->
    {
      try
      {
        onOpen();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    });

    delItem.setOnAction(ActionEvent ->
    {
      Disk thisBlock = blockList.get(ind);
      if (fat.isOpenedFile(thisBlock))
      {
        try
        {
          tipOpen("文件未关闭");
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }
      }
      else
      {
        try
        {
          delViewOpen(thisBlock);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });

    propItem.setOnAction(ActionEvent ->
    {
      Disk thisBlock = blockList.get(ind);
      //          new PropertyView(thisBlock, fat, icons[ind], pathMap);
      try
      {
        propertyOpen(thisBlock,icons[ind],  pathMap);
      }
      catch (Exception exception)
      {
        exception.printStackTrace();
      }
    });

    copyItem.setOnAction(ActionEvent ->
    {
      Disk thisBlock = blockList.get(ind);
      copyBlock = thisBlock;
      copyFile = (File)thisBlock.getObject();
      copyFlag = true;
    });
    moveItem.setOnAction(ActionEvent ->
    {
      Disk thisBlock = blockList.get(ind);
      copyBlock = thisBlock;
      copyFile = (File)thisBlock.getObject();
      copyFlag = true;
      moveFlag =true;
    });

    pasteItem.setOnAction(ActionEvent ->
    {
      if(copyFlag) {
        int no = fat.createFile(recentPath);
        if (no == -1)
        {
          try
          {
            tipOpen("磁盘容量已满，无法粘贴");
          }
          catch (Exception exception)
          {
            exception.printStackTrace();
          }
        }
        else
        {
          if(moveFlag){
            fat.delete(copyBlock);
            moveFlag =false;
          }
          flowPane.getChildren().removeAll(flowPane.getChildren());

          addIcon(fat.getBlockList(recentPath), recentPath);
        }
        copyFlag = false;

      }
      }
     );
  }

  //属性窗口创建
  public void propertyOpen(Disk thisBlock, Label icon, Map<Path, TreeItem<String>> pathMap) throws Exception
  {
    Stage stage = new Stage();
    PropertyView propertyView = null;
    try
    {
      propertyView = new PropertyView(thisBlock,icon,pathMap,stage);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    propertyView.start(stage);
    stage.setAlwaysOnTop(true);
    stage.setIconified(false);
    stage.toFront();
  }
  //提示窗口创建
  public static void tipOpen(String tipString) throws Exception
  {
    Stage stage = new Stage();
    TipWindow tipWindow = new TipWindow(tipString);
    tipWindow.start(stage);
    stage.setAlwaysOnTop(true);
    stage.setIconified(false);
    stage.toFront();
    fileAppAdditionStageList.add(stage);
  }
  //文件窗口创建
  public static void fileViewOpen(File file,Disk block) throws Exception
  {
    System.out.println("oppppp1111"+file.isOpened());
    if(file.isOpened()){
      FileView.maps.get(file).show();
      FileView.maps.get(file).setAlwaysOnTop(true);
      FileView.maps.get(file).setIconified(false);
      FileView.maps.get(file).toFront();
    }
if(!file.isOpened()){
      Stage stage = new Stage();
      FileView fileView = new FileView(stage,file,block);
      fileView.start(stage);
      System.out.println("oppppp"+file.isOpened());
      stage.setAlwaysOnTop(true);
      stage.setIconified(false);
      stage.toFront();
      fileAppAdditionStageList.add(stage);
    }

  }
  //删除窗口创建
  public void delViewOpen(Disk block) throws Exception
  {
    Stage stage = new Stage();
    DelView delView = new DelView(MainUI.this,block);
    fileAppAdditionStageList.add(stage);
    delView.start(stage);
    stage.setAlwaysOnTop(true);
    stage.setIconified(false);
    stage.toFront();
  }
  //保存数据
  public static void saveData()
  {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"))) {
      System.out.println("saving");
      outputStream.writeObject(fat);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    }
  }
}
