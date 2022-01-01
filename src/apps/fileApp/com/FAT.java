package apps.fileApp.com;

import apps.fileApp.app.MainUI;
import apps.fileApp.app.TipWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import utils.ExecutableFile;
import utils.ProcessManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FAT implements Serializable{

  private static final long serialVersionUID = 1L;
  private Disk[] disks;
  private  static transient ObservableList<File> openedFiles;
  private Folder c;
  private Path rootPath = new Path("C:", null);
  private List<Path> paths;

  public FAT() {
    File f1 = new  File("FAT", null,0,null);
    disks = new Disk[256];
    disks[0] = new Disk(0, 1, "文件", f1);
    disks[0].setBegin(true);
    disks[1] = new Disk(1, -1, "文件", f1);
    c = new Folder("C:", "root", 2, null);
    disks[2] = new Disk(2, -1, "root", c);
    disks[2].setBegin(true);
    for(int i = 3; i< (3+ ProcessManager.executableFileList.size()); i++){
      ExecutableFile f = ProcessManager.executableFileList.get(i-3);
      disks[i] = new Disk( i, -1, "可执行文件", f);
      disks[i].allocBlock(-1, "可执行文件", f, true);
//      reallocBlocks(blocksCount(f.size),disks[i]);
      disks[i].setBegin(true);
    }

//		初始化盘块
    for (int i = 3+ProcessManager.executableFileList.size(); i < 256; i++) {
      disks[i] = new Disk(i, 0, "空", null);
    }
    openedFiles = FXCollections.observableArrayList(new ArrayList<File>());
    paths = new ArrayList<Path>();
    paths.add(rootPath);
    c.setPath(rootPath);
  }

  public void addOpenedFile(Disk block) {
    File thisFile = (File) block.getObject();
    openedFiles.add(thisFile);
    thisFile.setOpened(true);
  }
  //关闭文件
  public static void removeOpenedFile(Disk block) {
    File thisFile = (File) block.getObject();
    for (int i = 0; i < openedFiles.size(); i++) {
      if (openedFiles.get(i) == thisFile) {
        openedFiles.remove(i);
        thisFile.setOpened(false);
        break;
      }
    }
  }

  public static  void closeAll(){
    if(openedFiles!=null){
      for (int i = 0; i < openedFiles.size(); i++) {
        openedFiles.get(i).setOpened(false);
        openedFiles.remove(i);
      }
    }
  }

  //判断是否打开了的文件
  public boolean isOpenedFile(Disk block) {
    if (block.getObject() instanceof Folder) {
      return false;
    }
    return ((File) block.getObject()).isOpened();
  }

  //提示窗口创建
  public void tipOpen(String tipString) throws Exception
  {
    Stage stage = new Stage();
    TipWindow tipWindow = new TipWindow(tipString);
    tipWindow.start(stage);
    stage.setAlwaysOnTop(true);
    stage.setIconified(false);
    stage.toFront();
  }

  //在指定路径下创建文件夹
  public int createFolder(String path) {

    String folderName = null;
    boolean canName = true;
    int index = 1;
    // 得到文件夹名 1.2.3...
    do {
      folderName = "文件夹";
      canName = true;
      folderName += index;
      for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
        if (!disks[i].isFree()) {
          if (disks[i].getType().equals("文件夹")) {
            Folder folder = (Folder) disks[i].getObject();
            if (path.equals(folder.getLocation()) && folderName.equals(folder.getFolderName()))
            {
              canName = false;
            }
          }
        }
      }
      index++;
    } while (!canName);

    int num = searchEmptyDiskBlock();
    if (num == -1) {
      return -1;
    } else {
      Folder parent = getFolder(path);
//////////////////////////////////
      if(path.equals("C:")){
        parent.setCatalogNum(parent.getCatalogNum()+1);
      }
      else {
        parent.setCatalogNum(parent.getCatalogNum()+1);
        int countBlock = 0;
        if(parent.getCatalogNum()%8 == 0)
          countBlock = parent.getCatalogNum()/8;
        else
          countBlock =parent.getCatalogNum()/8+1;
        reallocFolderBlocks(countBlock, disks[parent.getDiskNum()]);
      }

///////////////////////////////////////
      num= searchEmptyDiskBlock();
      Folder newFolder = new Folder(folderName, path, num, parent);
      if (parent instanceof Folder) {
        parent.addChildren(newFolder);
      }
      disks[num].allocBlock(-1, "文件夹", newFolder, true);
      Path parentPath = getPath(path);
      Path thisPath = new Path(path + "\\" + folderName, parentPath);
      if (parentPath != null) {
        parentPath.addChildren(thisPath);
      }
      paths.add(thisPath);
      newFolder.setPath(thisPath);
    }
    return num;
  }

  //在指定路径下创建文件
  public int createFile(String path) {
    String fileName = null;
    boolean canName = true;
    int index = 1;
    // 得到文件名 1.2.3....
    do {
      fileName = "文件";
      canName = true;
      fileName += index;
      for (int i = 3; i < disks.length; i++) {
        if (!disks[i].isFree() && disks[i].getType().equals("文件")) {
          File file = (File) disks[i].getObject();
          if (path.equals(file.getLocation()) && fileName.equals(file.getFileName())) {
            canName = false;
          }
        }
      }
      index++;
    } while (!canName);
    int num = searchEmptyDiskBlock();
    if (num == -1) {
      return -1;
    } else {
      Folder parent = getFolder(path);
  System.out.println("pp"+parent.getDiskNum());////////////////////////
      parent.setCatalogNum(parent.getCatalogNum()+1);
      if(!path.equals("C:")){
        int countBlock = 0;
        if(parent.getCatalogNum()%8 == 0)
          countBlock = parent.getCatalogNum()/8;
        else
          countBlock =parent.getCatalogNum()/8+1;
        reallocFolderBlocks(countBlock, disks[parent.getDiskNum()]);
      }

      System.out.println(parent.getCatalogNum()+" 目录项 "+parent.getFolderName());
      num = searchEmptyDiskBlock();
      File file = new File(fileName, path, num, parent);
      file.setFlag(1);//默认新建读写文件

      if(MainUI.copyFlag){//粘贴重命名
        file.setContent(MainUI.copyFile.getContent());
        boolean canName1 = true;
        index = 1;
        // 得到文件名 1.2.3....
        do {
          fileName = MainUI.copyFile.getFileName();
          canName1 = true;
          fileName += index;
          for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
            if (!disks[i].isFree() && disks[i].getType().equals("文件")) {
              File file1 = (File) disks[i].getObject();
              if (path.equals(file1.getLocation()) && fileName.equals(file1.getFileName())) {
                canName1 = false;
                break;
              }
            }
          }
          index++;
        } while (!canName1);
        file.setFileName(fileName);
        int newLength = MainUI.copyFile.getContent().length();
        int blockCount = FAT.blocksCount(newLength);
        file.setLength(blockCount);
        file.setContent(MainUI.copyFile.getContent());
        file.setSize(FAT.getSize(newLength));
        if (file.hasParent()) {
          Folder parent1 = (Folder) file.getParent();

//          parent1.setCatalogNum(parent1.getCatalogNum()+1);
//          System.out.println(parent1.getCatalogNum()+" 目录项 "+parent1.getFolderName());

          parent1.setSize(FAT.getFolderSize(parent1));
          while (parent1.hasParent()) {
            parent1 = (Folder) parent1.getParent();
            parent1.setSize(FAT.getFolderSize(parent1));
          }
        }

        if(MainUI.moveFlag){
          boolean canName2 = true;
          index = 1;
          // 得到文件名 1.2.3....
          do {
            fileName = MainUI.copyFile.getFileName();
            canName2 = true;
            fileName += index;
            for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
              if (!disks[i].isFree() && disks[i].getType().equals("文件")) {
                File file2 = (File) disks[i].getObject();
                if (path.equals(file2.getLocation()) && fileName.equals(file2.getFileName())) {
                  canName2 = false;
                  break;
                }
              }
            }
            index++;
          } while (!canName2);
          file.setFileName(fileName);
        }

        reallocBlocks(blockCount, MainUI.copyBlock);  ///
      }
      if (parent instanceof Folder) {
        parent.addChildren(file);
      }
      disks[num].allocBlock(-1, "文件", file, true);
    }

    return num;
  }

  //返回第一个空闲盘块的盘块号
  public int searchEmptyDiskBlock() {
    for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
      if (disks[i].isFree()) {
        return i;
      }
    }
    return -1;
  }

  //计算空闲盘块数
  public int freeBlocksCount() {
    int n = 0;
    for (int i = 0; i < disks.length; i++) {
      if (disks[i].isFree()) {
        n++;
      }
    }
    return n;
  }

  //	文件长度变更时重新分配盘块
  public boolean reallocBlocks(int num, Disk block) {
    File thisFile = (File) block.getObject();
    int begin = thisFile.getDiskNum();
    int index = disks[begin].getIndex();
    int oldNum = 1;
    while (index != -1) {
      oldNum++;
      if (disks[index].getIndex() == -1) {
        begin = index;
      }
      index = disks[index].getIndex();
    }

    if (num > oldNum) {
      // 增加磁盘块
      int n = num - oldNum;
      if (freeBlocksCount() < n) {
        // 超过磁盘容量
        return false;
      }
      int space = searchEmptyDiskBlock();
      disks[begin].setIndex(space);
      for (int i = 1; i <= n; i++) {
        space = searchEmptyDiskBlock();
        if (i == n) {
          disks[space].allocBlock(-1, "文件", thisFile, false);
        } else {
          disks[space].allocBlock(-1, "文件", thisFile, false);// 同一个文件的所有磁盘块拥有相同的对象
          int space2 = searchEmptyDiskBlock();
          disks[space].setIndex(space2);
        }
        System.out.println(thisFile);
      }
    } else if (num < oldNum) {
      // 减少释放磁盘块
      int end = thisFile.getDiskNum();
      while (num > 1) {
        end = disks[end].getIndex();
        num--;
      }
      int next = 0;
      for (int i = disks[end].getIndex(); i != -1; i = next) {
        next = disks[i].getIndex();
        disks[i].clearBlock();
      }
      disks[end].setIndex(-1);
    }
    thisFile.setLength(num);
    return true;
  }
