package dv.kinash.HW6_JPA.repository;

import dv.kinash.HW6_JPA.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private EntityManager entityManager;

    public List<User> getList(){
        return entityManager.createQuery("From User u").getResultList();
    }

    @Transactional
    public User save(User user){
        return entityManager.merge(user);
    }

    public User getById(Long id){
        return entityManager.find(User.class, id);
    }

    public List<User> getByEmail(String email){
        Query query = entityManager.createQuery("FROM User Where email=:email");
        query.setParameter("email", email);
        return query.getResultList();
    }

    @Transactional
    public boolean delete(Long id){
        User user = getById(id);
        if (user == null)
            return false;
        entityManager.remove(user);
        return true;
    }

}
