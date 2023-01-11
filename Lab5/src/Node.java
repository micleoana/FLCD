public class Node {
    private Integer index;
    private String info;
    private Integer parent;
    private Integer sibling;

    public Node(Integer index, String info, Integer parent, Integer sibling) {
        this.info = info;
        this.index = index;
        this.parent = parent;
        this.sibling = sibling;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSibling() {
        return sibling;
    }

    public void setSibling(Integer sibling) {
        this.sibling = sibling;
    }

    @Override
    public String toString() {
        return "Node{" +
                "index=" + index +
                ", info='" + info + '\'' +
                ", parent=" + parent +
                ", sibling=" + sibling +
                '}';
    }
}
