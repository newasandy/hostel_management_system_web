package daoInterface;

import model.Users;
import java.util.List;

public interface UsersDAO extends BaseDAO<Users> {
    Users getByEmail(String email);
    List<Users> getUnallocatedUsers();
    List<Users> getOnlyStudent();
}
