import java.util.Objects;

public class Entry {
    private String key;
    private String value;

    public Entry(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Entry e))
            return false;
        return Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
    }

    @Override
    public String toString(){
        return key+", "+value;
    }
}

