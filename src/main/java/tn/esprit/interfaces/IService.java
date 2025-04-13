package tn.esprit.interfaces;

import java.util.List;

public interface IService <T> {
    void add(T e);
    void update(T e);
    void delete(int id);
    List<T> retrieveAll();
}