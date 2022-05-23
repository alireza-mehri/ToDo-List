package ir.alirezamehri.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TaskDao {
    @Insert
    long addTask(Task task);

    @Delete
    int deleteTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("SELECT * FROM tbl_tasks")
    List<Task> getTasks();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE '%' || :title || '%'")
    List<Task> searchInTasks(String title);

    @Query("DELETE FROM tbl_tasks")
    void deleteAllTasks();
}
