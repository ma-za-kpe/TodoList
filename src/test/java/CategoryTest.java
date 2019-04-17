import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class CategoryTest {

    @Before
    public void setUp() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
    }

    @After
    public void tearDown() {
        try(Connection con = DB.sql2o.open()) {
            String deleteTasksQuery = "DELETE FROM tasks *;";
            String deleteCategoriesQuery = "DELETE FROM categories *;";
            con.createQuery(deleteTasksQuery).executeUpdate();
            con.createQuery(deleteCategoriesQuery).executeUpdate();
        }
    }

    @Test
    public void equals_returnsTrueIfNamesAretheSame() {
        Category firstCategory = new Category("Household chores");
        Category secondCategory = new Category("Household chores");
        assertTrue(firstCategory.equals(secondCategory));
    }

    @Test
    public void save_savesIntoDatabase_true() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        assertTrue(Category.all().get(0).equals(myCategory));
    }

    @Test
    public void all_returnsAllInstancesOfCategory_true() {
        Category firstCategory = new Category("Home");
        firstCategory.save();
        Category secondCategory = new Category("Work");
        secondCategory.save();
        assertEquals(true, Category.all().get(0).equals(firstCategory));
        assertEquals(true, Category.all().get(1).equals(secondCategory));
    }

    //assigning unique ids
    @Test
    public void save_assignsIdToObject() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Category savedCategory = Category.all().get(0);
        assertEquals(myCategory.getId(), savedCategory.getId());
    }

    //get with id
    @Test
    public void getId_categoriesInstantiateWithAnId_1() {
        Category testCategory = new Category("Home");
        testCategory.save();
        assertTrue(testCategory.getId() > 0);
    }

    @Test
    public void find_returnsCategoryWithSameId_secondCategory() {
        Category firstCategory = new Category("Home");
        firstCategory.save();
        Category secondCategory = new Category("Work");
        secondCategory.save();
        assertEquals(Category.find(secondCategory.getId()), secondCategory);
    }

    //ave a categoryId into our tasks table, thereby associating a Task with its Category
    @Test
    public void save_savesCategoryIdIntoDB_true() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Task myTask = new Task("Mow the lawn", myCategory.getId());
        myTask.save();
        Task savedTask = Task.find(myTask.getId());
        assertEquals(savedTask.getCategoryId(), myCategory.getId());
    }

    //retrieve all Tasks saved into a specific Category
    @Test
    public void getTasks_retrievesALlTasksFromDatabase_tasksList() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Task firstTask = new Task("Mow the lawn", myCategory.getId());
        firstTask.save();
        Task secondTask = new Task("Do the dishes", myCategory.getId());
        secondTask.save();
        Task[] tasks = new Task[] { firstTask, secondTask };
        assertTrue(myCategory.getTasks().containsAll(Arrays.asList(tasks)));
    }

    //update category
    @Test
    public void update_updatesTaskDescription_true() {
        Category myCategory= new Category("Mow the lawn");
        myCategory.save();
        myCategory.update("Take a nap");
        assertEquals("Take a nap", Category.find(myCategory.getId()).getName());
    }
}