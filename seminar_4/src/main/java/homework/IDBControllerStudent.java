package homework;

public interface IDBControllerStudent {
    public Student findById(long id);
    public void persist(Student student);
    public void merge(Student student);
    public Student remove(long id);
}
