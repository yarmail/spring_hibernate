package project.models;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    public Person() {

    }

    public Person( String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
/** Превращаем наш обычный класс в сущность для Hibernate
@Entity (сущность)
Добавляем над классом аннотацию

@Table(name = "Person")
Связываем с класс с таблицей Person в базе данных

@Id
В обязательном порядке добавляем аннотацию  над полем id

@GeneratedValue(strategy = GenerationType.IDENTITY)
Указываем, что генерация id будет происходить
автоматически в БД

@Column
Каждому полю в классе назначаем свою колонку в таблице

Конструкторы
В обязательном порядке добавляем пустой конструктор
Второй конструктор без id, т.к. он генерируется
автоматически
 */


/**@NotEmpty, @Size, @Min, @NotEmpty
 * Аннотации используются для валидации на представлении
 * т.е. если на форме (на странице) мы зададим неправильный папаметр
 * появится красное предупреждение
 */
