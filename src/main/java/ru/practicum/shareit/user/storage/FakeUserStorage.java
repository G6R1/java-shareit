package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class FakeUserStorage implements UserStorage{

    final private Map<Long, User> inMemoryStorage;
    private Long idCounter;

    public FakeUserStorage() {
        this.inMemoryStorage = new HashMap<>();
        idCounter = 1L;
    }


    @Override
    public User createUser(User user) {
        User userForSave = new User(idCounter, user.getName(), user.getEmail());
        inMemoryStorage.put(idCounter, userForSave);
        return userForSave;
    }

    @Override
    public User patchUser(User user) {
        inMemoryStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(Long userId) {
        return inMemoryStorage.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return inMemoryStorage.values();
    }

    @Override
    public boolean deleteUser(Long userId) {
        inMemoryStorage.remove(userId);
        return true;
    }
}
