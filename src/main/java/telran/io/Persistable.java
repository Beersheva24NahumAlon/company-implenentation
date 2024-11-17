package telran.io;

public interface Persistable {
    boolean saveToFile(String fileName);

    void restoreFromFile(String fileName);
}
