package apps.fileApp.com;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.ExecutableFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Disk implements Serializable {

	private static final long serialVersionUID = 1L;

	private int num;
	private int index;
	private String type;
	private Object object;

	private boolean begin;

	private transient StringProperty numP = new SimpleStringProperty();
	private transient StringProperty indexP = new SimpleStringProperty();
	private transient StringProperty typeP = new SimpleStringProperty();
	private transient StringProperty objectP = new SimpleStringProperty();

	//UI获取property的方法
	public StringProperty numPProperty() {
		return numP;
	}
	public StringProperty indexPProperty() {
		return indexP;
	}
	public StringProperty typePProperty() {
		return typeP;
	}
	public StringProperty objectPProperty() {
		return objectP;
	}

	private void setnumP() {
		this.numP.set(String.valueOf(num));
	}
	private void setIndexP() {
		this.indexP.set(String.valueOf(index));
	}
	private void setTypeP() {
		this.typeP.set(type);
	}
	private void setObjectP() {
		this.objectP.set(object == null ? "" : object.toString());
		if(object instanceof ExecutableFile)
      this.objectP.set(((ExecutableFile) object).getName());
	}

	public Disk(int num, int index, String type, Object object) {
		super();
		this.num = num;
		this.index = index;
		this.type = type;
		this.object = object;
		this.begin = false;
		setnumP();
		setIndexP();
		setTypeP();
		setObjectP();
	}

	public int getnum() {
		return num;
	}

	public void setnum(int num) {
		this.num = num;
		setnumP();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		setIndexP();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		setTypeP();
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
		if (object instanceof File) {
			this.objectP.bind(((File)object).fileNamePProperty());
		} else if (object instanceof Folder){
			this.objectP.bind(((Folder)object).folderNamePProperty());
		} else {
			this.objectP.unbind();
			setObjectP();
		}
	}

	public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
	}

	public void allocBlock(int index, String type, Object object, boolean begin) {
		setIndex(index);
		setType(type);
		setObject(object);
		setBegin(begin);
	}

	public void clearBlock() {
		setIndex(0);
		setType("空");
		setObject(null);
		setBegin(false);
	}

	public boolean isFree() {
		return index == 0;
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    	s.defaultReadObject();
    	numP = new SimpleStringProperty(String.valueOf(num));
    	indexP = new SimpleStringProperty(String.valueOf(index));
    	typeP = new SimpleStringProperty(type);
    	objectP = new SimpleStringProperty(object == null ? "" : object.toString());
    	setObject(object);
	}

	@Override
	public String toString() {
		Object object = getObject();
		if (object instanceof File) {
			return ((File)object).toString();
		} else if(object instanceof Folder){
			return ((Folder)object).toString();
		}else {
      return ((ExecutableFile)object).getName();
    }
	}
}
