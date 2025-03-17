package daoInterface;

import model.Users;

public interface UsersDAO extends BaseDAO<Users> {
    Users getByEmail(String email);
}
