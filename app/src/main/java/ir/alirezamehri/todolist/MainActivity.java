package ir.alirezamehri.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.AddNewTaskCallback, TaskAdapter.TaskItemEventListener,
        EditTaskDialog.EditNewTaskCallback {
    private TaskDao taskDao;
    private boolean isNull = true;
    private TaskAdapter taskAdapter = new TaskAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDao = AppDatabase.getAppDatabase(this).getTaskDao();

        EditText etSearch = findViewById(R.id.et_search_tasks);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<Task> tasks = taskDao.searchInTasks(s.toString());
                    taskAdapter.setTasks(tasks);
                } else {
                    List<Task> tasks = taskDao.getTasks();
                    taskAdapter.setTasks(tasks);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_main_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(taskAdapter);
        List<Task> tasks = taskDao.getTasks();
        TextView nullTasks = findViewById(R.id.tv_null_tasks);

        if (tasks.size() == 0) {
            isNull = true;
            nullTasks.setVisibility(View.VISIBLE);
        } else {
            taskAdapter.addItems(tasks);
        }

        View clearTasksBtn = findViewById(R.id.btn_clear_tasks);
        clearTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDao.deleteAllTasks();
                taskAdapter.clearAllItems();

                isNull = true;
                nullTasks.setVisibility(View.VISIBLE);
            }
        });

        View addNewFabTask = findViewById(R.id.fab_main_add_task);
        addNewFabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskDialog dialog = new AddTaskDialog();
                dialog.show(getSupportFragmentManager(), "null");
            }
        });
    }

    @Override
    public void onNewTask(Task task) {
        TextView nullTasks = findViewById(R.id.tv_null_tasks);
        long taskId = taskDao.addTask(task);
        if (taskId != -1) {
            task.setId(taskId);
            taskAdapter.addItem(task);
            if (isNull) {
                isNull = false;
                nullTasks.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDeleteButtonClick(Task task) {
        TextView nullTasks = findViewById(R.id.tv_null_tasks);
        int result = taskDao.deleteTask(task);
        if (result > 0) {
            taskAdapter.removeItem(task);
            if (taskAdapter.getItemCount() == 0) {
                isNull = true;
                nullTasks.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemLongPress(Task task) {
        TextView nullTasks = findViewById(R.id.tv_null_tasks);
        EditTaskDialog dialog = new EditTaskDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", task);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "null");
    }

    @Override
    public void onItemCheckedChange(Task task) {
        taskDao.updateTask(task);
    }

    @Override
    public void onEditTask(Task task) {
        int result = taskDao.updateTask(task);
        if (result > 0) {
            taskAdapter.updateItem(task);
        }
    }
}