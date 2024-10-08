package telran.io;

public interface Persistable {
    void saveToFile(String fileName) throws Exception;

    void restoreFromFile(String fileName) throws Exception;
}
