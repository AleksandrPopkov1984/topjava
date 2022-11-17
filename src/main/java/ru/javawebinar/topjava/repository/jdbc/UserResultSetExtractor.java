package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        Map<Integer, User> userMap = new HashMap<>();

        while (rs.next()) {
            Integer userKey = rs.getInt("id");
            User user = userMap.get(userKey);
            if (user == null) {
                user = new User();
                userMap.put(userKey, user);
                users.add(user);
                user.setId(userKey);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            }
            if (rs.getString("role") != null) {
                Role role = Role.valueOf(rs.getString("role"));
                user.getRoles().add(role);
            }
        }
        return users;
    }
}
