package project.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.models.Person;
import java.util.List;

@Component
public class PersonDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "select p from Person p", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person personToBeUpdated = session.get(Person.class, id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Person.class, id));
    }
}

/** sessionFactory
Внедряем сюда sessionFactory, бин, которой мы сделали в
project/config/SpringConfig.java
Этот бин нужен будет для всех операций
 */
/** @Transactional (readOnly = true)
выбираем из пакета
import org.springframework.transaction.annotation.Transactional;
Используем эту аннотацию для каждого метода,
чтобы Спринг автоматически открывал и закрывал транзакции
(не делать это каждый раз вручную)  после выполнения всех строк кода
(readOnly = true) - не обязательный, но желательный параметр,
означающий, что данный метод будет только читать БД
 */
/** index() вывести всех людей
В данном случае используется HQL - это вариант
SQL для Hibernate
вывод можно посмотреть здесь
(см. src/main/resources/pic/index.png)
*/
/** show() - показать данные выбранного на форме человека
(см. src/main/resources/pic/show.png)
 */
/** save() - сохраняем данные нового человека
 (см. src/main/resources/pic/create_new_save.png)
*/
/** update() - редактируем и обновляем данные нового человека
 (см. src/main/resources/pic/edit_update.png)
 */
/** delete - удаляем человека по id
 (см. src/main/resources/pic/delete.png)
*/