///////////////////////////////////
public boolean reallocFolderBlocks(int num, Disk block) {
  Folder thisFolder = (Folder) block.getObject();
  int begin = thisFolder.getDiskNum();
  int index = disks[begin].getIndex();
  int oldNum = 1;
  while (index != -1) {
    oldNum++;
    if (disks[index].getIndex() == -1) {
      begin = index;
    }
    index = disks[index].getIndex();
  }

  if (num > oldNum) {
    // 增加磁盘块
    int n = num - oldNum;
    if (freeBlocksCount() < n) {
      // 超过磁盘容量
      return false;
    }
    int space = searchEmptyDiskBlock();
    disks[begin].setIndex(space);
    for (int i = 1; i <= n; i++) {
      space = searchEmptyDiskBlock();
      if (i == n) {
        disks[space].allocBlock(-1, "文件夹", thisFolder, false);
      } else {
        disks[space].allocBlock(-1, "文件夹", thisFolder, false);// 同一个文件的所有磁盘块拥有相同的对象
        int space2 = searchEmptyDiskBlock();
        disks[space].setIndex(space2);
      }
      System.out.println(thisFolder);
//      System.out.println("FolderAclsda");
    }
  } else if (num < oldNum) {
    // 减少释放磁盘块
    int end = thisFolder.getDiskNum();
    while (num > 1) {
      end = disks[end].getIndex();
      num--;
    }
    int next = 0;
    for (int i = disks[end].getIndex(); i != -1; i = next) {
      next = disks[i].getIndex();
      disks[i].clearBlock();
    }
    disks[end].setIndex(-1);
  }
  return true;
}
 ///////////////////////////////////////////

  //返回指定路径下所有文件夹
  public List<Folder> getFolders(String path) {
    List<Folder> list = new ArrayList<Folder>();
    for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
      if (!disks[i].isFree()) {
        if (disks[i].getObject() instanceof Folder) {
          if (((Folder) (disks[i].getObject())).getLocation().equals(path)) {
            list.add((Folder) disks[i].getObject());
          }
        }
      }
    }
    return list;
  }
  /*
   * 返回所有文件夹和文件的起始盘块
   */
  public List<Disk> getBlockList(String path) {
    List<Disk> List = new ArrayList<Disk>();
    for (int i =3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
      if (!disks[i].isFree()) {
        if (disks[i].getObject() instanceof Folder) {
          if (((Folder) (disks[i].getObject())).getLocation().equals(path)
            && disks[i].isBegin()) {
            List.add(disks[i]);
          }
        }
      }
    }
    int n =0 ;
    for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
      if (!disks[i].isFree()) {
        if (disks[i].getObject() instanceof File) {
          if (((File) (disks[i].getObject())).getLocation().equals(path)
            && disks[i].isBegin()) {
            List.add(disks[i]);
            n++;
          }
        }
      }
    }
    System.out.println("file-length:"+n);
    return List;
  }

  /*
   * 返回指定路径指向的文件夹
   */
  public Folder getFolder(String path) {
    if (path.equals("C:")) {
      return c;
    }
    int split = path.lastIndexOf('\\');
    String location = path.substring(0, split);
    String folderName = path.substring(split + 1);
    List<Folder> folders = getFolders(location);
    for (Folder folder : folders) {
      if (folder.getFolderName().equals(folderName)) {
        return folder;
      }
    }
    return null;
  }

  /*
   * 给出路径名返回路径对象
   */
  public Path getPath(String path) {
    for (Path p : paths) {
      if (p.getPathName().equals(path)) {
        return p;
      }
    }
    return null;
  }
  /*
   * 删除文件或文件夹
   */
  public int delete(Disk block) {
//	  删除文件
    if (block.getObject() instanceof File) {
      if (isOpenedFile(block)) {
        // 文件已打开，不能删除
        return 3;
      }
      File thisFile = (File) block.getObject();
      Folder parent = thisFile.getParent();
      if (parent instanceof Folder) {
        parent.setCatalogNum(parent.getCatalogNum()-1);
        int countBlock = 0;
        if(parent.getCatalogNum()%8 == 0)
          countBlock = parent.getCatalogNum()/8;
        else
          countBlock =parent.getCatalogNum()/8+1;
        reallocFolderBlocks(countBlock, disks[parent.getDiskNum()]);
        parent.removeChildren(thisFile);
        parent.setSize(this.getFolderSize(parent));
//				修改路径上的文件夹大小
        while (parent.hasParent()) {
          parent = parent.getParent();
          parent.setSize(this.getFolderSize(parent));
        }
      }
//			释放盘块
      for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
        if (!disks[i].isFree() && disks[i].getObject() instanceof File) {
          if (((File) disks[i].getObject()).equals(thisFile)) {// 同一个对象
            disks[i].clearBlock();
          }
        }
      }
      return 1;
    } else {// 删除文件夹
      String folderPath = ((Folder) block.getObject()).getLocation() + "\\"
        + ((Folder) block.getObject()).getFolderName();
      int index = 0;
      for (int i = 3+ProcessManager.executableFileList.size(); i < disks.length; i++) {
        if (!disks[i].isFree()) {
          Object obj = disks[i].getObject();
          if (disks[i].getType().equals("文件夹")) {
            if (((Folder) obj).getLocation().equals(folderPath)) {
              // 文件夹不为空，不能删除
              return 2;
            }
          } else {
            if (((File) obj).getLocation().equals(folderPath)) {
              // 文件夹不为空，不能删除
              return 2;
            }
          }
          if (disks[i].getType().equals("文件夹")) {
            if (((Folder) disks[i].getObject()).equals(block.getObject())) {
              index = i;
            }
          }
        }
      }
      Folder thisFolder = (Folder) block.getObject();
      Folder parent = thisFolder.getParent();
      if (parent instanceof Folder) {
        parent.removeChildren(thisFolder);
        parent.setSize(this.getFolderSize(parent));
        parent.setCatalogNum(parent.getCatalogNum()-1);
        int countBlock = 0;
        if(parent.getCatalogNum()%8 == 0)
          countBlock = parent.getCatalogNum()/8;
        else
          countBlock =parent.getCatalogNum()/8+1;
        reallocFolderBlocks(countBlock, disks[parent.getDiskNum()]);
      }
      paths.remove(getPath(folderPath));
      disks[index].clearBlock();
      return 0;
    }
  }

  public Disk[] getDiskBlocks() {
    return disks;
  }

  public void setDiskBlocks(Disk[] disks) {
    this.disks = disks;
  }

  /*
   * 按盘块号查找盘块
   */
  public Disk getBlock(int index) {
    return disks[index];
  }

  public ObservableList<File> getOpenedFiles() {
    return openedFiles;
  }

  public void setOpenedFiles(ObservableList<File> openFiles) {
    this.openedFiles = openFiles;
  }

  public List<Path> getPaths() {
    return paths;
  }

  public void setPaths(List<Path> paths) {
    this.paths = paths;
  }

  public void addPath(Path path) {
    paths.add(path);
  }

  public void removePath(Path path) {
    paths.remove(path);
    if (path.hasParent()) {
      path.getParent().removeChildren(path);
    }
  }

  public void replacePath(Path oldPath, String newName) {
    oldPath.setPathName(newName);
  }

  public boolean hasPath(Path path) {
    for (Path p : paths) {
      if (p.equals(path)) {
        return true;
      }
    }
    return false;
  }

  /*
   * 判断指定路径下是否有同名文件夹或文件
   */
  public boolean hasName(String path, String name) {
    Folder thisFolder = getFolder(path);
    for (Object child : thisFolder.getChildren()) {
      if (child.toString().equals(name)) {
        return true;
      }
    }
    return false;
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    openedFiles = FXCollections.observableArrayList(new ArrayList<File>());
  }

  public static double getSize(int length) {
    return Double.parseDouble(String.valueOf(length));
  }
  //	求文件夹长度
  public static double getFolderSize(Folder folder) {
    List<Object> children = folder.getChildren();
    double size = 0;
    for (Object child : children) {
      if (child instanceof File) {
        size += ((File)child).getSize();
      } else {
        size += getFolderSize((Folder)child);
      }
    }
    return Double.parseDouble(String.valueOf(size));
  }
  public static int blocksCount(int length){
    if (length <= 64){
      return 1;
    } else {
      int n = 0;
      if (length % 64 == 0){
        n = length / 64;
      } else {
        n = length / 64;
        n++;
      }
      return n;
    }
  }
}
