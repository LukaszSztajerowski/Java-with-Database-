import entity.*;

public class Main {
    public static void main(String[] args) {
UserDAO dao = new UserDAO();
        dao.read(1);
        System.out.println(dao.read(1).getUserName() + " " + dao.read(1).getId());

        dao.read(3);
        System.out.println(dao.read(3).getUserName());
//dao.update(dao.read(2));
//dao.delete(2);
int liczba = dao.findAll().length;
        System.out.println(liczba);
    }
}
