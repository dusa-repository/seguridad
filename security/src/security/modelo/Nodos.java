package security.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nodos {

	private Nodos _parent;
	private List<Nodos> _children;
	private int _index;
	private String _label = "";

	public Nodos(Nodos parent, int index, String label) {
		_parent = parent;
		_index = index;
		_label = label;
	}

	public void setParent(Nodos parent) {
		_parent = parent;
	}

	public Nodos getParent() {
		return _parent;
	}

	public void appendChild(Nodos child) {
		if (_children == null)
			_children = new ArrayList<Nodos>();
		_children.add(child);
	}

	public List<Nodos> getChildren() {
		if (_children == null)
			return Collections.emptyList();
		return _children;
	}

	public void setIndex(int index) {
		_index = index;
	}

	public int getIndex() {
		return _index;
	}

	public String getLabel() {
		return _label;
	}

	public String toString() {
		return getLabel();
	}
}